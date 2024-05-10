package com.epawo.custodian.interfaces

import com.epawo.custodian.model.transaction.response.Transactions

interface TransactionClickListener {
    fun onTransactionClicked(item : Transactions)
}