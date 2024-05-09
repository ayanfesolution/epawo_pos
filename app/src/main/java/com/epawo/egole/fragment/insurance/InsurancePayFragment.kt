package com.epawo.egole.fragment.insurance

import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
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
import com.epawo.egole.R
import com.epawo.egole.databinding.LayoutCashoutFragmentBinding
import com.epawo.egole.databinding.LayoutInsuranceFragmentBinding
import com.epawo.egole.databinding.LayoutInsurancepayFragmentBinding
import com.epawo.egole.fragment.BaseFragment
import com.epawo.egole.fragment.NavigationCommand
import com.epawo.egole.fragment.cashout.CashoutPresenter
import com.epawo.egole.model.cashout.CardDatas
import com.epawo.egole.model.cashout.CardModel
import com.epawo.egole.model.cashout.CashoutRequestModel
import com.epawo.egole.model.insurance.CardData
import com.epawo.egole.model.insurance.InsuranceCashoutRequest
import com.epawo.egole.model.insurance.InsuranceCashoutResponse
import com.epawo.egole.model.insurance.InsuranceDetailResponse
import com.epawo.egole.utilities.AppPreferences
import com.epawo.egole.utilities.UrlConstants
import com.epawo.egole.utilities.Utility
import com.google.gson.Gson
import com.topwise.cloudpos.aidl.AidlDeviceService
import com.topwise.library.activity.TopWiseDevice
import com.topwise.library.util.emv.DeviceState
import java.text.SimpleDateFormat
import java.util.*

class InsurancePayFragment: BaseFragment(), InsurancePolicyContract.InsuranceCashoutView {

    var amountInputed = ""
    var passwordText = ""
    var sessionId = ""
    var inputPin = ""
    var cardNumber = ""
    var cardExpiry  = ""
    var cardStan = ""
    var cardRRN = ""
    var encryptedPin = ""
    var token = ""
    var customerName = ""
    var stan = ""
    var rrn = ""
    lateinit var terminalId : String
    private lateinit var cardModel: CardModel

    var selectedAccountType = ""
    lateinit var dialog : AlertDialog
    lateinit var passwordTextView : EditText
    lateinit var pinOne : EditText
    lateinit var pinTwo : EditText
    lateinit var pinThree : EditText
    lateinit var pinFour : EditText
    lateinit var presenter: InsuranceCashoutPresenter

    lateinit var amount: String
    lateinit var bizName: String
    lateinit var email:String
    lateinit var name: String
    lateinit var policyNumber:String
    lateinit var walletAccount: String

    lateinit var phone: String
    lateinit var desc: String

    private var _binding: LayoutInsurancepayFragmentBinding? = null
    private val binding get() = _binding!!

    private val stringList = mutableListOf<String>()

    override fun onDeviceConnected(serviceManager: AidlDeviceService?) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =LayoutInsurancepayFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        extractBundles()
        Handler().postDelayed({ downloadConfig() }, 3000L)
        terminalId = AppPreferences().getTerminalId(mainActivity).toString()
        setListeners()
        setControls()
    }

    private fun setControls(){
        presenter = InsuranceCashoutPresenter(this)
        token = AppPreferences().getUserToken(mainActivity).toString()
        walletAccount = AppPreferences().getWalletAccountNumber(mainActivity).toString()
    }

    private fun setListeners(){
        binding.nextButton.setOnClickListener { onNextButtonClick() }
       // binding.imageView3.setOnClickListener{ onBackButtonClick() }
        checkButtonGroupClick()
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

    private fun extractBundles(){
        val bundle = arguments
        amount = bundle?.getString("amount").toString()
        name = bundle?.getString("insuredName").toString()
        email = bundle?.getString("insuredEmail").toString()
        bizName = bundle?.getString("bizUnit").toString()
        policyNumber = bundle?.getString("policyNumber").toString()
        phone = bundle?.getString("phone").toString()
        desc = bundle?.getString("desc").toString()
    }

    private fun onNextButtonClick(){
        if(Utility.isNetworkAvailable(mainActivity)){
            try{
                val intAmount = Integer.parseInt(amount) * 100
                topWiseDevice.startEmv(intAmount.toString())
            }catch (e : NumberFormatException){
                toastShort("Please input a valid amount")
            }
        }else{
            toastShort(UrlConstants.NO_INTERNET)
        }
    }

    private fun downloadConfig(){
        topWiseDevice.configureTerminal()
    }

    private val topWiseDevice by lazy {
        TopWiseDevice(mainActivity) {
            Log.e("PPPP", Gson().toJson(it))
            when (it.state) {
                DeviceState.INSERT_CARD -> {
                    navigate(NavigationCommand.To(actionInsuranceCashoutFragmentToSearchCardFragment()))
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

    private fun actionInsuranceCashoutFragmentToSearchCardFragment() : NavDirections {
        return ActionOnlyNavDirections(R.id.action_insuranceCashoutFragment_to_searchCardFragment)
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
            var cardData = CardData(cardNumber,expiryMonth, expiryYear,
                model.cardTrackTwoNumber, model.pinBlock)
            val request = InsuranceCashoutRequest(selectedAccountType.toInt(),
            bizName,cardData, model.cardSequenceNumber,desc,email, model.cardIccData, name, "Custodian Premium Payment",
                phone, model.pinBlock, policyNumber, 0, amount.toInt(),terminalId, walletAccount)

//            var transactionRequestModel = CashoutRequestModel(
//                selectedAccountType.toInt(),
//                amountInputed.toInt(),
//                cardData,
//                model.cardSequenceNumber,
//                model.cardIccData,
//                0,
//                terminalId)

            //"2058HGP7"
            val gson = Gson()
          //  val stringRequest = gson.toJson(transactionRequestModel)
            /*val key = AppConstants.HEADER_KEY // 128 bit key
            val initVector = AppConstants.INIT_VECTOR.substring(0,16) // 16 bytes IV
            val request = AESEncryptor.encrypt(key,initVector,stringRequest).toString()
            var requestData = CashoutRequest(request)*/
            presenter.insuranceCashout(token,request)
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
                    Utility.playBeepSound(mainActivity)
                }else if(TextUtils.isEmpty(pinTwo?.text.toString())){
                    pinTwo?.setText(getLastPinItem(pin))
                    Utility.playBeepSound(mainActivity)
                }else if(TextUtils.isEmpty(pinThree?.text.toString())){
                    pinThree?.setText(getLastPinItem(pin))
                    Utility.playBeepSound(mainActivity)
                }else{
                    pinFour?.setText(getLastPinItem(pin))
                    Utility.playBeepSound(mainActivity)
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

        navigate(R.id.action_insuranceCashout_to_printFragment, bundle)
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

    override fun onSuccess(response: InsuranceCashoutResponse) {
        TODO("Not yet implemented")
    }

}