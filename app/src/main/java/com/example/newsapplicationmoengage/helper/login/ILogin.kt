package com.example.newsapplicationmoengage.helper.login

interface ILogin {

    fun login(
        userLoginId: String,
        password: String,
        onLoginSuccess: (userId : String) -> Unit,
        onLoginFailed: () -> Unit
    )

    fun logout(onLogoutComplete: () -> Unit)
}