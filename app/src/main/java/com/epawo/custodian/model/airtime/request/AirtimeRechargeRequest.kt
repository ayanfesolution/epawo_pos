package com.epawo.custodian.model.airtime.request

data class AirtimeRechargeRequest(
    var phoneNo : String,
    var amount : Int,
    var productID : Int,
    var productName : String,
    var userid : Int,
    var terminalID : String,
    var pin : String,
    var mainAcctNumber : String
)
