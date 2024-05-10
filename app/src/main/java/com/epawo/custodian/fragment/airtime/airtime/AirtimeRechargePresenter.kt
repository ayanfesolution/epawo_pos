package com.epawo.custodian.fragment.airtime.airtime

import com.epawo.custodian.model.airtime.request.AirtimeRechargeRequest
import com.epawo.custodian.model.airtime.response.AirtimeRechargeResponse

class AirtimeRechargePresenter(view : AirtimeRechargeContract.AirtimeRechargeView)
    : AirtimeRechargeContract.AirtimeRechargeListener {

    var view : AirtimeRechargeContract.AirtimeRechargeView
    var interactor : AirtimeRechargeInteractor

    init {
        this.view = view
        interactor = AirtimeRechargeInteractor(this)
    }

    override fun onRechargeSuccess(response: AirtimeRechargeResponse) {
        view.hideProgress()
        if(response.status == "200"){
            view.onRechargeSuccess(response)
        }else{
            view.showToast(response.message)
        }
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    fun rechargePhone(token : String, request : AirtimeRechargeRequest){
        view.showProgress()
        interactor.performRecharge(token, request)
    }
}