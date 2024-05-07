package com.epawo.egole.fragment.transfer.fund_transfer

import com.epawo.egole.model.fund_transfer.LoadBankListResponse
import com.epawo.egole.model.fund_transfer.ValidateBankRequest
import com.epawo.egole.model.fund_transfer.ValidateBankResponse


class FundTransferPresenter(fundTransferView : FundTransferContract.FundTransferView) : FundTransferContract.FundTransferListener {

    var fundTransferView : FundTransferContract.FundTransferView
    var fundTransferInteractor : FundTransferInteractor

    init {
        this.fundTransferView = fundTransferView
        fundTransferInteractor = FundTransferInteractor(this)
    }


    override fun onBankLoadSuccessful(response : LoadBankListResponse) {
       fundTransferView.hideProgress()
       fundTransferView.onBankLoadSuccessful(response)
    }

    override fun onValidateAccountNumberSuccess(response: ValidateBankResponse) {
        fundTransferView.hideProgress()
        fundTransferView.onValidateAccountNumberSuccess(response)
    }

    override fun onFailure(message: String?) {
        fundTransferView.hideProgress()
        fundTransferView.showToast(message)
    }

    fun loadBankList(token : String){
        fundTransferView.showProgress()
        fundTransferInteractor.loadBank(token)
    }

    fun validateNumber(token : String, request : ValidateBankRequest){
        fundTransferView.showProgress()
        fundTransferInteractor.validateAccountNumber(token,request)
    }
}