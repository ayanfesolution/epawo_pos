package com.epawo.egole.fragment.cashout

import com.epawo.egole.model.cashout.CashoutErrorResponse
import com.epawo.egole.model.cashout.CashoutResponseModel

interface CashoutContract {

    interface CashoutView{
        fun showToast(message: String?)
        fun showProgress()
        fun hideProgress()
        fun cashoutFailed(response : String?)
        fun cashoutSuccessFul(cashout: CashoutResponseModel)
        fun onExpiredSessionId(message : String?)
        fun onException(message: String?)
    }

    interface CashoutListener{
        fun onSuccess(response: CashoutResponseModel)
        fun onFailure(response: String?)
        fun onException(message : String?)
        fun onExpiredSessionId(message : String?)
    }
}