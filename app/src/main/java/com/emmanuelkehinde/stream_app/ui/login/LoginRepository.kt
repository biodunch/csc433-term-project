package com.emmanuelkehinde.stream_app.ui.login

import android.os.Handler
import com.emmanuelkehinde.stream_app.data.response.LoginResponse
import com.emmanuelkehinde.stream_app.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginRepository(var apiService: ApiService) {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    internal fun loginUser(email: String, password: String, successHandler: (LoginResponse) -> Unit, failureHandler: (Throwable?) -> Unit) {
        Handler().postDelayed({
            successHandler(LoginResponse(true,"",""))
        },3000)

//        compositeDisposable.add(
//                apiService.login()
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