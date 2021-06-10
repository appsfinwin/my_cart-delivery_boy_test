package com.finwin.mycart_admin.login.pojo

data class LoginResponse(
    val `data`: Data
)

data class Data(
    val Table1: List<Table1>
)

data class Table1(
    val BranchId: String,
    val FirmId: String,
    val Name: String,
    val ReturnStatus: String,
    val UserId: String,
    val UserReferenceId: String,
    val UserRole: String,
    val UserType: String
)
