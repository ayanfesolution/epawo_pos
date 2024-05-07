package com.epawo.egole.model.cable.response

data class CablePaymentResponse(
    val message : String,
    val status : String,
    val amount : String,
    val transId : String,
    val date : String,
    val phone : String,
    val type: String,
    val Account : String
)
