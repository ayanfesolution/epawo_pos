package com.epawo.custodian.fragment.transfer.fund_transfer

import com.epawo.custodian.model.fund_transfer.TransferFundRequest
import com.epawo.custodian.model.fund_transfer.TransferFundResponse

class TransferPresenter(fundTransferView : TransferContract.TransferView) : TransferContract.TransferListener {

    var fundTransferView : TransferContract.TransferView
    var fundTransferInteractor : TransferInteractor

    init {
        this.fundTransferView = fundTransferView
        fundTransferInteractor = TransferInteractor(this)
    }

    override fun onTransferSuccessful(response: TransferFundResponse) {
        fundTransferView.hideProgress()
        if(response.status == "200"){
            fundTransferView.onTransferSuccessful(response)
        }else{
            fundTransferView.showToast(response.message)
        }
    }

    override fun onFailure(message: String?) {
        fundTransferView.hideProgress()
        fundTransferView.showToast(message)
    }

    fun transferFund(token : String, request : TransferFundRequest){
        fundTransferView.showProgress()
        fundTransferInteractor.transfer(token, request)
    }
}