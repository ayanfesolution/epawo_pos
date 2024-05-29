package com.epawo.custodian.model.insurance


data class InsuranceCashoutRequest (
    val walletAccount: String,
    val terminalID: String,
    val pin: String,
    val policy_number: String,
    val third_party_id: String,
    val payment_amt: Int,
    val installment_count: Int,
    val reference_number: String,
    val tnxfee: Int,
    val accountType: Int,
    val cardData: CardData,
    val cardSequenceNumber: String,
    val iccData: String,
    val preferredChannel: Int
)

data class CardData (
    val expiryMonth: String,
    val expiryYear: String,
    val pan: String,
    val pinBlock: String,
    val track2: String
)

//data class InsuranceCashoutRequest(
//    val accountType: Int,
//    val biz_unit: String,
//    val cardData: CardData,
//    val cardSequenceNumber: String,
//    val description: String,
//    val email_address: String,
//    val iccData: String,
//    val issured_name: String,
//    val payment_narrtn: String,
//    val phone_number: String,
//    val pin: String,
//    val policy_number: String,
//    val preferredChannel: Int,
//    val premium: Int,
//    val terminalID: String,
//    val walletAccount: String
//)
//
//data class CardData(
//    val expiryMonth: String,
//    val expiryYear: String,
//    val pan: String,
//    val pinBlock: String,
//    val track2: String
//)