package com.finwin.mycart_admin.sign_up.action

import com.finwin.mycart_admin.login.pojo.LoginResponse
import com.finwin.mycart_admin.sign_up.pojo.GetBranchResponse
import com.finwin.mycart_admin.sign_up.pojo.sign_up_response.SignUpResponse

class SignUpAction {
    companion object {
        public const val DEFAULT= -1
        public const val SIGN_UP_SUCCESS= 1
        public const val API_ERROR= 2
        public const val CLICK_LOGIN= 3
        public const val GET_BRANCH_SUCCESS= 4
        public const val LOGIN_SUCCESS= 5

    }
    lateinit var getBranchResponse: GetBranchResponse
    lateinit var signUpResponse: SignUpResponse
    lateinit var loginResponse: LoginResponse

    var action: Int = 0
    lateinit var error: String

    constructor(action: Int) {
        this.action = action
    }

    constructor(action: Int, error: String) {
        this.action = action
        this.error = error
    }

    constructor(action: Int,signUpResponse: SignUpResponse) {
        this.signUpResponse = signUpResponse
        this.action = action
    }

    constructor(action: Int,getBranchResponse: GetBranchResponse) {
        this.getBranchResponse = getBranchResponse
        this.action = action
    }

    constructor( action: Int,loginResponse: LoginResponse) {
        this.loginResponse = loginResponse
        this.action = action
    }


}