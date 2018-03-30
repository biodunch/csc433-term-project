package com.emmanuelkehinde.stream_app.network

import com.emmanuelkehinde.stream_app.data.response.LoginResponse
import com.emmanuelkehinde.stream_app.data.response.RegisterResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(): Observable<LoginResponse>

    @POST("register")
    fun register(): Observable<RegisterResponse>
}