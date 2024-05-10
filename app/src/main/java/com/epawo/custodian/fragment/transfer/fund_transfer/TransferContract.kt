package com.epawo.custodian.fragment.transfer.fund_transfer

import com.epawo.custodian.model.fund_transfer.TransferFundResponse

interface TransferContract {

    interface TransferView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onTransferSuccessful(response : TransferFundResponse)
    }

    interface TransferListener{
        fun onTransferSuccessful(response : TransferFundResponse)
        fun onFailure(message : String?)
    }
}