package com.example.newsapplicationmoengage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapplicationmoengage.databinding.ActivityLoginBinding
import com.example.newsapplicationmoengage.helper.MoeEventHelper
import com.example.newsapplicationmoengage.helper.SharedPreferencesHelper
import com.example.newsapplicationmoengage.helper.login.LocalLogin
import com.moengage.core.analytics.MoEAnalyticsHelper

class LoginActivity: AppCompatActivity() {

    private lateinit var bindings: ActivityLoginBinding
    private val localLogin = LocalLogin(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(SharedPreferencesHelper.getProperty(this, SharedPreferencesHelper.LOGGED_IN_USER) != null) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
        bindings = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        setUpLogin()
    }

    private fun setUpLogin() {
        bindings.apply {
            loginButton.setOnClickListener {
                val loginId = idEditText.text.toString()
                val password = passwordEditText.text.toString()
                localLogin.login(
                    userLoginId = loginId,
                    password = password,
                    onLoginSuccess = { userId ->
                        Log.i("LoginActivityTag", "Logged in with userid = $userId")

                        //Set Unique Id for the User
                        MoEAnalyticsHelper.setUniqueId(this@LoginActivity, userId)

                        MoeEventHelper.setMoEUserAttributes(this@LoginActivity)

                        finish()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    },
                    onLoginFailed = {
                        Log.i("LoginActivityTag", "Log in failed...")
                    }
                )
            }
        }
    }

}