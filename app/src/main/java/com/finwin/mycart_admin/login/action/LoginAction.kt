package com.finwin.mycart_admin.login.action

import com.finwin.mycart_admin.login.pojo.LoginResponse

class LoginAction {
    companion object {
        public const val DEFAULT= -1
        public const val LOGIN_SUCCESS= 1
        public const val API_ERROR= 2
        public const val CLICK_SIGN_UP= 3

    }
    var action: Int = 0
     var error: String=""
    lateinit var loginResponse: LoginResponse

    constructor(action: Int) {
        this.action = action
    }

    constructor(action: Int, error: String) {
        this.action = action
        this.error = error
    }

    constructor(action: Int, loginResponse: LoginResponse) {
        this.action = action
        this.loginResponse = loginResponse
    }


}