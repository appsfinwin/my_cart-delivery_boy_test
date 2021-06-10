package com.finwin.mycart_admin.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.finwin.mycart_admin.MainActivity
import com.finwin.mycart_admin.R
import com.finwin.mycart_admin.databinding.ActivityLoginBinding
import com.finwin.mycart_admin.login.action.LoginAction
import com.finwin.mycart_admin.sign_up.SignUpActivity
import com.finwin.mycart_admin.utils.Constants

class LoginActivity : AppCompatActivity() {

    var sharedPreferences: SharedPreferences? = null
    var edit: SharedPreferences.Editor? = null
    lateinit var viewModel: LoginViewModel
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel= ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel=viewModel
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE)
        edit= sharedPreferences?.edit()

        
        viewModel.mAction.observe(this, Observer {
            viewModel.cancelLoading()
            when(it.action){
                LoginAction.LOGIN_SUCCESS->{
                    val i = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(i)
                    edit?.putBoolean(Constants.IS_LOGIN,true)
                    edit?.putString(Constants.USERNAME,viewModel.ob_userName.get())
                    edit?.putString(Constants.PASSWORD,viewModel.ob_password.get())
                    edit?.commit()
                    finish()
                }
                LoginAction.API_ERROR->{
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText(it.error)
                        .show()
                }

                LoginAction.CLICK_SIGN_UP->{
                    val i = Intent(this@LoginActivity, SignUpActivity::class.java)
                    startActivity(i)
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mAction.value= LoginAction(LoginAction.DEFAULT)
    }
}