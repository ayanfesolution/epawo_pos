package com.epawo.egole.fragment.airtime.airtime

import android.os.Bundle
import android.os.RemoteException
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.egole.BuildConfig
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutAirtimeRechargeDetailsFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.model.airtime.request.AirtimeRechargeRequest
import com.epawo.egole.model.airtime.response.AirtimeRechargeResponse
import com.epawo.egole.utilities.AppPreferences
import com.epawo.egole.utilities.UrlConstants
import com.epawo.egole.utilities.Utility
import com.epawo.egole.utilities.Utility.Companion.getAppBitmap
import com.google.gson.Gson
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.cloudpos.aidl.printer.*
import java.util.LinkedHashMap

class AirtimeRechargeDetailsFragment : BaseFragment(), AirtimeRechargeContract.AirtimeRechargeView {

    private var _binding: LayoutAirtimeRechargeDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var printerDev: AidlPrinter
    lateinit var valueMap: LinkedHashMap<String, String>

    private lateinit var presenter : AirtimeRechargePresenter
    private lateinit var token : String
    private lateinit var userId : String
    private lateinit var terminalId : String
    private lateinit var selectedProvider : String
    private lateinit var selectedProviderCode : String
    private lateinit var transactionStatus : String
    private lateinit var transactionReference : String
    private lateinit var amount : String
    private lateinit var phone : String
    private lateinit var mainWalletNumber : String
    lateinit var dialog: AlertDialog
    lateinit var pinOne: EditText
    lateinit var pinTwo: EditText
    lateinit var pinThree: EditText
    lateinit var pinFour: EditText


    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {
        try {
            printerDev = AidlPrinter.Stub.asInterface(serviceManager!!.printer)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    var mListen: AidlPrinterListener = object : AidlPrinterListener.Stub() {
        @Throws(RemoteException::class)
        override fun onError(i: Int) {
            toastShort("Print error, error code: $i")
        }

        @Throws(RemoteException::class)
        override fun onPrintFinish() {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = LayoutAirtimeRechargeDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = AirtimeRechargePresenter(this)
        extractBundles()
        setControls()
        setListeners()
    }

    private fun setControls(){
        token = AppPreferences().getUserToken(mainActivity).toString()
        userId = AppPreferences().getUserId(mainActivity).toString()
        mainWalletNumber = AppPreferences().getWalletAccountNumber(mainActivity).toString()
        terminalId = AppPreferences().getTerminalId(mainActivity).toString()
        binding.transferAmount.text = Utility.formatCurrency(amount.toDouble())
        binding.accountNumber.text = phone
        binding.accountName.text = selectedProvider
    }

    private fun extractBundles(){
        val bundle = arguments
        amount = bundle?.getString("amount").toString()
        phone = bundle?.getString("phone").toString()
        selectedProvider = bundle?.getString("provider").toString()
        selectedProviderCode = bundle?.getString("code").toString()
    }

    private fun setListeners(){
        binding.transferButton.setOnClickListener { onContinueButtonClick() }
    }

    private fun onContinueButtonClick(){
        showPinDialog("Continue")
    }

    private fun showPinDialog(title : String){
        val alertBuilder = AlertDialog.Builder(
            mainActivity,
            android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen
        )
        val customLayout = mainActivity.layoutInflater.inflate(R.layout.dialog_pin, null)
        alertBuilder.setView(customLayout)
        dialog = alertBuilder.create()

        dialog.setOnKeyListener{ _, keycode, keyEvent ->
            if(keyEvent.action == KeyEvent.ACTION_UP){
                processKeyCode(keycode)
            }
            false
        }

        pinOne = customLayout.findViewById<EditText>(R.id.pinOne)
        pinTwo = customLayout.findViewById<EditText>(R.id.pinTwo)
        pinThree = customLayout.findViewById<EditText>(R.id.pinThree)
        pinFour = customLayout.findViewById<EditText>(R.id.pinFour)

        val loginButton = customLayout.findViewById<Button>(R.id.loginButton)
        loginButton.text = title
        loginButton.setOnClickListener {

            val agentPin = pinOne.text.toString().trim() + pinTwo.text.toString()
                .trim() + pinThree.text.toString().trim() + pinFour.text.toString().trim()

            val request = AirtimeRechargeRequest(phone,amount.toInt(),selectedProviderCode.toInt(),selectedProvider,
                userId.toInt(),terminalId,agentPin, mainWalletNumber)
            dialog.dismiss()
            val gson = Gson()
            val stringRequest = gson.toJson(request)
            presenter.rechargePhone(token,request)
        }

        dialog.show()
    }

    private fun processKeyCode(keyCode : Int){
        when (keyCode) {
            KeyEvent.KEYCODE_0 -> processPinControls("0")
            KeyEvent.KEYCODE_1 -> processPinControls("1")
            KeyEvent.KEYCODE_2 -> processPinControls("2")
            KeyEvent.KEYCODE_3 -> processPinControls("3")
            KeyEvent.KEYCODE_4 -> processPinControls("4")
            KeyEvent.KEYCODE_5 -> processPinControls("5")
            KeyEvent.KEYCODE_6 -> processPinControls("6")
            KeyEvent.KEYCODE_7 -> processPinControls("7")
            KeyEvent.KEYCODE_8 -> processPinControls("8")
            KeyEvent.KEYCODE_9 -> processPinControls("9")
            KeyEvent.KEYCODE_CLEAR -> {}
            KeyEvent.KEYCODE_DEL -> deletePassword()
            KeyEvent.KEYCODE_BACK -> {}
            KeyEvent.KEYCODE_ENTER -> {}
        }
    }

    private fun processPinControls(pin : String){
        if(TextUtils.isEmpty(pinOne.text.toString())){
            pinOne?.setText(pin)
        }else if(TextUtils.isEmpty(pinTwo.text.toString())){
            pinTwo?.setText(pin)
        }else if(TextUtils.isEmpty(pinThree.text.toString())){
            pinThree?.setText(pin)
        }else if(TextUtils.isEmpty(pinFour.text.toString())){
            pinFour.setText(pin)
        }
    }

    private fun deletePassword(){
        if(!TextUtils.isEmpty(pinFour.text.toString())){
            pinFour?.setText("")
        }else if(!TextUtils.isEmpty(pinThree.text.toString())){
            pinThree?.setText("")
        }else if(!TextUtils.isEmpty(pinTwo.text.toString())){
            pinTwo?.setText("")
        }else{
            pinOne?.setText("")
        }
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
         binding.progressBar2.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.progressBar2.visibility = View.GONE
    }

    override fun onRechargeSuccess(response: AirtimeRechargeResponse) {
        transactionStatus = response.status
        transactionReference = response.transId
        showSuccessDialog(amount, response.transId)
    }

    private fun getBundles(){
        valueMap = LinkedHashMap()
        valueMap["Terminal ID: "] = AppPreferences().getTerminalId(mainActivity).toString()
        valueMap["Provider : "] = selectedProvider
        valueMap["Phone Number: "] = phone
        valueMap["Status"] = transactionStatus
        valueMap["Date/Time : "] = Utility.getCurrentDate().toString() + "/" + Utility.getCurrentTime()
        valueMap["Reference: "] = transactionReference
    }

    private fun showSuccessDialog(amount : String, requestId : String){
        val alertBuilder = AlertDialog.Builder(
            mainActivity,
            android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen
        )
        val customLayout = mainActivity.layoutInflater.inflate(R.layout.layout_success_dialog, null)
        alertBuilder.setView(customLayout)
        dialog = alertBuilder.create()
        val transferAmount = customLayout.findViewById<TextView>(R.id.transferAmount)
        transferAmount.text = Utility.formatCurrency(amount.toDouble())

        val transRef = customLayout.findViewById<TextView>(R.id.transRef)
        transRef.text = "REF : $requestId"

        val printButton = customLayout.findViewById<Button>(R.id.printButton)
        printButton.setOnClickListener {
            getBundles()
            printReceipt()
        }

        val closeButton = customLayout.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
            navigate(NavigationCommand.To(actionAirtimeRechargeDetailsFragmentToHomeFragment()))

        }
        dialog.show()
    }

    private fun actionAirtimeRechargeDetailsFragmentToHomeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_airtimeRechargeDetailsFragment_to_homeFragment)
    }

