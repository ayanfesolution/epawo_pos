package com.epawo.egole.model.energy.response

data class ValidateMeterNumberResponse(
    val isSuccessful : Boolean,
    val responseModel : ResponseModel,
    val message : String,
    val statusCode : Any,
    val productID : Int
)

data class ResponseModel(
    val name : String,
    val reference : String,
    val business_reference : String? = "",
    val address : String,
    val type : String,
    val surauth : String
)
