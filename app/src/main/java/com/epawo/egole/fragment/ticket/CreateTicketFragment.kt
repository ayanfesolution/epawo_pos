package com.epawo.egole.fragment.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epawo.egole.databinding.LayoutCreateTicketFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.topwise.cloudpos.aidl.AidlDeviceService

class CreateTicketFragment : BaseFragment() {

    private var _binding: LayoutCreateTicketFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LayoutCreateTicketFragmentBinding.inflate(inflater, container, false)
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}