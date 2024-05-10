package com.epawo.custodian.model.energy.request

data class ValidateMeterNumberRequest(
    var serviceType : String,
    var accountNumber : String,
    var type : String,
    var amount : String
)
