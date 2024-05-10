package com.epawo.custodian.fragment.airtime.airtime

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutAirtimeRechargeFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.category.response.CategoryResponse
import com.epawo.custodian.utilities.AppPreferences
import com.topwise.cloudpos.aidl.AidlDeviceService

class AirtimeRechargeFragment : BaseFragment(), AirtimeCategoriesContract.AirtimeCategoriesView {

    private var _binding: LayoutAirtimeRechargeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : AirtimeCategoriesPresenter

    private lateinit var token : String
    private lateinit var selectedProvider : String
    private var selectedProviderCode = ""
    private var mtnCode = ""
    private var gloCode = ""
    private var airtelCode = ""
    private var nineMobileCode = ""

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = LayoutAirtimeRechargeFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        token = AppPreferences().getUserToken(mainActivity).toString()
        presenter = AirtimeCategoriesPresenter(this)
        loadCategories()
        setListeners()
        loadProviders()
    }

    private fun loadCategories(){
        presenter.loadCategories(token)
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { navigate(NavigationCommand.Back) }
        binding.continueButton.setOnClickListener { onContinueButtonClick() }
        binding.providerFilledExposedDropdown.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, _, _ ->
                selectedProvider = binding.providerFilledExposedDropdown.text.toString()
                getSelectedProviderCode(selectedProvider)
            }
    }

    private fun loadProviders(){
        val providerList = resources.getStringArray(R.array.provider)
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,providerList)
        binding.providerFilledExposedDropdown.setAdapter(arrayAdapter)
    }

    private fun onContinueButtonClick(){

        val amount = binding.amountField.text.toString().trim()
        val phoneNumber = binding.phoneNumberField.text.toString().trim()
        if( validateControls(phoneNumber, amount, selectedProvider)){
            val bundle = Bundle()
            bundle.putString("amount", amount)
            bundle.putString("phone", phoneNumber)
            bundle.putString("provider", selectedProvider)
            bundle.putString("code", selectedProviderCode)

            navigate(R.id.action_airtimeRechargeFragment_to_airtimeRechargeDetailsFragment,bundle)
        }

    }

    private fun validateControls(phone : String, amount : String, type : String) : Boolean{
        if(TextUtils.isEmpty(phone)){
            toastShort("Please input your phone number")
            return false
        }else if(phone.length < 11 || phone.length > 11){
            toastShort("Please input a valid phone number")
            return false
        }else if(TextUtils.isEmpty(amount)){
            toastShort("Please input recharge amount")
            return false
        }else if(TextUtils.isEmpty(type)){
            toastShort("Please select your provider")
            return false
        }

        return true
    }

    private fun getSelectedProviderCode(type : String){
        when (type) {
            "MTN" -> {
                selectedProviderCode = mtnCode
            }
            "AIRTEL" -> {
                selectedProviderCode = airtelCode
            }
            "GLO" -> {
                selectedProviderCode = gloCode
            }
            "9MOBILE" -> {
                selectedProviderCode = nineMobileCode
            }
        }
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
        binding.progressBar6.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar6.visibility = View.GONE
    }

    override fun onSuccess(response: List<CategoryResponse>) {
        if(response.isNotEmpty()){
            for(item in response){
                if(item.categoryName == "Airtime"){
                    val product = item.products
                    for(i in product){
                        when (i.productName) {
                            "MTN" -> {
                                mtnCode = i.productId.toString()
                            }
                            "GLO" -> {
                                gloCode = i.productId.toString()
                            }
                            "AIRTEL" -> {
                                airtelCode = i.productId.toString()
                            }
                            "9Mobile" -> {
                                nineMobileCode = i.productId.toString()
                            }
                        }
                    }
                }
            }
        }
    }
}