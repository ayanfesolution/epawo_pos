package com.epawo.custodian.fragment.electricity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutElectricityFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.model.energy.request.ValidateMeterNumberRequest
import com.epawo.custodian.model.energy.response.EnergyProviderResponse
import com.epawo.custodian.model.energy.response.EnergyResponseModel
import com.epawo.custodian.model.energy.response.ValidateMeterNumberResponse
import com.epawo.custodian.utilities.AppPreferences
import com.google.gson.Gson
import com.topwise.cloudpos.aidl.AidlDeviceService

class ElectricityFragment : BaseFragment(), ElectricityProvidersContract.ElectricityProvidersView{

    private var _binding: LayoutElectricityFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : ElectricityProvidersPresenter
    private lateinit var userToken : String
    private lateinit var meterType : String
    private lateinit var energyProvider : String
    private lateinit var customerName : String
    private var providerCodeName = ""
    private lateinit var meterNum : String
    private lateinit var phoneNum : String
    private lateinit var amount : String
    private lateinit var energyList : List<EnergyResponseModel>

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutElectricityFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        setControls()
        setListeners()
        loadEnergyProviders()
    }

    private fun setControls(){
        presenter = ElectricityProvidersPresenter(this)
        userToken = AppPreferences().getUserToken(mainActivity).toString()
        loadProviders()
    }

    private fun loadProviders(){
        val providerList = resources.getStringArray(R.array.meter_type)
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerList)
        binding.meterTypeProviderFilledExposedDropdown.setAdapter(arrayAdapter)
    }

    private fun loadEnergyProviders(){
        toastShort("Loading energy providers...")
        presenter.getEnergyProviders(userToken)
    }

    private fun setListeners(){
        binding.continueButton.setOnClickListener { onButtonClick() }
        binding.providerFilledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                energyProvider = binding.providerFilledExposedDropdown.text.toString()
            }
        binding.meterTypeProviderFilledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                meterType = binding.meterTypeProviderFilledExposedDropdown.text.toString()
                getProviderService(meterType,energyProvider)
            }
    }

    private fun getProviderService(type : String, name : String){
        for(i in energyList){
            if(i.name == name){
                for(j in i.products){
                    providerCodeName = j.productCode
                    break
                    /*if(providerCode.contains(type,true)){
                        providerCodeName = j.name
                        break
                    }*/
                }
            }
        }
    }

    private fun onButtonClick(){
        meterNum = binding.meterNumberField.text.toString()
        phoneNum = binding.phoneNumberField.text.toString()
        amount = binding.amountField.text.toString()
        binding.scrollView.scrollTo(0,0)

        if(validateControls(meterNum, phoneNum, amount, meterType, energyProvider)){
            val request = ValidateMeterNumberRequest(providerCodeName,meterNum, meterType, amount)
            val gson = Gson()
            val requestString = gson.toJson(request)
            presenter.validateMeterNumber(userToken,request)
        }
    }

    private fun validateControls(meterNum : String, phone : String, amount : String, type : String,
    provider : String) : Boolean{
        if(TextUtils.isEmpty(meterNum)){
            toastShort("Please input your meter number")
            return false
        }else if(TextUtils.isEmpty(phone)){
            toastShort("Please input your phone number")
            return false
        }else if(phone.length > 11 || phone.length < 11){
            toastShort("Please input a valid phone number")
            return false
        }else if(TextUtils.isEmpty(amount)){
            toastShort("Please input amount")
            return false
        }else if(TextUtils.isEmpty(type)){
            toastShort("Please select meter type")
            return false
        }else if(TextUtils.isEmpty(provider)){
            toastShort("Please select energy provider")
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
        _binding?.progressBar3?.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        _binding?.progressBar3?.visibility = View.GONE
    }

    override fun onLoadProviderSuccess(response: EnergyProviderResponse) {
        Log.i("RESPONSE", response.toString())
        energyList = response.responseModel
        populateEnergyDropdown(energyList)
    }

    override fun onValidateMeterNumberSuccess(response: ValidateMeterNumberResponse) {
        if(response.isSuccessful){
            customerName = response.responseModel.name
            val bundle = Bundle()
            bundle.putString("meterNumber", meterNum)
            bundle.putString("phoneNumber", phoneNum)
            bundle.putString("customerName", customerName)
            bundle.putString("amount", amount)
            bundle.putString("surauth", response.responseModel.surauth)
            bundle.putString("type", response.responseModel.type)
            bundle.putString("productId", response.productID.toString())
            bundle.putString("meterType", meterType)
            bundle.putString("provider", energyProvider)
            bundle.putString("providerCode", providerCodeName)
            navigate(R.id.action_electricityFragment_to_electricityDetailsFragment, bundle)
        }else{
            toastShort(response.message)
        }
    }

    private fun populateEnergyDropdown(energyList : List<EnergyResponseModel>){
        val providerName = ArrayList<String>()
        for (i in energyList){
            providerName.add(i.name)
        }
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerName)
        binding.providerFilledExposedDropdown.setAdapter(arrayAdapter)
    }
}