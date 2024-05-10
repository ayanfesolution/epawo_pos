package com.epawo.custodian.fragment.betting

import com.epawo.custodian.model.betting.response.BettingProviderResponse

interface BettingContract {
    interface BettingView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onLoadBettingProvider(response : BettingProviderResponse)
    }

    interface BettingListener{
        fun onLoadBettingProvider(response : BettingProviderResponse)
        fun onFailure(message : String?)
    }
}