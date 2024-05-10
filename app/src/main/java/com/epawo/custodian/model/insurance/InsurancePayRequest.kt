package com.epawo.custodian.model.insurance

data class InsuranceCashoutRequest(
    val accountType: Int,
    val biz_unit: String,
    val cardData: CardData,
    val cardSequenceNumber: String,
    val description: String,
    val email_address: String,
    val iccData: String,
    val issured_name: String,
    val payment_narrtn: String,
    val phone_number: String,
    val pin: String,
    val policy_number: String,
    val preferredChannel: Int,
    val premium: Int,
    val terminalID: String,
    val walletAccount: String
)

data class CardData(
    val expiryMonth: String,
    val expiryYear: String,
    val pan: String,
    val pinBlock: String,
    val track2: String
)