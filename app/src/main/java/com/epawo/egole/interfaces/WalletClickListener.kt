package com.epawo.egole.interfaces

import com.epawo.egole.model.login.response.Wallets

interface WalletClickListener {

    fun onWalletClicked(wallet : Wallets)
}