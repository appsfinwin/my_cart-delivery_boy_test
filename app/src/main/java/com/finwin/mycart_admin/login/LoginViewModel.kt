package com.finwin.mycart_admin.login

import android.app.Application
import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cn.pedant.SweetAlert.SweetAlertDialog
import com.finwin.mycart_admin.login.action.LoginAction
import com.finwin.mycart_admin.retrofit.ApiInterface
import com.finwin.mycart_admin.retrofit.RetrofitClient
import com.finwin.mycart_admin.services.Services
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.HashMap

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var ob_userName=ObservableField("")
    var ob_password=ObservableField("")
//    var ob_userName=ObservableField("ajuvijay2017@gmail.com")
//    var ob_password=ObservableField("ajuvijay91")
    lateinit var apiInterface: ApiInterface
    var repository= LoginRepository().getInstance()
    var mAction: MutableLiveData<LoginAction> = MutableLiveData()

    var loading: SweetAlertDialog? = null
    fun initLoading(context: Context?) {
        loading = Services.showProgressDialog(context)
    }

    fun cancelLoading() {
        if (loading != null) {
            loading!!.cancel()
            loading = null
        }
    }

    init {
        repository.mAction=mAction
    }
    public fun clickLogin(view:View)
    {
        if (ob_userName.get().equals(""))
        {
            showSnakbar("Username cannot be empty",view)
        }else if (ob_password.get().equals(""))
        {
            showSnakbar("Password cannot be empty",view)
        }else
        {
            initLoading(view.context)
            login()
        }
    }

    public fun clickSignUp(view:View)
    {
        mAction.value= LoginAction(LoginAction.CLICK_SIGN_UP)
    }

    public fun login() {

        val jsonParams: MutableMap<String?, Any?> = HashMap()
        jsonParams["Flag"] = "CUST_LOGIN"
        jsonParams["UserName"] = ob_userName.get()
        jsonParams["Password"] = ob_password.get()

        val body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(jsonParams).toString()
        )

        apiInterface = RetrofitClient().RetrofitClient()?.create(ApiInterface::class.java)!!
        repository.login(apiInterface, body)
    }

    fun showSnakbar(message: String, view: View) {
        val snackbar = Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    override fun onCleared() {
        super.onCleared()
        mAction.value= LoginAction(LoginAction.DEFAULT)
    }
}