package com.finwin.mycart_admin.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.finwin.mycart_admin.login.action.LoginAction
import com.finwin.mycart_admin.retrofit.ApiInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody

class LoginRepository{

    lateinit var INSTANCE: LoginRepository
    lateinit var mAction: MutableLiveData<LoginAction>

    fun getInstance() :LoginRepository{
        if (!::INSTANCE.isInitialized) {
            INSTANCE= LoginRepository()
        }
        return INSTANCE
    }
    @SuppressLint("CheckResult")
    fun login(apiInterface: ApiInterface, body: RequestBody?) {
        val observable = apiInterface.login(body)
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->

                    if (response.data.Table1.size>0) {

                        if (response.data.Table1[0].ReturnStatus.equals("Y")) {
                            mAction.value = LoginAction(
                                LoginAction.LOGIN_SUCCESS,
                                response
                            )
                        } else {

                            mAction.value = LoginAction(
                                LoginAction.API_ERROR,
                               "Invalid Username/password"
                            )
                        }
                    }
                }, { error ->
                    mAction.value =
                        LoginAction(
                            LoginAction.API_ERROR,
                            error.message.toString()
                        )
                }
            )

    }
}