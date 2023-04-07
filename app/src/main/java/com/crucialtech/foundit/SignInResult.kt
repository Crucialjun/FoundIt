package com.crucialtech.foundit

data class SignInResult(
    val data : UserData?,
    val errorMsg : String?
)

data class UserData(
    val uid : String,
    val username : String?,
    val picUrl : String?
)
