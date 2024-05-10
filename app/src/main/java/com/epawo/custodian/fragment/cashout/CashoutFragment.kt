package com.epawo.custodian.fragment.cashout

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.epawo.custodian.R
import com.epawo.custodian.databinding.LayoutCashoutFragmentBinding
import com.epawo.custodian.fragment.BaseFragment
import com.epawo.custodian.fragment.NavigationCommand
import com.epawo.custodian.model.cashout.*
import com.epawo.custodian.utilities.AppPreferences
import com.epawo.custodian.utilities.UrlConstants
import com.epawo.custodian.utilities.Utility
import com.epawo.custodian.utilities.Utility.Companion.playBeepSound
import com.google.gson.Gson
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.library.activity.TopWiseDevice
import com.topwise.library.util.emv.DeviceState
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


class CashoutFragment : BaseFragment(), CashoutContract.CashoutView {

    var amountInputed = ""
    var passwordText = ""
    var sessionId = ""
    var inputPin = ""
    var cardNumber = ""
    var cardExpiry  = ""
    var cardStan = ""
    var cardRRN = ""
    var encryptedPin = ""
    var selectedAccountType = ""
    var apiKey = ""
    var token = ""
    var customerName = ""
    var stan = ""
    var rrn = ""
    lateinit var dialog : AlertDialog
    lateinit var passwordTextView : EditText
    lateinit var pinOne : EditText
    lateinit var pinTwo : EditText
    lateinit var pinThree : EditText
    lateinit var pinFour : EditText
    lateinit var inputAmountPresenter : CashoutPresenter
    lateinit var terminalId : String
    private lateinit var cardModel: CardModel

    private val stringList = mutableListOf<String>()

