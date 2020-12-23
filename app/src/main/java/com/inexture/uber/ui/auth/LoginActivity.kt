package com.inexture.uber.ui.auth

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.inexture.uber.R
import com.livinglifetechway.k4kotlin.databinding.setBindingView
import com.uber.sdk.android.core.auth.AccessTokenManager
import com.uber.sdk.android.core.auth.AuthenticationError
import com.uber.sdk.android.core.auth.LoginCallback
import com.uber.sdk.android.core.auth.LoginManager
import com.uber.sdk.core.auth.AccessToken
import android.content.Intent
import com.inexture.uber.databinding.LoginActivityBinding
import com.livinglifetechway.k4kotlin.core.onClick


class LoginActivity : AppCompatActivity() {


    private lateinit var loginManager: LoginManager
    val loginCallback = object : LoginCallback {
        override fun onLoginSuccess(accessToken: AccessToken) {
            Log.e("LoginActivity", "access token $accessToken")
        }

        override fun onLoginCancel() {
            Log.e("LoginActivity", "onLoginCancel")

        }

        override fun onLoginError(error: AuthenticationError) {
            Log.e("LoginActivity", "onLoginError $error")

        }

        override fun onAuthorizationCodeReceived(authorizationCode: String) {
            Log.e("LoginActivity", "onLoginError $authorizationCode")

        }
    }

    private lateinit var viewModel: LoginViewModel
    private lateinit var mBinding: LoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = setBindingView(R.layout.login_activity)
        Log.e("Login Activity", "Started")

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        mBinding.btnUberLogin.onClick {
            //start uber login
            loginUber()
        }

        mBinding.btnStartRiding.onClick {
            //start riding
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loginManager.onActivityResult(this, requestCode, resultCode, data)
    }

    private fun loginUber() {

        val accessTokenManager = AccessTokenManager(this)
        loginManager = LoginManager(accessTokenManager, loginCallback)
        loginManager.login(this)

    }
}
