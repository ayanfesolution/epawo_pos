package com.epawo.custodian.fragment.airtime.data

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
import com.epawo.custodian.BuildConfig
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutDataRechargeDetailsFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.airtime.request.AirtimeDataRequest
import com.epawo.custodian.model.airtime.response.AirtimeDataPaymentResponse
import com.epawo.custodian.utilities.AppPreferences
import com.epawo.custodian.utilities.UrlConstants
import com.epawo.custodian.utilities.Utility
import com.epawo.custodian.utilities.Utility.Companion.getAppBitmap
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.cloudpos.aidl.printer.*
import java.util.LinkedHashMap

class DataRechargeDetailsFragment : BaseFragment(), DataPaymentContract.DataPaymentView {

    private var _binding: LayoutDataRechargeDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var printerDev: AidlPrinter
    lateinit var valueMap: LinkedHashMap<String, String>
    lateinit var presenter : DataPaymentPresenter
    private lateinit var phone : String
    private lateinit var amount : String
    private lateinit var provider : String
    private lateinit var providerCode : String
    private lateinit var productModeID : String
    private lateinit var serviceCode : String
    private lateinit var validity : String
    private lateinit var allowance : String
    private lateinit var userToken : String
    private lateinit var dataBundleCode : String
    private lateinit var userId : String
    private lateinit var transactionMessage : String
    private var transactionReference = ""
    private lateinit var mainWalletNumber : String
    private lateinit var terminalId : String
    private lateinit var type : String
    private lateinit var surauth : String
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

        _binding = LayoutDataRechargeDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = DataPaymentPresenter(this)
        userId = AppPreferences().getUserId(mainActivity).toString()
        userToken = AppPreferences().getUserToken(mainActivity).toString()
        terminalId = AppPreferences().getTerminalId(mainActivity).toString()
        mainWalletNumber = AppPreferences().getWalletAccountNumber(mainActivity).toString()
        extractBundles()
        setControls()
        setListeners()
    }

    private fun extractBundles(){
        val bundle = arguments
        phone = bundle?.getString("phone").toString()
        amount = bundle?.getString("bundlePrice").toString()
        provider = bundle?.getString("provider").toString()
        providerCode = bundle?.getString("providerCode").toString()
        productModeID = bundle?.getString("productModeID").toString()
        surauth = bundle?.getString("surauth").toString()
        type = bundle?.getString("type").toString()
        validity = bundle?.getString("validity").toString()
        dataBundleCode = bundle?.getString("dataBundleCode").toString()
        allowance = bundle?.getString("allowance").toString()
    }

    private fun setControls(){
        binding.transferAmount.text = Utility.formatCurrency(amount.toDouble())
        binding.accountNumber.text = phone
        binding.accountName.text = provider
        binding.validity.text = validity
        binding.dataBundle.text = allowance
    }

    private fun setListeners(){
        binding.imageView3.setOnClickListener { onBackButtonClick() }
        binding.transferButton.setOnClickListener { onContinueButtonClick() }
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
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

            val request = AirtimeDataRequest(type,surauth,phone,amount,dataBundleCode,
                provider,providerCode.toInt(),productModeID.toInt(), userId.toInt(),terminalId, agentPin,mainWalletNumber)
            dialog.dismiss()
            presenter.makePayment(userToken,request)
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

    override fun onPaymentSuccessful(response: AirtimeDataPaymentResponse) {
        transactionMessage = response.message
        transactionReference = response.requestid
        showSuccessDialog(amount, transactionReference)
    }

    private fun getBundles(){
        valueMap = LinkedHashMap()
        valueMap["Terminal ID: "] = AppPreferences().getTerminalId(mainActivity).toString()
        valueMap["Phone Number: "] = phone
        valueMap["Provider : "] = provider
        valueMap["Data Bundle : "] = allowance
        valueMap["Validity : "] = validity
        valueMap["Status"] = transactionMessage
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
            navigate(NavigationCommand.To(actionDataRechargeDetailsFragmentToHomeFragment()))

        }
        dialog.show()
    }

    private fun actionDataRechargeDetailsFragmentToHomeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_dataRechargeDetailsFragment_to_homeFragment)
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
            template.add(TextUnit("Airtime Data Recharge", TextUnit.TextSize.LARGE, Align.CENTER).setBold(false))
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