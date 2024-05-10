package com.epawo.custodian.fragment.cashout

import com.epawo.custodian.model.cashout.CashoutResponseModel

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