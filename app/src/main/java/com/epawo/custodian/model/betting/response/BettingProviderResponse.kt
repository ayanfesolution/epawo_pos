package com.epawo.custodian.model.betting.response

data class BettingProviderResponse(
    val message : String,
    val statusCode : String,
    val productID : String,
    val isSuccessful : String,
    val responseModel : List<BettingResponseModel>
)

data class BettingResponseModel(
    val id : Int,
    val providerCode : String,
    val code : String,
    val providerId : String? = "",
    val name : String,
    val serviceCide : String,
    val logo : String
)
