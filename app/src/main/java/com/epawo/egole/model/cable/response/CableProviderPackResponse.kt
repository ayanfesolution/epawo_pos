package com.epawo.egole.model.cable.response

data class CableProviderPackResponse(
    val isSuccessful : Boolean,
    val responseModel : List<CablePack>,
    val message : String,
    val statusCode : String? = ""
)

data class CablePack(
    val isAmountFixed : Boolean,
    val name : String,
    val amount : Int,
    val code : String,
    val productCode : String,
    val itemFee : String? = ""
)
