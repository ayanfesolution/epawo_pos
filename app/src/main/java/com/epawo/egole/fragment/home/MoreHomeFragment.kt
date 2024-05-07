package com.epawo.egole.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutHomeMoreFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.topwise.cloudpos.aidl.AidlDeviceService

class MoreHomeFragment : BaseFragment() {
    private var _binding: LayoutHomeMoreFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutHomeMoreFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setListeners()
    }

    private fun setListeners(){
        binding.createTicketLayout.setOnClickListener { onCreateTicketLayoutClick() }
        binding.ticketListLayout.setOnClickListener { onTicketListClick() }
        binding.airtimeLayout.setOnClickListener { onAirtimeLayoutClick() }
        binding.dataLayout.setOnClickListener { onAirtimeDataLayoutClick() }
        binding.transactionHistoryLayout.setOnClickListener{ onTransactionHistoryLayoutClick() }
        binding.payBillsLayout.setOnClickListener { onPayBillsLayoutClick() }
        binding.cashoutLayout.setOnClickListener { onCashoutLayoutClick() }
        binding.transferLayout.setOnClickListener { onTransferLayoutClick() }
        binding.cardLayout.setOnClickListener { onCardLayoutClick() }
        binding.autoRegLayout.setOnClickListener { onAutoRegLayoutClick() }
        binding.betting.setOnClickListener { onBettingLayoutClick() }
    }

    private fun onCashoutLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToCashoutFragment()))
    }

    private fun onBettingLayoutClick(){
        navigate(NavigationCommand.To(actionMoreHomeFragmentToBettingFragment()))
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

    private fun onAirtimeDataLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToDataRechargeFragment()))
    }

    private fun onAirtimeLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToAirtimeRechargeFragment()))
    }

    private fun onPayBillsLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToBillsFragment()))
    }

    private fun onCreateTicketLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToCreateTicketFragment()))
    }

    private fun onTransactionHistoryLayoutClick(){
        navigate(NavigationCommand.To(actionHomeFragmentToTransactionHistoryFragment()))
    }

    private fun onTicketListClick(){
        navigate(NavigationCommand.To(layoutTicketListFragment()))
    }

    private fun actionHomeFragmentToCreateTicketFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_createTicketFragment)
    }

    private fun layoutTicketListFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_ticketListFragment)
    }

    private fun actionHomeFragmentToAirtimeRechargeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_airtimeRechargeFragment)
    }

    private fun actionHomeFragmentToDataRechargeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_dataRechargeFragment)
    }

    private fun actionHomeFragmentToBillsFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_billsFragment)
    }

    private fun actionHomeFragmentToTransactionHistoryFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_transactionHistoryFragment)
    }

    private fun actionHomeFragmentToFundTransferFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_fundTransferFragment)
    }

    private fun actionHomeFragmentToCardTransferFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_cardTransferFragment)
    }

    private fun actionHomeFragmentToAutoRegFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_autoRegFragment)
    }

    private fun actionHomeFragmentToCashoutFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_cashoutFragment)
    }

    private fun actionMoreHomeFragmentToBettingFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_moreHomeFragment_to_bettingFragment)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}