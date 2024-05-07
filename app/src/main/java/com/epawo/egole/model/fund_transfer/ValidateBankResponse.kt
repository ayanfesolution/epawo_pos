package com.epawo.egole.model.fund_transfer

data class ValidateBankResponse(
    val status : String,
    val message : String,
    val data : AccountData
)

data class AccountData(
    val name : String,
    val clientId : String,
    val bvn : String,
    val account : Account,
    val status : String,
    val currency : String,
    val bank : String,
    val bankCode : String
)

data class Account(
    val number : String,
    val id : String
)
