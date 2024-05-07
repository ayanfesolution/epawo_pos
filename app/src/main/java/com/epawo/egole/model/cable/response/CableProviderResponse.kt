package com.epawo.egole.model.cable.response

data class CableProviderResponse(
    val isSuccessful : Boolean,
    val responseModel : List<CableProviderModel>,
    val message : String,
    val statusCode : String? = ""
)

data class CableProviderModel(
    val id : Int,
    val providerCode : String,
    val code : String,
    val providerId : String? = "",
    val name : String,
    val products : Any,
    val serviceCode : String,
    val logo : String
)
