package com.emmanuelkehinde.stream_app.ui.register

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class RegisterViewModel: ViewModel() {

    @Inject
    lateinit var registerRepository: RegisterRepository

    private var validationError = MutableLiveData<String>()

    private var isLoading = MutableLiveData<Boolean>()

    private var registerError = MutableLiveData<Throwable>()

    private var registerResponse = MutableLiveData<String>()

    internal fun registerUser(fullName: String, email: String, password: String) {
        if (fullName.isEmpty()) {
            validationError.value = "Insert your Full Name"
            return
        }
        if (email.isEmpty() || password.isEmpty()) {
            validationError.value = "Email or Password cannot be empty"
            return
        }

        isLoading.value = true
        registerRepository.registerUser(fullName,email,password,
                {
                    isLoading.value = false
                    if (it.status) {
                        registerResponse.value = it.data
                    } else {
                        registerError.value = Throwable(it.message)
                    }
                },
                {
                    isLoading.value = false
                    registerError.value = it
                }
        )
    }

    fun getValidationError(): MutableLiveData<String> {
        return validationError
    }

    fun getRegisterError(): MutableLiveData<Throwable> {
        return registerError
    }

    fun getRegisterResponse(): MutableLiveData<String> {
        return registerResponse
    }

    fun isLoading(): MutableLiveData<Boolean> {
        return isLoading
    }
}