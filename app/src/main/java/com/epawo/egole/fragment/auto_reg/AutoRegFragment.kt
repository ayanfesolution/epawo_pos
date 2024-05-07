package com.epawo.egole.fragment.auto_reg

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutAutoRegFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.model.auto_reg.response.AutoRegStateListResponse
import com.epawo.egole.utilities.AppPreferences
import com.topwise.cloudpos.aidl.AidlDeviceService

class AutoRegFragment : BaseFragment(), AutoRegStateContract.AutoRegStateView {

    private var _binding: LayoutAutoRegFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : AutoRegStatePresenter
    private lateinit var token : String

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutAutoRegFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        initPresenter()
        setListeners()
        loadAutoRegStates()
    }

    private fun initPresenter(){
        token = AppPreferences().getUserToken(mainActivity).toString()
        presenter = AutoRegStatePresenter(this)
    }

    private fun setListeners(){
        _binding?.imageView3?.setOnClickListener{ onBackButtonClick() }
        _binding?.continueButton?.setOnClickListener { onContinueButtonClick() }
    }

    private fun loadAutoRegStates(){
        presenter.loadStates(token)
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    private fun onContinueButtonClick(){
        var referenceNum = _binding?.referenceNumber?.text.toString()
        if(validateControls(referenceNum)){
            presenter.getVehicleDetails(token)
        }
    }

    private fun validateControls(referenceNumber : String) : Boolean{
        if(TextUtils.isEmpty(referenceNumber)){
            toastShort("Please input your reference number")
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
       _binding?.progressBar7?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        _binding?.progressBar7?.visibility = View.GONE
    }

    override fun onSuccess(response: List<AutoRegStateListResponse>) {
        populateStateDropDown(response)
    }

    private fun populateStateDropDown(response: List<AutoRegStateListResponse>){
        val providerName = ArrayList<String>()
        //cableProviderList = response.responseModel
        for (i in response){
            providerName.add(i.stateName)
        }
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerName)
        _binding?.providerFilledExposedDropdown?.setAdapter(arrayAdapter)
    }
}