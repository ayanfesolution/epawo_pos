package com.epawo.egole.fragment.electricity

import com.epawo.egole.model.energy.response.EnergyProviderResponse
import com.epawo.egole.model.energy.response.ValidateMeterNumberResponse

interface ElectricityProvidersContract {

    interface ElectricityProvidersView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onLoadProviderSuccess(response : EnergyProviderResponse)
        fun onValidateMeterNumberSuccess(response : ValidateMeterNumberResponse)
    }

    interface ElectricityProvidersListener{
        fun onLoadProviderSuccess(response : EnergyProviderResponse)
        fun onValidateMeterNumberSuccess(response : ValidateMeterNumberResponse)
        fun onFailure(message : String?)
    }
}