    private var _binding: LayoutCashoutFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LayoutCashoutFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        Handler().postDelayed({ downloadConfig() }, 3000L)
        terminalId = AppPreferences().getTerminalId(mainActivity).toString()
        setListeners()
        setControls()
    }

    private fun setControls(){
        inputAmountPresenter = CashoutPresenter(this)
        token = AppPreferences().getUserToken(mainActivity).toString()
    }

    private fun setListeners(){
        binding.nextButton.setOnClickListener { onNextButtonClick() }
        binding.imageView3.setOnClickListener{ onBackButtonClick() }
        amountLayoutFocus()
        checkButtonGroupClick()
    }

    private fun onBackButtonClick(){
        navigate(NavigationCommand.Back)
    }

    private fun checkButtonGroupClick(){
        binding.accountGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId ->
            val radioButton = requireActivity().findViewById(checkedId) as RadioButton
            selectedAccountType = selectedAccountType(radioButton.text.toString())
        })
    }

    private fun selectedAccountType(account : String) : String{
        return if(account == "Savings"){
            "10"
        }else{
            "20"
        }
    }

    private fun amountLayoutFocus(){
        binding.amountInput.requestFocus()
    }

    private fun onNextButtonClick(){
        if(Utility.isNetworkAvailable(mainActivity)){
            amountInputed = binding.amountInput.text?.trim().toString()
            if(validateAmountField(amountInputed,selectedAccountType)){
                try{
                    val intAmount = Integer.parseInt(amountInputed) * 100
                    topWiseDevice.startEmv(intAmount.toString())
                }catch (e : NumberFormatException){
                    toastShort("Please input a valid amount")
                }
            }
        }else{
            toastShort(UrlConstants.NO_INTERNET)
        }
    }

    private fun validateAmountField(amount : String, accountType : String) : Boolean{
        //val maximumWithdrawAmount = AppPreferences().geCashoutLimit(mainActivity).toString()
        if(TextUtils.isEmpty(amount)){
            toastShort("Please input your amount")
            return false
        }else if(amount.toInt() < 50){
            toastShort("You can not withdraw less than " + UrlConstants.CURRENCY_NAIRA + "50")
            return false
        }/*else if(amount.toInt() > maximumWithdrawAmount.toInt()){
            toastShort("You can not withdraw more than " + Utility.formatCurrency(maximumWithdrawAmount.toDouble()))
            return false
        }*/else if(TextUtils.isEmpty(accountType)){
            toastShort("Please select account type")
            return false
        }
        return true
    }

    private fun downloadConfig(){
        topWiseDevice.configureTerminal()
    }

    private val topWiseDevice by lazy {
        TopWiseDevice(mainActivity) {
            Log.e("PPPP", Gson().toJson(it))
            when (it.state) {
                DeviceState.INSERT_CARD -> {
                    navigate(NavigationCommand.To(actionCashoutFragmentToSearchCardFragment()))
                }
                DeviceState.PROCESSING -> {

                }
                DeviceState.INPUT_PIN -> {
                    requireActivity().runOnUiThread{
                        navigate(NavigationCommand.Back)
                        displayPinDialog()
                    }
                }
                DeviceState.PIN_DATA -> {
                    requireActivity().runOnUiThread {
                        setCardPin(it.message)
                    }
                }
                DeviceState.INFO -> {
                    Log.i("Transaction Details", it.transactionData.toString())
                    var pinBlock = if(it.transactionData!!.pinBlock.isNullOrEmpty()) "" else it.transactionData!!.pinBlock
                    cardModel = CardModel(it.transactionData!!.pan,
                        it.transactionData!!.cardExpiryDate, it.transactionData!!.sequenceNumber,
                        it.transactionData!!.track2Data, it.transactionData!!.rrn, it.transactionData!!.stan,
                        it.transactionData!!.iccData, it.transactionData!!.acquiringInstitutionalCode,
                        pinBlock!!, it.transactionData!!.acceptorCode, it.transactionData!!.postDataCode,
                        it.transactionData!!.customerName
                    )
                    processTransaction(cardModel!!)
                }
                else -> {
                    Log.e("ERROR", it.message)
                }
            }
        }
    }

    private fun processTransaction(model : CardModel){

        requireActivity().runOnUiThread{
            dialog.dismiss()

            rrn = model.cardRRN
            stan = model.cardStan
            var expiryYear = model.cardExpiry.take(2)
            var expiryMonth = model.cardExpiry.takeLast(2)
            customerName = model.customerName.toString()
            cardNumber = model.cardPan
            cardExpiry = model.cardExpiry
            var cardData = CardDatas(cardNumber,expiryMonth, expiryYear,
                model.cardTrackTwoNumber, model.pinBlock)

            var transactionRequestModel = CashoutRequestModel(
                selectedAccountType.toInt(),
                amountInputed.toInt(),
                cardData,
                model.cardSequenceNumber,
                model.cardIccData,
                0,
                terminalId)

            //"2058HGP7"
            val gson = Gson()
            val stringRequest = gson.toJson(transactionRequestModel)
            /*val key = AppConstants.HEADER_KEY // 128 bit key
            val initVector = AppConstants.INIT_VECTOR.substring(0,16) // 16 bytes IV
            val request = AESEncryptor.encrypt(key,initVector,stringRequest).toString()
            var requestData = CashoutRequest(request)*/
            inputAmountPresenter.cashOut(token,transactionRequestModel)
        }
    }

    private fun displayPinDialog(){
        val alertBuilder = AlertDialog.Builder(requireActivity())
        val customLayout = requireActivity().layoutInflater.inflate(R.layout.cashout_pin, null)
        alertBuilder.setView(customLayout)
        dialog = alertBuilder.create()

        val amountDialog = customLayout.findViewById<TextView>(R.id.dialog_amount)
        pinOne = customLayout.findViewById<EditText>(R.id.pinOne)
        pinTwo = customLayout.findViewById<EditText>(R.id.pinTwo)
        pinThree = customLayout.findViewById<EditText>(R.id.pinThree)
        pinFour = customLayout.findViewById<EditText>(R.id.pinFour)

        amountDialog.text = UrlConstants.CURRENCY_NAIRA + amountInputed
        dialog.show()
        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun setCardPin(pin :String){
        //this condition checks that the user have deleted on oh the password
        if(pin.length < stringList.size ){
            stringList.removeLast()
            when (pin.length) {
                3 -> {
                    pinFour?.setText("")
                }
                2 -> {
                    pinThree?.setText("")
                }
                1 -> {
                    pinTwo?.setText("")
                }
                else -> {
                    pinOne?.setText("")
                }
            }
        }else{
            if(pin.length > 1){
                stringList.add(getLastPinItem(pin))
            }else{
                stringList.add(pin)
            }

            if(stringList.size == pin.length){
                if(TextUtils.isEmpty(pinOne?.text.toString())){
                    pinOne?.setText(pin)
                    playBeepSound(mainActivity)
                }else if(TextUtils.isEmpty(pinTwo?.text.toString())){
                    pinTwo?.setText(getLastPinItem(pin))
                    playBeepSound(mainActivity)
                }else if(TextUtils.isEmpty(pinThree?.text.toString())){
                    pinThree?.setText(getLastPinItem(pin))
                    playBeepSound(mainActivity)
                }else{
                    pinFour?.setText(getLastPinItem(pin))
                    playBeepSound(mainActivity)
                }
            }
        }
    }

    private fun getLastPinItem(pin : String) : String{
        var lastItem = ""
        if(!TextUtils.isEmpty(pin)){
            lastItem = pin[pin.length - 1].toString()
        }

        return lastItem
    }

    private fun getEncryptPinBlock(pinBLock : String) : String{
        if(!TextUtils.isEmpty(pinBLock)){
            try {
                encryptedPin = encryptCardPinBlock(pinBLock, sessionId)
                return encryptedPin
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        return ""
    }

    private fun encryptCardPinBlock(input: String, key: String): String {
        var crypted: ByteArray? = null
        try {
            val skey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skey)
            crypted = cipher.doFinal(input.toByteArray())
        } catch (e: java.lang.Exception) {
            println(e.toString())
        }
        return Base64.encodeToString(crypted, Base64.DEFAULT)
    }

    private fun actionCashoutFragmentToSearchCardFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_cashoutFragment_to_searchCardFragment)
    }

    override fun showToast(message: String?) {
        toastShort(message)
    }

    override fun showProgress() {
        binding.scrollBar.scrollTo(0,0)
        binding.loadingBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        binding.loadingBar.visibility = View.GONE
    }

    override fun cashoutFailed(response: String?) {
        val amount = amountInputed
        val pan = ""
        val status = ""
        val responseCode = ""
        val description = ""
        val ref = ""
        getTransactionDetails(responseCode,pan,amount,status,description,"","","")
    }

    override fun cashoutSuccessFul(cashout: CashoutResponseModel) {
        val amount = amountInputed
        val pan = Utility.maskCardPan(cardNumber)
        val status = "Transaction Successful"
        val responseCode = cashout.content.statusCode
        val description = cashout.content.responseMessage.toString()
        val terminalID = cashout.content.terminalID
        val ref = cashout.content.referenceNumber

        getTransactionDetails(responseCode,pan,amount,status,description, terminalID,customerName,ref)
    }

    override fun onExpiredSessionId(message: String?) {
        toastShort(message)
        navigate(NavigationCommand.To(actionCashoutFragmentToLoginFragment()))
    }

    override fun onException(message: String?) {
        toastShort(message)
    }

    private fun getTransactionDetails(responseCode : String, pan : String, amount : String, status : String, description : String,
    terminalID : String, customerName : String, reference : String){
        val currentDate = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
            .format(Date())

        var bundle = Bundle()
        bundle.putString("TerminalId", terminalID)
        bundle.putString("cardNo", pan)
        bundle.putString("cardType",getCardType(cardNumber))
        bundle.putString("expiry", cardExpiry)
        bundle.putString("status", status)
        bundle.putString("rrn",rrn)
        bundle.putString("stan", stan)
        bundle.putString("amount", amount)
        bundle.putString("customerName", customerName)
        bundle.putString("accountType", selectedAccountType)
        bundle.putString("responseCode", responseCode)
        bundle.putString("description", description)
        bundle.putString("ref", reference)
        bundle.putString("date", currentDate)

        navigate(R.id.action_cashoutFragment_to_printFragment, bundle)
    }

    private fun getCardType(cardValue: String): String? {
        return if ((cardValue[0] == '5') and (cardValue.length == 16)) {
            getString(R.string.mastercard)
        } else if ((cardValue[0] == '4') and (cardValue.length <= 16)) {
            getString(R.string.visacard)
        } else if (cardValue.length > 16) {
            getString(R.string.vervecard)
        } else {
            getString(R.string.debit_card)
        }
    }

    private fun actionCashoutFragmentToLoginFragment() : NavDirections{
        return ActionOnlyNavDirections(R.id.action_cashoutFragment_to_loginFragment)
    }
}