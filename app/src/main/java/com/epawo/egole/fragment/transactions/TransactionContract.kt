package com.epawo.egole.fragment.transactions

import com.epawo.egole.model.transaction.response.TransactionResponseModel

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