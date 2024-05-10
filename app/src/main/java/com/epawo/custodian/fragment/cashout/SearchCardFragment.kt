package com.epawo.custodian.fragment.cashout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.custodian.databinding.LayoutInsertCardFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.topwise.cloudpos.aidl.AidlDeviceService

class SearchCardFragment : BaseFragment() {

    private var _binding: LayoutInsertCardFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutInsertCardFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}