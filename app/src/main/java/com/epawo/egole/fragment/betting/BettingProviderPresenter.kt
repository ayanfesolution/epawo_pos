package com.epawo.egole.fragment.betting

import com.epawo.egole.model.betting.request.BettingLookupRequest
import com.epawo.egole.model.betting.response.BettingProviderResponse

class BettingProviderPresenter(view : BettingContract.BettingView)
    : BettingContract.BettingListener {


    var view : BettingContract.BettingView
    var interactor : BettingInteractor

    init {
        this.view = view
        interactor = BettingInteractor(this)
    }

    override fun onLoadBettingProvider(response: BettingProviderResponse) {
        view.hideProgress()
        view.onLoadBettingProvider(response)
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }
    fun loadBettingProviders(token : String){
        view.showProgress()
        interactor.loadBettingProviders(token)
    }

    fun lookupSubscriber(token : String, request : BettingLookupRequest){
        view.showProgress()
        interactor.lookup(token, request)
    }
}