package com.emmanuelkehinde.stream_app.ui.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class LoginViewModel: ViewModel() {

    @Inject
    lateinit var loginRepository: LoginRepository

    var validationError = MutableLiveData<String>()

    var isLoading = MutableLiveData<Boolean>()

    var loginError = MutableLiveData<Throwable>()

    var loginResponse = MutableLiveData<String>()

    internal fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            validationError.value = "Email or Password cannot be empty"
            return
        }

        isLoading.value = true
        loginRepository.loginUser(email,password,
                {
                    isLoading.value = false
                    if (it.status) {
                        loginResponse.value = it.data
                    } else {
                        loginError.value = Throwable(it.message)
                    }
                },{
                    isLoading.value = false
                    loginError.value = it
                }
        )
    }
}
