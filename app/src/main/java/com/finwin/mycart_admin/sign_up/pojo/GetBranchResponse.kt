package com.finwin.mycart_admin.sign_up.pojo

import com.google.gson.annotations.SerializedName

data class GetBranchResponse(
    val `data`: Data
)

data class Data(
    @SerializedName("Table")
    val branchData: List<BranchData>
)

data class BranchData(
    val Address: String,
    val BranchId: String,
    val BranchName: String,
    val BranchRegNo: String,
    val Contact: String,
    val Delivery: String,
    val FirmId: String,
    val FirmName: String

)