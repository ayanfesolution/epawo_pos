package com.epawo.egole.fragment.insurance

import com.epawo.egole.model.insurance.InsuranceDetailResponse
import com.epawo.egole.model.insurance.InsuranceDetailsRequest

class InsurancePresenter(view: InsurancePolicyContract.InsurancePolicyView)
    : InsurancePolicyContract.InsurancePolicyListener {

    private var view : InsurancePolicyContract.InsurancePolicyView
    private var interactor: InsuranceInteractor

    init {
        this.view = view
        interactor = InsuranceInteractor(this)
    }
    override fun onSuccess(response: InsuranceDetailResponse) {
        view.hideProgress()
        view.onSuccess(response)
    }

    override fun onFailure(message: String?) {
        view.hideProgress()
        view.showToast(message)
    }

    fun getInsuranceDetails(token : String, request : InsuranceDetailsRequest){
        view.showProgress()
        interactor.getInsurancePolicyDetails(token, request)
    }
}