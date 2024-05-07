package com.epawo.egole.fragment.insurance

import com.epawo.egole.interfaces.NetworkService
import com.epawo.egole.model.insurance.InsuranceDetailResponse
import com.epawo.egole.model.insurance.InsuranceDetailsRequest
import com.epawo.egole.service.RetrofitInstance
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class InsuranceInteractor(listener: InsurancePolicyContract.InsurancePolicyListener) {
    var listener : InsurancePolicyContract.InsurancePolicyListener

    init{
        this.listener = listener
    }

    fun getInsurancePolicyDetails(token: String, request: InsuranceDetailsRequest){
        loadInsuranceObserver(token, request)
    }


    private fun loadInsuranceObserver(token: String, request: InsuranceDetailsRequest): Observable<InsuranceDetailResponse>{
        return RetrofitInstance.getRetrofitInstance()!!.create(NetworkService::class.java)
            .insuranceDetails(token, request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}