package com.epawo.custodian.model.energy.response

data class ElectricityPaymentResponse(
    val productID : Int,
    val statusCode : String,
    val message : String,
    val isSuccessful : Boolean,
    val transIld : String,
    val responseModel : ElectricityResponseModel,
)

data class ElectricityResponseModel(
    val transactionReference : String,
    val transactionMessage : String,
    val token : String,
    val providerResponse : String,
    val accountName : String,
    val account : String,
    val address : String
)

