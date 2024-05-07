package com.epawo.egole.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.RecyclerView
import com.epawo.egole.R
import com.epawo.egole.adapter.WalletsAdapter
import com.epawo.egole.databinding.LayoutHomeFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.fragment.airtime.airtime.AirtimeCategoriesPresenter
import com.epawo.egole.interfaces.WalletClickListener
import com.epawo.egole.model.login.response.Wallets
import com.epawo.egole.model.wallet_balance.response.WalletBalanceResponse
import com.epawo.egole.utilities.AppPreferences
import com.epawo.egole.utilities.Utility
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.topwise.cloudpos.aidl.AidlDeviceService
import java.lang.reflect.Type


class HomeFragment : BaseFragment(), WalletClickListener, HomeContract.HomeView {

    private var _binding: LayoutHomeFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var dialog: AlertDialog
    lateinit var walletList : List<Wallets>
    private lateinit var presenter : HomePresenter

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = HomePresenter(this)
        setListeners()
        loadWalletsList()
        setControls()
    }

    private fun setListeners(){
        binding.cashoutLayout.setOnClickListener { onCashoutLayoutClick() }
        binding.transferLayout.setOnClickListener { onTransferLayoutClick() }
        binding.cardLayout.setOnClickListener { onCardLayoutClick() }
//        binding.autoRegLayout.setOnClickListener { onAutoRegLayoutClick() }
//        binding.more.setOnClickListener{ onMoreButtonClicked() }
        binding.balanceLayer.refreshBalance.setOnClickListener { onRefreshBalanceClicked() }
        binding.balanceLayer.profileImage.setOnClickListener { onProfileImageClicked() }
        binding.balanceLayer.constraintLayout2.setOnClickListener { onWalletLayerClicked() }
    }

    private fun setControls(){
        binding.balanceLayer.companyName.text = AppPreferences().getCompanyName(mainActivity).toString()
        val walletBalance = AppPreferences().getWalletBalance(mainActivity).toString()
        binding.balanceLayer.walletBalance.text = Utility.formatCurrency(walletBalance.toDouble())
        binding.balanceLayer.companyName.text = AppPreferences().getCompanyName(mainActivity)
    }

    private fun loadWalletsList(){
        val storedWalletList = AppPreferences().getWalletList(mainActivity)
        val type: Type = object : TypeToken<List<Wallets?>?>() {}.type
        walletList = Gson().fromJson(storedWalletList, type)

        binding.balanceLayer.walletName.text = walletList[0].bankName
        binding.balanceLayer.accountNumber.text = walletList[0].accountNumber

    }

    private fun onRefreshBalanceClicked(){
        val token = AppPreferences().getUserToken(mainActivity).toString()
        val mainAccount = AppPreferences().getWalletAccountNumber(mainActivity).toString()
        presenter.loadWalletBalance(token, mainAccount)
    }

    private fun onWalletLayerClicked(){
        val alertBuilder = AlertDialog.Builder(mainActivity)
        val customLayout = mainActivity.layoutInflater.inflate(R.layout.layout_wallet_list, null)
        alertBuilder.setView(customLayout)
        dialog = alertBuilder.create()

        val walletLists = customLayout.findViewById<RecyclerView>(R.id.walletList)
        var walletAdapter = WalletsAdapter(this, walletList)
        walletLists.adapter = walletAdapter
        dialog.show()
    }

    private fun onProfileImageClicked(){
        navigate(NavigationCommand.To(actionHomeFragmentToProfileFragment()))
    }

    private fun onMoreButtonClicked(){
        navigate(NavigationCommand.To(actionHomeFragmentToMoreHomeFragment()))
    }

    private fun onCashoutLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToCashoutFragment()))
    }

    private fun onAutoRegLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToAutoRegFragment()))
    }

    private fun onCardLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToCardTransferFragment()))
    }

    private fun onTransferLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToFundTransferFragment()))
    }

    private fun actionHomeFragmentToProfileFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_homeFragment_to_profileFragment)
    }

    private fun actionHomeFragmentToFundTransferFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_homeFragment_to_fundTransferFragment)
    }

    private fun actionHomeFragmentToCardTransferFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_transactionHistoryFragment)
    }

    private fun actionHomeFragmentToAutoRegFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_homeFragment_to_autoRegFragment)
    }

    private fun actionHomeFragmentToMoreHomeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_homeFragment_to_moreHomeFragment)
    }

    private fun actionHomeFragmentToCashoutFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_homeFragment_to_cashoutFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onWalletClicked(wallet: Wallets) {
       dialog.dismiss()
        binding.balanceLayer.walletName.text = wallet.bankName
        binding.balanceLayer.accountNumber.text = wallet.accountNumber
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun onSuccess(response: WalletBalanceResponse) {
        val balance = response.walletBalance
        AppPreferences().setWalletBalance(mainActivity, balance.toString())
        binding.balanceLayer.walletBalance.text = Utility.formatCurrency(balance)
        toastShort("Wallet balance updated...")

    }
}