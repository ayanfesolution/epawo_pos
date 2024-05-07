package com.epawo.egole.fragment.transfer.fund_transfer

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutFundTransferFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.model.fund_transfer.BankList
import com.epawo.egole.model.fund_transfer.LoadBankListResponse
import com.epawo.egole.model.fund_transfer.ValidateBankRequest
import com.epawo.egole.model.fund_transfer.ValidateBankResponse
import com.epawo.egole.utilities.AppPreferences
import com.topwise.cloudpos.aidl.AidlDeviceService


class FundTransferFragment : BaseFragment(), FundTransferContract.FundTransferView {

    private var _binding: LayoutFundTransferFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : FundTransferPresenter
    private lateinit var bankList : List<BankList>
    private var selectedBank = ""
    private var selectedBankCode = ""
    private var userToken = ""

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutFundTransferFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = FundTransferPresenter(this)
        userToken = AppPreferences().getUserToken(mainActivity).toString()
        loadBanks()
        setListeners()
    }

    private fun setListeners(){
        binding.accountNumberField.addTextChangedListener(textWatcher)
        binding.filledExposedDropdown.onItemClickListener =
            OnItemClickListener { _, _, _, _ ->
                selectedBank = binding.filledExposedDropdown.text.toString()
                getSelectedBankCode(selectedBank)
            }
        binding.continueButton.setOnClickListener { onContinueButtonClick() }
        binding.imageView3.setOnClickListener{ onBackButtonClick() }
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    private fun onContinueButtonClick(){
        val amount = binding.amountField.text.toString().trim()
        val accountNumber = binding.accountNumberField.text.toString().trim()
        val accountName = binding.accountNameField.text.toString().trim()
        val narration = binding.narrationField.text.toString().trim()
        if(validateControls(amount, selectedBankCode, accountNumber)){
            val bundle = Bundle()
            bundle.putString("amount", amount)
            bundle.putString("accountNumber", accountNumber)
            bundle.putString("accountName", accountName)
            bundle.putSerializable("narration", narration)
            bundle.putString("bankCode", selectedBankCode)
            bundle.putString("bankName", selectedBank)
            navigate(R.id.action_fundTransferFragment_to_transferDetailsFragment, bundle)
        }
    }

    private fun validateControls(amount : String,bankCode : String, accountNumber : String) : Boolean{
        if(TextUtils.isEmpty(amount)){
            toastShort(getString(R.string.error_transfer_amount))
            return false
        }else if(TextUtils.isEmpty(bankCode)){
            toastShort(getString(R.string.error_select_bank))
            return false
        }else if(TextUtils.isEmpty(accountNumber)){
            toastShort(getString(R.string.error_account_number))
            return false
        }else if(accountNumber.length < 10 || accountNumber.length > 10){
            toastShort(getString(R.string.error_invalid_account_number))
            return false
        }

        return true
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            // get the content of both the edit text
            val accountNumber = binding.accountNumberField.text.toString()
            if(start == 9){
                val request = ValidateBankRequest(selectedBankCode, accountNumber)
                presenter.validateNumber(userToken,request)
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }



    private fun loadBanks(){
        toastShort("Loading bank list...")
        presenter.loadBankList(userToken)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onBankLoadSuccessful(response: LoadBankListResponse) {
       Log.i("RESPONSE", response.toString())
       bankList = response.bankslist
       populateBankListDropdown(bankList)
    }

    override fun onValidateAccountNumberSuccess(response: ValidateBankResponse) {
        binding.accountNameField.setText(response.data.name)
    }

    private fun populateBankListDropdown(bankList : List<BankList>){
        val bankNames = ArrayList<String>()
        for (i in bankList){
            bankNames.add(i.bankName)
        }
        val arrayAdapter = ArrayAdapter(mainActivity, R.layout.dropdown_menu,bankNames)
        binding.filledExposedDropdown.setAdapter(arrayAdapter)
    }

    private fun getSelectedBankCode(bankName : String) : String{
        for(i in bankList){
            if(i.bankName == bankName){
                selectedBankCode = i.bankCode
                break
            }
        }

        return selectedBankCode
    }
}