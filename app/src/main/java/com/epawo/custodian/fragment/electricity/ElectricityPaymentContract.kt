package com.epawo.custodian.fragment.electricity

import com.epawo.custodian.model.energy.response.ElectricityPaymentResponse

interface ElectricityPaymentContract {

    interface ElectricityPaymentView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onPaymentSuccessful(response : ElectricityPaymentResponse)
    }

    interface ElectricityPaymentListener{
        fun onPaymentSuccessful(response : ElectricityPaymentResponse)
        fun onFailure(message : String?)
    }
}