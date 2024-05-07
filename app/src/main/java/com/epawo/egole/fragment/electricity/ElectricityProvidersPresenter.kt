package com.epawo.egole.fragment.electricity

import com.epawo.egole.model.energy.request.ValidateMeterNumberRequest
import com.epawo.egole.model.energy.response.EnergyProviderResponse
import com.epawo.egole.model.energy.response.ValidateMeterNumberResponse

class ElectricityProvidersPresenter(view : ElectricityProvidersContract.ElectricityProvidersView)
    : ElectricityProvidersContract.ElectricityProvidersListener {

    var view : ElectricityProvidersContract.ElectricityProvidersView
    var interactor : ElectricityProviderInteractor

    init {
        this.view = view
        interactor = ElectricityProviderInteractor(this)
    }

    override fun onLoadProviderSuccess(response: EnergyProviderResponse) {
        view.hideProgress()
        view.onLoadProviderSuccess(response)
    }

    override fun onValidateMeterNumberSuccess(response: ValidateMeterNumberResponse) {
        view.hideProgress()
        view.onValidateMeterNumberSuccess(response)
    }

    override fun onFailure(message: String?) {
       view.hideProgress()
       view.showToast(message)
    }

    fun getEnergyProviders(token : String){
        view.showProgress()
        interactor.getProviders(token)
    }

    fun validateMeterNumber(token : String, request : ValidateMeterNumberRequest){
        view.showProgress()
        interactor.validateMeterNumber(token, request)
    }
}