    private fun printReceipt(){

        try {
            val poweredBy = UrlConstants.POWERED_BY
            val businessName = AppPreferences().getBusinessName(mainActivity).toString()
            val businessAddress = AppPreferences().getBusinessAddress(mainActivity).toString()
            val agentEmail = UrlConstants.COMPANY_EMAIL
            val phone = UrlConstants.COMPANY_PHONE
            val bitmap =  getAppBitmap(mainActivity)
            val template = PrintTemplate.getInstance()
            template.init(mainActivity, null)
            template.clear()
            template.add(ImageUnit(bitmap, 384, 125))
            template.add(TextUnit("**** Customer Copy ****", TextUnit.TextSize.NORMAL, Align.CENTER).setBold(true))
            template.add(TextUnit("Merchant Name : $businessName", TextUnit.TextSize.NORMAL, Align.LEFT).setBold(true))
            template.add(TextUnit("Address: $businessAddress", TextUnit.TextSize.NORMAL).setBold(true))
            template.add(TextUnit("Customer Care: $phone", TextUnit.TextSize.NORMAL).setBold(true))
            template.add(TextUnit("Email: $agentEmail", TextUnit.TextSize.NORMAL).setBold(true))
            template.add(TextUnit("................................................................."))
            template.add(TextUnit("Airtime Recharge", TextUnit.TextSize.LARGE, Align.CENTER).setBold(false))
            for ((key, value) in valueMap) {
                template.add(
                    TextUnit(
                        "$key $value",
                        TextUnit.TextSize.NORMAL,
                        Align.LEFT
                    ).setBold(false)
                )
            }
            template.add(TextUnit("Amount " + Utility.formatCurrency((amount.toDouble())) + " .00", TextUnit.TextSize.NORMAL, Align.LEFT).setBold(true))
            template.add(TextUnit(" "))
            template.add(TextUnit("*** $poweredBy ***", TextUnit.TextSize.NORMAL, Align.CENTER).setBold(true))
            template.add(TextUnit("App Version " + BuildConfig.VERSION_NAME, TextUnit.TextSize.NORMAL, Align.CENTER).setBold(true))
            template.add(TextUnit("\n\n\n\n"))
            printerDev.addRuiImage(template.printBitmap, 0)
            printerDev.printRuiQueue(mListen)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }
}