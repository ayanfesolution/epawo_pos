package com.epawo.custodian.model.forgot_password.response

data class ForgotPasswordResponse(
    val message : String,
    val isSucceded : Boolean,
    val errors : String? = "",
    val expiredDate : String? = ""
)
