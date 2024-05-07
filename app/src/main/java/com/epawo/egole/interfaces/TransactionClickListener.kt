package com.epawo.egole.interfaces

import com.epawo.egole.model.transaction.response.Transactions

interface TransactionClickListener {
    fun onTransactionClicked(item : Transactions)
}