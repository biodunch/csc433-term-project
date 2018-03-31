package com.emmanuelkehinde.stream_app.ui.streaming

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import com.emmanuelkehinde.stream_app.R
import android.os.Build
import kotlinx.android.synthetic.main.activity_stream.*
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.view.TextureView
import android.os.HandlerThread
import android.media.ImageReader
import android.os.Handler
import android.util.Size
import android.widget.Toast
import android.support.annotation.NonNull
import android.support.v4.app.ActivityCompat
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import java.util.*
import com.emmanuelkehinde.stream_app.ui.base.BaseActivity


class StreamingActivity : BaseActivity(), SurfaceHolder.Callback {

//    private var configuration: R5Configuration
    private var camera: Camera? = null
    private var isPublishing = false
//    protected var stream: R5Stream? = null

    private val REQUEST_CAMERA_PERMISSION = 200
    private var cameraId: String? = null
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSessions: CameraCaptureSession? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var imageDimension: Size? = null
    private var imageReader: ImageReader? = null
    private var mBackgroundHandler: Handler? = null
    private var mBackgroundThread: HandlerThread? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

//        configuration = R5Configuration(R5StreamProtocol.RTSP, "localhost", 8554, "live", 1.0f)
//        configuration.setLicenseKey("YOUR-SDK-LICENSE-KEY")
//        configuration.setBundleID(this.getPackageName())

        textureView?.surfaceTextureListener = textureListener

        btn_start_stream.setOnClickListener {
            showConfirmDialog(null,"Start LiveStream?",{
                //TODO Start LiveStream

                showToast(getString(R.string.prompt_msg_livestream_started))
                btn_end_stream.visibility = View.VISIBLE
                btn_start_stream.visibility = View.GONE
            },null)
        }

        btn_end_stream.setOnClickListener {
            showConfirmDialog(null,"End LiveStream?",{
                //TODO Start LiveStream

                showToast(getString(R.string.prompt_msg_livestream_end))
                btn_end_stream.visibility = View.GONE
                btn_start_stream.visibility = View.VISIBLE
            },null)
        }

    }


    private var textureListener: TextureView.SurfaceTextureListener = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            //open your camera here
            openCamera()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            // Transform you image captured size according to the surface width and height
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    private val stateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            cameraDevice = camera
            createCameraPreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cameraDevice?.close()
            }
        }

        override fun onError(camera: CameraDevice, error: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cameraDevice?.close()
            }
            cameraDevice = null
        }
    }

    private fun createCameraPreview() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                val texture = textureView.surfaceTexture!!
                texture.setDefaultBufferSize(imageDimension!!.width, imageDimension!!.height)
                val surface = Surface(texture)
                captureRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    captureRequestBuilder?.addTarget(surface)

                    cameraDevice?.createCaptureSession(Arrays.asList(surface), object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(@NonNull cameraCaptureSession: CameraCaptureSession) {
                            //The camera is already closed
                            if (null == cameraDevice) {
                                return
                            }
                            // When the session is ready, we start displaying the preview.
                            cameraCaptureSessions = cameraCaptureSession
                            updatePreview()
                        }

                        override fun onConfigureFailed(@NonNull cameraCaptureSession: CameraCaptureSession) {
                            Toast.makeText(this@StreamingActivity, "Configuration change", Toast.LENGTH_SHORT).show()
                        }
                    }, null)
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }
    }

    private fun openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val manager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                cameraId = manager.getCameraIdList()[0]
                val characteristics = manager.getCameraCharacteristics(cameraId)
                val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]
                // Add permission for camera and let user grant the permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
                    return
                }
                manager.openCamera(cameraId, stateCallback, null)
            } catch (e: CameraAccessException ) {
                e.printStackTrace()
            }
        }
    }

     private fun updatePreview() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             captureRequestBuilder?.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
             try {
                 cameraCaptureSessions?.setRepeatingRequest(captureRequestBuilder?.build(), null, mBackgroundHandler)
             } catch (e: CameraAccessException) {
                 e.printStackTrace()
             }
         }
     }

    private fun startBackgroundThread() {
        mBackgroundThread = HandlerThread("Camera Background")
        mBackgroundThread?.start()
        mBackgroundHandler = Handler(mBackgroundThread?.looper)
    }

    private fun stopBackgroundThread() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mBackgroundThread?.quitSafely()
        }
        try {
            mBackgroundThread?.join()
            mBackgroundThread = null
            mBackgroundHandler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

    }

    private fun closeCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != cameraDevice) {
                    cameraDevice?.close()
                cameraDevice = null
            }
            if (null != imageReader) {
                imageReader?.close()
                imageReader = null
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this@StreamingActivity, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (textureView?.isAvailable!!) {
                openCamera()
            } else {
                textureView?.surfaceTextureListener = textureListener
            }
        } else {
            preview()
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    /**
     * LESS THAN LOLLIPOP
     * **/
    private fun preview() {
        camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        val surface = surfaceView
        surface.holder.addCallback(this)
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        try {
            camera?.setPreviewDisplay(surfaceHolder)
            camera?.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {

    }

    override fun surfaceCreated(p0: SurfaceHolder?) {

    }
}
