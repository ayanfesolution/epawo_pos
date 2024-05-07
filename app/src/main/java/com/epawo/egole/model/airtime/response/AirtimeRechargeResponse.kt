package com.epawo.egole.model.airtime.response

data class AirtimeRechargeResponse(
    val amount : String,
    val message : String,
    val status : String,
    val transId : String,
    val date : String,
    val phone : String,
    val type : String
)
