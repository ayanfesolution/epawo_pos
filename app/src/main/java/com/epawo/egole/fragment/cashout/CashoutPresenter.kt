package com.epawo.egole.fragment.cashout

import com.epawo.egole.model.cashout.CashoutRequestModel
import com.epawo.egole.model.cashout.CashoutResponseModel

class CashoutPresenter(view : CashoutContract.CashoutView) : CashoutContract.CashoutListener {

    private var view : CashoutContract.CashoutView
    private var inputAmountInteractor : CashoutInteractor

    init {
        this.view = view
        inputAmountInteractor = CashoutInteractor(this)
    }

    override fun onSuccess(response: CashoutResponseModel) {
        view.hideProgress()
        view.cashoutSuccessFul(response)
    }

    override fun onFailure(response: String?) {
        view.hideProgress()
        view.cashoutFailed(response)
    }

    override fun onException(message: String?) {
        view.hideProgress()
        view.onException(message)
    }

    override fun onExpiredSessionId(message: String?) {
        view.hideProgress()
        view.onExpiredSessionId(message)
    }

    fun cashOut(token: String, request : CashoutRequestModel){
        view.showProgress()
        inputAmountInteractor.doCashOut(token, request)
    }
}