package com.epawo.custodian.fragment.airtime.airtime

import com.epawo.custodian.model.airtime.response.AirtimeRechargeResponse

interface AirtimeRechargeContract {

    interface AirtimeRechargeView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onRechargeSuccess(response : AirtimeRechargeResponse)
    }

    interface AirtimeRechargeListener{
        fun onRechargeSuccess(response : AirtimeRechargeResponse)
        fun onFailure(message : String?)
    }
}