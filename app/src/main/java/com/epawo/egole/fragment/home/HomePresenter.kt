package com.epawo.egole.fragment.home

import com.epawo.egole.model.wallet_balance.response.WalletBalanceResponse

class HomePresenter(view : HomeContract.HomeView)
    : HomeContract.HomeListener {

    var view : HomeContract.HomeView
    var interactor : HomeInteractor

    init {
        this.view = view
        interactor = HomeInteractor(this)
    }

    override fun onSuccess(response: WalletBalanceResponse) {
        view.onSuccess(response)
    }

    override fun onFailure(message: String?) {
        view.showToast(message)
    }

    fun loadWalletBalance(token : String, accountNumber : String){
        view.showToast("Loading wallet balance....")
        interactor.loadBalance(token, accountNumber)
    }
}