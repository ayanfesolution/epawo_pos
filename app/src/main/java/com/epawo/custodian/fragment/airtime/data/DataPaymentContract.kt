package com.epawo.custodian.fragment.airtime.data

import com.epawo.custodian.model.airtime.response.AirtimeDataPaymentResponse

interface DataPaymentContract {

    interface DataPaymentView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onPaymentSuccessful(response : AirtimeDataPaymentResponse)
    }

    interface DataPaymentListener{
        fun onPaymentSuccessful(response : AirtimeDataPaymentResponse)
        fun onFailure(message : String?)
    }
}