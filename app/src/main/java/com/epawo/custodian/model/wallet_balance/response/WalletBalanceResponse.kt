package com.epawo.custodian.model.wallet_balance.response

data class WalletBalanceResponse(
    val userID : Int,
    val mainWallet : String,
    val phoneNumber : String,
    val companyName : String,
    val businessAddress : String,
    val businessName : String,
    val businessDescription : String,
    val firstName : String,
    val middleName : String,
    val lastName : String,
    val email : String,
    val residentialState : String,
    val role : String,
    val roleID : String,
    val terminalId : String,
    val walletBalance : Double,
    val openingBalance : Double,
    val closingBalance : Double,
    val result : String,
    val statusCode : String,
    val message : String
)
