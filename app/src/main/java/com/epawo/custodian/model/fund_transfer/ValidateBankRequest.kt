package com.epawo.custodian.model.fund_transfer

data class ValidateBankRequest(
    var bankCode : String,
    var accountNumber : String
)
