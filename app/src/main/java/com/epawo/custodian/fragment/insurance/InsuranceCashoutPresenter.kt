package com.epawo.custodian.fragment.insurance

import com.epawo.custodian.model.insurance.InsuranceCashoutRequest
import com.epawo.custodian.model.insurance.InsuranceCashoutResponse


class InsuranceCashoutPresenter(view: InsurancePolicyContract.InsuranceCashoutView)
    : InsurancePolicyContract.InsuranceCashoutListener{

    private var view : InsurancePolicyContract.InsuranceCashoutView
    private var interactor: InsuranceCashoutInteractor

    init {
        this.view = view
        interactor = InsuranceCashoutInteractor(this)
    }

    override fun onSuccess(response: InsuranceCashoutResponse) {
        view.hideProgress()
        view.onSuccess(response)
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    override fun onException(message: String?) {
        view.hideProgress()
        view.onException(message)
    }

    fun insuranceCashout(token : String, request : InsuranceCashoutRequest){
        view.showProgress()
        interactor.insuranceCashout(token, request)
    }

}