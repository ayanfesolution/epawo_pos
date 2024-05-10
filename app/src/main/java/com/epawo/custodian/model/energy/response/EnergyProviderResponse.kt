package com.epawo.custodian.model.energy.response

data class EnergyProviderResponse(
    val isSuccessful : Boolean,
    val message : String,
    val statusCode : Any,
    val responseModel : List<EnergyResponseModel>
)

data class EnergyResponseModel(
    val id : Int,
    val providerCode : String,
    val code : String,
    val providerId : Any,
    val name : String,
    val products : List<EnergyProducts>,
    val serviceCode : String,
    val logo : String
)

data class EnergyProducts(
    val isAmountFixed : Boolean,
    val name : String,
    val amount : Any,
    val code : String,
    val productCode : String,
    val itemFee : Any

)
