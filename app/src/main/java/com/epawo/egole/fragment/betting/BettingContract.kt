package com.epawo.egole.fragment.betting

import com.epawo.egole.model.betting.response.BettingProviderResponse

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