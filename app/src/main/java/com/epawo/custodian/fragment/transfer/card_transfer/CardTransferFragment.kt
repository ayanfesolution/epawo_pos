package com.epawo.custodian.fragment.transfer.card_transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.custodian.databinding.LayoutCardTransferFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.topwise.cloudpos.aidl.AidlDeviceService

class CardTransferFragment : BaseFragment() {

    private var _binding: LayoutCardTransferFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutCardTransferFragmentBinding.inflate(inflater, container, false)
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
        binding.imageView3.setOnClickListener { onBackButtonClicked() }
    }

    private fun onBackButtonClicked(){
        navigate(NavigationCommand.Back)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}