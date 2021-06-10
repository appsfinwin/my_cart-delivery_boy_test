package com.finwin.mycart_admin.sign_up

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
import com.finwin.mycart_admin.databinding.ActivitySignUpBinding
import com.finwin.mycart_admin.sign_up.action.SignUpAction
import com.finwin.mycart_admin.utils.Constants

class SignUpActivity : AppCompatActivity() {

   lateinit var binding:ActivitySignUpBinding
   lateinit var viewModel: SignUpViewModel

    var sharedPreferences: SharedPreferences? = null
    var edit: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_sign_up)
        viewModel= ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding.viewmodel=viewModel

        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE)
        edit= sharedPreferences?.edit()
        viewModel.initLoading(this)
        viewModel.getBranches()

        viewModel.mAction.observe(this, Observer {
            viewModel.cancelLoading()
            when(it.action)
            {
                SignUpAction.GET_BRANCH_SUCCESS->{
                    viewModel.setBranchData(it.getBranchResponse.data)
                }
                SignUpAction.SIGN_UP_SUCCESS->{

                    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("SUCCESS!")
                        .setContentText("Register success!")
                        .setConfirmClickListener {
                            viewModel.initLoading(this)
                            viewModel.login()
                        }
                        .show()


                }
                SignUpAction.API_ERROR->{
                    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText(it.error)
                        .show()
                }
                SignUpAction.LOGIN_SUCCESS->{
                    viewModel.cancelLoading()
                    val i = Intent(this@SignUpActivity, MainActivity::class.java)
                    edit?.putBoolean(Constants.IS_LOGIN,true)
                    edit?.putString(Constants.USERNAME,viewModel.obEmailId .get())
                    edit?.putString(Constants.PASSWORD,viewModel.obPassword .get())
                    edit?.commit()
                    startActivity(i)
                    finish()
                }
            }
        })
    }
}