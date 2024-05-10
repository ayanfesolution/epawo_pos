package com.epawo.custodian.model.energy.request

data class ElectricityPaymentRequest(
    var accountNumber : String,
    var productCode : String,
    var providerCode : String,
    var amount : Int,
    var phoneNumber : String,
    var accountName : String,
    var narration : String,
    var productID : Int,
    var userID : Int,
    var terminalID : String,
    var pin : String,
    var mainAcctNumber : String,
    var surauth : String,
    var type : String
)
