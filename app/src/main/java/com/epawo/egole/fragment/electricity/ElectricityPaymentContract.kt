package com.epawo.egole.fragment.electricity

import com.epawo.egole.model.energy.response.ElectricityPaymentResponse

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