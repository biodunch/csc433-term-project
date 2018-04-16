package com.emmanuelkehinde.stream_app.ui.streaming

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.emmanuelkehinde.shutdown.Shutdown
import com.emmanuelkehinde.stream_app.BuildConfig
import com.emmanuelkehinde.stream_app.R
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity
import com.red5pro.streaming.R5Connection
import com.red5pro.streaming.R5Stream
import com.red5pro.streaming.R5StreamProtocol
import com.red5pro.streaming.config.R5Configuration
import com.red5pro.streaming.event.R5ConnectionEvent
import com.red5pro.streaming.event.R5ConnectionListener
import com.red5pro.streaming.source.R5Camera2
import com.red5pro.streaming.source.R5Microphone
import com.red5pro.streaming.view.R5VideoView
import kotlinx.android.synthetic.main.activity_streaming2.*

@TargetApi(21)
class StreamingActivity2 : BaseActivity(), ConfirmStreamingDialog.ConfirmStreamingListener {

    private var preview: R5VideoView? = null
    private var publish: R5Stream? = null
    private lateinit var camera2: R5Camera2
    private var camera: CameraDevice? = null
    private var camInfo: CameraCharacteristics? = null
    private var camOrientation: Int = 0
    private val REQUEST_CAMERA_PERMISSION = 200
    private lateinit var configuration: R5Configuration
    private var isPublishing: Boolean = false
    private lateinit var connection: R5Connection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_streaming2)
        preview = findViewById(R.id.videoPreview)

        configuration = R5Configuration(R5StreamProtocol.RTSP, "192.168.8.100", 8554, "live", 1.0f)
        configuration.licenseKey = BuildConfig.red5pro_license_key
        configuration.bundleID = this.packageName

        setupR5Stream()

        btn_start_stream.setOnClickListener {
            if (isPublishing) {
                showConfirmDialog(null,"End LiveStream?",{

                    stopPublishing()
                    isPublishing = false
                    showToast(getString(R.string.prompt_msg_livestream_end))
                    this.recreate()
                    btn_start_stream?.text = "Start LiveStream"
                },null)
            } else {
                val confirmStreamingDialog = ConfirmStreamingDialog()
                confirmStreamingDialog.isCancelable = false
                confirmStreamingDialog.show(fragmentManager,"ConfirmStreamingDialog")
            }
        }
    }

    override fun onStart() {
        super.onStart()

        val manager = this.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val camList = manager.cameraIdList
            for (id in camList) {
                val info = manager.getCameraCharacteristics(id)
                if (info.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    camOrientation = info.get(CameraCharacteristics.SENSOR_ORIENTATION)!!
                    camInfo = info
                    // Add permission for camera and let user grant the permission
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAPTURE_AUDIO_OUTPUT, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
                        return
                    }
                    manager.openCamera(id, object : CameraDevice.StateCallback() {
                        override fun onOpened(device: CameraDevice) {
                            if (preview == null)
                                return
                            camera = device
                            setupCamera(device)
                            btn_start_stream.visibility = View.VISIBLE
                        }

                        override fun onDisconnected(camera: CameraDevice) {}

                        override fun onError(camera: CameraDevice, error: Int) {}
                    }, null)
                    break
                }
            }

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun setupCamera(camera: CameraDevice?) {
        camera2 = R5Camera2(camera, camInfo, 320, 640)
        camera2.bitrate = 750
        camera2.orientation = camOrientation
        camera2.framerate = 15
        publish?.attachCamera(camera2)
    }

    private fun setupR5Stream() {
        connection = R5Connection(configuration)

        //setup a new stream using the connection
        publish = R5Stream(connection)

        publish?.audioController?.sampleRate = 44100

        //show all logging
        publish?.setLogLevel(R5Stream.LOG_LEVEL_DEBUG)

        //attach a microphone
        val mic = R5Microphone()
        publish?.attachMic(mic)

        preview?.attachStream(publish)
    }

    private fun startPublishing(streamName: String) {
        publish?.setListener(r5ConnectionListener)
        publish?.publish(streamName, R5Stream.RecordType.Live)
    }

    private fun stopPublishing() {
        preview = null

        if (publish != null) {
            publish?.stop()

            if (camera != null) {
                camera?.close()
                camera = null
            }

            publish = null
        }
    }

    override fun onStop() {
        preview = null

        if (publish != null) {
            publish?.stop()

            if (camera != null) {
                camera?.close()
                camera = null
            }

            r5ConnectionListener = null
            publish = null
        }
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this@StreamingActivity2, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    private var r5ConnectionListener: R5ConnectionListener? = R5ConnectionListener { event ->
        Log.d("Publisher", ":onConnectionEvent " + event?.name)
        if (event?.name === R5ConnectionEvent.START_STREAMING.name) {

        }
    }

    override fun onBackPressed() {
        Shutdown.now(this)
    }

    override fun onYesTapped(streamName: String) {
        startPublishing(streamName)
        isPublishing = true
        showToast(getString(R.string.prompt_msg_livestream_started))
        btn_start_stream.text = "End LiveStream"
    }
}
