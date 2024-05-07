package com.epawo.egole.fragment.cable_tv

import com.epawo.egole.model.cable.response.CablePaymentResponse

interface CablePaymentContract {

    interface CablePaymentView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response : CablePaymentResponse)
    }

    interface CablePaymentListener{
        fun onSuccess(response : CablePaymentResponse)
        fun onFailure(message : String?)
    }
}