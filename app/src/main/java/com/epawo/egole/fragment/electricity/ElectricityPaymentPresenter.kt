package com.epawo.egole.fragment.electricity

import com.epawo.egole.model.energy.request.ElectricityPaymentRequest
import com.epawo.egole.model.energy.response.ElectricityPaymentResponse

class ElectricityPaymentPresenter(view : ElectricityPaymentContract.ElectricityPaymentView)
    : ElectricityPaymentContract.ElectricityPaymentListener {

    var view : ElectricityPaymentContract.ElectricityPaymentView
    var interactor : ElectricityPaymentInteractor

    init {
        this.view = view
        interactor = ElectricityPaymentInteractor(this)
    }

    override fun onPaymentSuccessful(response: ElectricityPaymentResponse) {
        view.hideProgress()
        if(response.statusCode == "200"){
            view.onPaymentSuccessful(response)
        }else{
            view.showToast(response.message)
        }
    }

    override fun onFailure(message: String?) {
       view.hideProgress()
       view.showToast(message)
    }

    fun makePayment(token : String, request : ElectricityPaymentRequest){
        view.showProgress()
        interactor.makePayment(token, request)
    }
}