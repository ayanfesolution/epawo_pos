package com.epawo.egole.fragment.transactions

import com.epawo.egole.model.transaction.response.TransactionResponseModel

class TransactionPresenter(view : TransactionContract.TransactionView)
    : TransactionContract.TransactionListener {

    var view : TransactionContract.TransactionView
    var interactor : TransactionInteractor

    init {
        this.view = view
        interactor = TransactionInteractor(this)
    }

    override fun onSuccess(response: TransactionResponseModel) {
        view.hideProgress()
        view.onSuccess(response)
    }

    override fun onLoadMoreSuccess(response: TransactionResponseModel) {
        view.onLoadMoreSuccess(response)
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    fun loadTransaction(token : String, page : String){
        view.showProgress()
        interactor.loadTransactions(token, page)
    }

    fun loadMore(token : String, page : String){
        view.showProgress()
        interactor.loadTransactions(token, page)
    }
}