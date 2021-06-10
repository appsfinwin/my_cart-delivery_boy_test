package com.finwin.mycart_admin.sign_up.pojo.sign_up_response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    val `data`: Data
)

data class Data(
    @SerializedName("Table1")
    val signUpData: List<SignUpData>
)

data class SignUpData(
    val ReturnID: String,
    val ReturnMessage: String,
    val ReturnStatus: String
)