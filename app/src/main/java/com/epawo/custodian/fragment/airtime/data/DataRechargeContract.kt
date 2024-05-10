package com.epawo.custodian.fragment.airtime.data

import com.epawo.custodian.model.airtime.response.AirtimeDataResponse

interface DataRechargeContract {

    interface DataRechargeView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onLoadBundles(response : AirtimeDataResponse)
    }

    interface DataRechargeListener{
        fun onLoadBundles(response : AirtimeDataResponse)
        fun onFailure(message : String?)
    }
}