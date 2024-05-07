package com.epawo.egole.fragment.transfer.fund_transfer

import com.epawo.egole.model.fund_transfer.LoadBankListResponse
import com.epawo.egole.model.fund_transfer.ValidateBankResponse

interface FundTransferContract {

    interface FundTransferView{
        fun showToast(message : String?)
        fun showProgress()
        fun hideProgress()
        fun onBankLoadSuccessful(response : LoadBankListResponse)
        fun onValidateAccountNumberSuccess(response : ValidateBankResponse)
    }

    interface FundTransferListener{
        fun onBankLoadSuccessful(response : LoadBankListResponse)
        fun onValidateAccountNumberSuccess(response : ValidateBankResponse)
        fun onFailure(message : String?)
    }
}