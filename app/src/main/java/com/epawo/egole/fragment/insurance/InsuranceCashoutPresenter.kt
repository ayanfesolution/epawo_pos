package com.epawo.egole.fragment.insurance

import com.epawo.egole.model.insurance.InsuranceCashoutRequest
import com.epawo.egole.model.insurance.InsuranceCashoutResponse


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

    fun insuranceCashout(token : String, request : InsuranceCashoutRequest){
        view.showProgress()
        interactor.insuranceCashout(token, request)
    }

}