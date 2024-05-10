package com.epawo.custodian.fragment.cable_tv

import com.epawo.custodian.model.cable.request.CablePaymentRequest
import com.epawo.custodian.model.cable.response.CablePaymentResponse

class CablePaymentPresenter(view : CablePaymentContract.CablePaymentView)
    : CablePaymentContract.CablePaymentListener {

    var view : CablePaymentContract.CablePaymentView
    var interactor : CablePaymentInteractor

    init {
        this.view = view
        interactor = CablePaymentInteractor(this)
    }

    override fun onSuccess(response: CablePaymentResponse) {
        view.hideProgress()
        if(response.status == "Successful"){
            view.onSuccess(response)
        }else{
            if(response.message == null){
                view.showToast("Cable Payment Failed")
            }else{
                view.showToast(response.message)
            }
        }
    }

    override fun onFailure(message: String?) {
       view.hideProgress()
       view.showToast(message)
    }

    fun makePayment(token : String, request : CablePaymentRequest){
        view.showProgress()
        interactor.cablePayment(token, request)
    }
}