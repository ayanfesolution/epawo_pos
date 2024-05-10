package com.epawo.custodian.interfaces

import com.epawo.custodian.model.betting.response.BettingResponseModel

interface BettingProviderClickListener {

    fun onBettingProviderClicked(item : BettingResponseModel)
}