package com.epawo.custodian.interfaces

import com.epawo.custodian.model.login.response.Wallets

interface WalletClickListener {

    fun onWalletClicked(wallet : Wallets)
}