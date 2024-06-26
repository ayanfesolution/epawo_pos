package com.epawo.custodian.fragment.insurance


import com.epawo.custodian.model.insurance.InsuranceCashoutResponse
import com.epawo.custodian.model.insurance.InsuranceDetailResponse

interface InsurancePolicyContract {

    interface InsurancePolicyView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onSuccess(response : InsuranceDetailResponse)
    }

    interface InsurancePolicyListener{
        fun onSuccess(response : InsuranceDetailResponse)
        fun onFailure(message : String?)
    }

    interface InsuranceCashoutView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onException(message: String?)
        fun onSuccess(response : InsuranceCashoutResponse)
    }

    interface InsuranceCashoutListener{
        fun onSuccess(response : InsuranceCashoutResponse)
        fun onFailure(message : String?)

        fun onException(message : String?)
    }


}