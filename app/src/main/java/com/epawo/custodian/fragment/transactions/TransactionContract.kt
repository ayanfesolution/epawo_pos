package com.epawo.custodian.fragment.transactions

import com.epawo.custodian.model.transaction.response.TransactionResponseModel

interface TransactionContract {

    interface TransactionView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response : TransactionResponseModel)
        fun onLoadMoreSuccess(response : TransactionResponseModel)
    }

    interface TransactionListener{
        fun onSuccess(response : TransactionResponseModel)
        fun onLoadMoreSuccess(response : TransactionResponseModel)
        fun onFailure(message : String?)
    }
}