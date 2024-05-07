package com.epawo.egole.model.cable.response

data class CableLookupResponse(
    val isSuccessful : Boolean,
    val responseModel : CableLookupModel,
    val message : String,
    val statusCode : String? = "",
    val productID : Int
)

data class CableLookupModel(
    val name : String,
    val reference : String,
    val business_reference : String? = "",
    val address : String? = "",
    val type : String,
    val surauth : String

)
