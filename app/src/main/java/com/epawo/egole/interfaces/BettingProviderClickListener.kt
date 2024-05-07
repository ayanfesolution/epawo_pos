package com.epawo.egole.interfaces

import com.epawo.egole.model.betting.response.BettingResponseModel

interface BettingProviderClickListener {

    fun onBettingProviderClicked(item : BettingResponseModel)
}