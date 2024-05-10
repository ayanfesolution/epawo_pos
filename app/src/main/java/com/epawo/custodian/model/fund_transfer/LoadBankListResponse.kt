package com.epawo.custodian.model.fund_transfer

data class LoadBankListResponse(
    var bankslist : List<BankList>
)

data class BankList(
    var bankID : Int,
    var bankName : String,
    var bankCode : String,
    var dateCreated : String,
    var createdBy : Any
)
