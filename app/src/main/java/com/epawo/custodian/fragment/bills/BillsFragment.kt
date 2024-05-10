package com.epawo.custodian.fragment.bills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutBillsFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.topwise.cloudpos.aidl.AidlDeviceService

class BillsFragment : BaseFragment() {

    private var _binding: LayoutBillsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutBillsFragmentBinding.inflate(inflater, container, false)
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
        binding.cableTVLayout.setOnClickListener { onCableTvLayoutClick() }
        binding.electricityLayout.setOnClickListener { onElectricityLayoutClick() }
        binding.educationLayout.setOnClickListener { onEducationLayoutClick() }

    }

    private fun onCableTvLayoutClick(){
        navigate(NavigationCommand.To(actionBillsFragmentToCableTvFragment()))
    }

    private fun onElectricityLayoutClick(){
        navigate(NavigationCommand.To(actionBillsFragmentToElectricityFragment()))
    }

    private fun onEducationLayoutClick(){
        navigate(NavigationCommand.To(actionBillsFragmentToWaecPaymentFragment()))
    }


    private fun actionBillsFragmentToCableTvFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_billsFragment_to_cableTvFragment)
    }

    private fun actionBillsFragmentToElectricityFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_billsFragment_to_electricityFragment)
    }

    private fun actionBillsFragmentToWaecPaymentFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_billsFragment_to_waecPaymentFragment)
    }
}