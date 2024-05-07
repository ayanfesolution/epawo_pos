package com.epawo.egole.fragment.home

import com.epawo.egole.model.wallet_balance.response.WalletBalanceResponse

interface HomeContract {

    interface HomeView{
        fun showToast(message : String?)
        fun onSuccess(response : WalletBalanceResponse)
    }

    interface HomeListener{
        fun onSuccess(response : WalletBalanceResponse)
        fun onFailure(message : String?)
    }
}