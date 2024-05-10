package com.epawo.custodian.fragment.ticket

import com.epawo.custodian.model.ticket.TicketResponseModel

class TicketListPresenter(view : TicketListContract.TicketListView) :
    TicketListContract.TicketListListener  {


    private var view : TicketListContract.TicketListView
    private var inputAmountInteractor : TicketListInteractor

    init {
        this.view = view
        inputAmountInteractor = TicketListInteractor(this)
    }

    override fun onSuccess(response: TicketResponseModel) {
        view.hideProgress()
        view.onSuccess(response)
    }

    override fun onFailure(response: String?) {
        view.hideProgress()
        view.showToast(response)
    }

    fun loadTickets(token : String, walletId : String, startDate : String, endDate : String){
        view.showProgress()
        inputAmountInteractor.loadTickets(token, walletId, startDate, endDate)
    }
}