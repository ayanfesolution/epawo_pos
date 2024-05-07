package com.epawo.egole.fragment.insurance


import com.epawo.egole.model.insurance.InsuranceDetailResponse

interface InsurancePolicyContract {

    interface InsurancePolicyView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response : InsuranceDetailResponse)
//        fun onLoadBundlesSuccess(response : CableProviderPackResponse)
//        fun onLookUpSuccessful(response : CableLookupResponse)
    }

    interface InsurancePolicyListener{
        fun onSuccess(response : InsuranceDetailResponse)
//        fun onLookUpSuccessful(response : CableLookupResponse)
//        fun onLoadBundlesSuccess(response : CableProviderPackResponse)
        fun onFailure(message : String?)
    }
}