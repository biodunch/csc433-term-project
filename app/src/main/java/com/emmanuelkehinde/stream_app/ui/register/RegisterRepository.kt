package com.emmanuelkehinde.stream_app.ui.register

import android.os.Handler
import com.emmanuelkehinde.stream_app.data.response.LoginResponse
import com.emmanuelkehinde.stream_app.data.response.RegisterResponse
import com.emmanuelkehinde.stream_app.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterRepository(var apiService: ApiService) {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    internal fun registerUser(fullName: String, email: String, password: String, successHandler: (RegisterResponse) -> Unit, failureHandler: (Throwable?) -> Unit) {
        Handler().postDelayed({
            successHandler(RegisterResponse(true,"",""))
        },3000)

//        compositeDisposable.add(
//                apiService.register()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe({
//                            successHandler(it)
//                        },{
//                            failureHandler(it)
//                        })
//        )
    }

}