package com.epawo.custodian.fragment.airtime.data

import com.epawo.custodian.model.airtime.request.AirtimeDataRequest
import com.epawo.custodian.model.airtime.response.AirtimeDataPaymentResponse

class DataPaymentPresenter(view : DataPaymentContract.DataPaymentView)
    : DataPaymentContract.DataPaymentListener {

    var view : DataPaymentContract.DataPaymentView
    var interactor : DataPaymentInteractor

    init {
        this.view = view
        interactor = DataPaymentInteractor(this)
    }

    override fun onPaymentSuccessful(response: AirtimeDataPaymentResponse) {
        view.hideProgress()
        if(response.status == "200"){
            view.onPaymentSuccessful(response)
        }else{
            view.showToast(response.message)
        }
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    fun makePayment(token : String, request : AirtimeDataRequest){
        view.showProgress()
        interactor.makePayment(token,request)
    }
}