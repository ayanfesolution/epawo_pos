package com.epawo.custodian.fragment.ticket

import com.epawo.custodian.model.ticket.TicketResponseModel

interface TicketListContract {

    interface TicketListView{
        fun showToast(message: String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response: TicketResponseModel)
    }

    interface TicketListListener{
        fun onSuccess(response: TicketResponseModel)
        fun onFailure(response: String?)
    }
}