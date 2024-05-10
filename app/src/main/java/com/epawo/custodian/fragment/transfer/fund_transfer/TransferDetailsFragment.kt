package com.epawo.custodian.fragment.transfer.fund_transfer

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
import com.epawo.custodian.databinding.LayoutTransferDetailsFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.fund_transfer.TransferFundRequest
import com.epawo.custodian.model.fund_transfer.TransferFundResponse
import com.epawo.custodian.utilities.AppPreferences
import com.epawo.custodian.utilities.UrlConstants
import com.epawo.custodian.utilities.Utility
import com.epawo.custodian.utilities.Utility.Companion.getAppBitmap
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.cloudpos.aidl.printer.*
import java.util.LinkedHashMap

class TransferDetailsFragment : BaseFragment(), TransferContract.TransferView {

    private var _binding: LayoutTransferDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var presenter : TransferPresenter
    lateinit var printerDev: AidlPrinter
    lateinit var valueMap: LinkedHashMap<String, String>

    private var amount = ""
    private var accountNumber = ""
    private var accountName = ""
    private var narration = ""
    private var bankCode = ""
    private var bankName = ""
    private var userToken = ""
    private var terminalId = ""
    private var userWalletId = ""
    private var transactionStatus = ""
    private var transactionReference = ""
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
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutTransferDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        presenter = TransferPresenter(this)
        userToken = AppPreferences().getUserToken(mainActivity).toString()
        terminalId = AppPreferences().getTerminalId(mainActivity).toString()
        userWalletId = AppPreferences().getUserId(mainActivity).toString()
        mainWalletNumber = AppPreferences().getWalletAccountNumber(mainActivity).toString()
        extractBundles()
        setControls()
        setListeners()
    }

    private fun extractBundles(){
        val bundle = arguments
        amount = bundle?.getString("amount").toString()
        accountNumber = bundle?.getString("accountNumber").toString()
        accountName = bundle?.getString("accountName").toString()
        narration = bundle?.getString("narration").toString()
        bankCode = bundle?.getString("bankCode").toString()
        bankName = bundle?.getString("bankName").toString()
    }

    private fun setControls(){
        binding.transferAmount.text = Utility.formatCurrency(amount.toDouble())
        binding.accountNumber.text = accountNumber
        binding.accountName.text = accountName
        binding.narration.text = narration
        binding.bankName.text = bankName
    }

    private fun setListeners(){
        binding.transferButton.setOnClickListener { onTransferButtonClick() }
    }

    private fun onTransferButtonClick(){
        showTransactionPinDialog()
    }

    private fun showTransactionPinDialog(){
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
        loginButton.setOnClickListener {

            val agentPin = pinOne.text.toString().trim() + pinTwo.text.toString()
                .trim() + pinThree.text.toString().trim() + pinFour.text.toString().trim()

            val request = TransferFundRequest(userWalletId.toInt(),accountNumber,bankCode,amount.toInt(),
            UrlConstants.INTER,narration, accountName,terminalId, agentPin,mainWalletNumber)
            dialog.dismiss()
            presenter.transferFund(userToken,request)
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
            navigate(NavigationCommand.To(actionTransferDetailsFragmentToHomeFragment()))

        }
        dialog.show()
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

    override fun onTransferSuccessful(response: TransferFundResponse) {
        transactionStatus = response.status
        transactionReference = response.requestid
        showSuccessDialog(amount, response.requestid)
    }

    private fun actionTransferDetailsFragmentToHomeFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_transferDetailsFragment_to_homeFragment)
    }

    private fun getBundles(){
        valueMap = LinkedHashMap()
        valueMap["Terminal ID: "] = AppPreferences().getTerminalId(mainActivity).toString()
        valueMap["Bank Name: "] = bankName
        valueMap["Account Number: "] = accountNumber
        valueMap["Account Name"] = accountName
        valueMap["Narration: "] = narration
        valueMap["Status"] = transactionStatus
        valueMap["Date/Time : "] = Utility.getCurrentDate().toString() + "/" + Utility.getCurrentTime()
        valueMap["Reference: "] = transactionReference
    }

    private fun printReceipt(){

        try {
            val poweredBy = UrlConstants.POWERED_BY
            val businessName = AppPreferences().getCompanyName(mainActivity).toString()
            val businessAddress = AppPreferences().getAddress(mainActivity).toString()
            val agentEmail = AppPreferences().getEmail(mainActivity).toString()
            val phone = AppPreferences().getPhone(mainActivity).toString()
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
            template.add(TextUnit("Fund Transfer", TextUnit.TextSize.LARGE, Align.CENTER).setBold(false))
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
            template.add(TextUnit("***** Powered By $poweredBy *****", TextUnit.TextSize.NORMAL, Align.CENTER).setBold(true))
            template.add(TextUnit("App Version " + BuildConfig.VERSION_NAME, TextUnit.TextSize.NORMAL, Align.CENTER).setBold(true))
            template.add(TextUnit("\n\n\n\n"))
            printerDev.addRuiImage(template.printBitmap, 0)
            printerDev.printRuiQueue(mListen)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }
}