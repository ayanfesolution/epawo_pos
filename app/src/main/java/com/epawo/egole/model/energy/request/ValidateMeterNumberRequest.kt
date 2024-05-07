package com.epawo.egole.model.energy.request

data class ValidateMeterNumberRequest(
    var serviceType : String,
    var accountNumber : String,
    var type : String,
    var amount : String
)
