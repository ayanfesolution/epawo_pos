package com.epawo.custodian.utilities

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.util.Patterns
import android.widget.Toast
import com.epawo.custodian.R
import java.math.RoundingMode
import java.security.SecureRandom
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utility {

    companion object{
        var pbar: ProgressDialog? = null
        const val AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        var rnd = SecureRandom()


        fun shortToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun longToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun getCurrentDate(): String? {
            val c = Calendar.getInstance().time
            val simpleDateFormat =
                SimpleDateFormat("dd/MM/yyyy")
            return simpleDateFormat.format(c)
        }

        fun getPresentDates(): String {
            val c = Calendar.getInstance().time
            val simpleDateFormat =
                SimpleDateFormat("yyyy-MM-dd")
            return simpleDateFormat.format(c)
        }

        fun getOneMonthDate() : String{
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH, -1)
            val result = cal.time
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            return simpleDateFormat.format(result)
        }

        fun getCurrentTime(): String? {
            val df: DateFormat = SimpleDateFormat("HH:mm") // Format time
            return df.format(Calendar.getInstance().time)
        }

        fun roundOffDecimal(number: Double): Double {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        fun createRandomNumber(len: Long): String {
            check(len <= 18) { "To many digits" }
            val tLen = Math.pow(10.0, (len - 1).toDouble()).toLong() * 9
            val number =
                (Math.random() * tLen).toLong() + Math.pow(10.0, (len - 1).toDouble()).toLong() * 1
            val tVal = number.toString() + ""
            check(tVal.length.toLong() == len) { "The random number '$tVal' is not '$len' digits" }
            return tVal
        }

        fun formatData(cardDate: String): String? {
            var formatDate = cardDate
            if (formatDate.length == 6) {
                formatDate = formatDate.substring(0, formatDate.length - 2)
            }
            return formatDate
        }

        fun formatCurrency(price: Double): String? {
            return UrlConstants.CURRENCY_NAIRA + formatCurrencyWithoutSymbol(price)
        }

        private fun formatCurrencyWithoutSymbol(price: Double): String {
            val formattedPrice: Double
            var nairaPrice: String
            try {
                val decimalFormat = DecimalFormat()
                decimalFormat.isDecimalSeparatorAlwaysShown = false
                formattedPrice = if (price - price.toInt() != 0.0) {
                    price
                } else {
                    price.toInt().toDouble()
                }
                nairaPrice = decimalFormat.format(formattedPrice)
            } catch (e: Exception) {
                nairaPrice = "0.00"
            }
            return nairaPrice
        }

        fun randomNumber(len: Int): String? {
            val AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
            val rnd = SecureRandom()
            val sb = StringBuilder(len)
            for (i in 0 until len) sb.append(AB[rnd.nextInt(AB.length)])
            return sb.toString()
        }

        fun getPresentDate(): String? {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val cal = Calendar.getInstance()
            return dateFormat.format(cal.time)
        }

        fun capitalize(value: String): String? {
            var upper = value
            upper = if (value.length > 0) {
                value.substring(0, 1).uppercase(Locale.getDefault()) + value.substring(1)
            } else {
                value
            }
            return upper
        }

        fun randomAlphaNumericNumber(length: Int): String? {
            val sb = StringBuilder(length)
            for (i in 0 until length) sb.append(AB[rnd.nextInt(AB.length)])
            return sb.toString()
        }

        @Throws(Exception::class)
        fun maskString(strText: String, start: Int, end: Int, maskChar: Char): String {
            var start = start
            var end = end
            if (strText == null || strText == "") return ""
            if (start < 0) start = 0
            if (end > strText.length) end = strText.length
            if (start > end) throw Exception("End index cannot be greater than start index")
            val maskLength = end - start
            if (maskLength == 0) return strText
            val sbMaskString = StringBuilder(maskLength)
            for (i in 0 until maskLength) {
                sbMaskString.append(maskChar)
            }
            return strText.substring(
                0,
                start
            ) + sbMaskString + strText.substring(start + maskLength)
        }

        fun maskCardPan(pan: String): String {
            val STARTLENGTH = 6 //first digit of card you don't want to mask
            val ENDLENGTH = 4 //last digit of card you don't want to mask

            val maskedLength = pan.length - (STARTLENGTH + ENDLENGTH)
            println(maskedLength)
            val sb = java.lang.StringBuilder()
            for (i in 0 until maskedLength) {
                sb.append("*")
            }
            return pan.substring(0, STARTLENGTH) + sb + pan.substring(pan.length - ENDLENGTH, pan.length)
        }

        fun getTimeFormat(time: String?): Map<String, String>? {
            val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timeFormat: DateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val dateMap: MutableMap<String, String> = HashMap()
            var date: Date? = Date()
            try {
                date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(time)
            } catch (e: Exception) {
                //e.printStackTrace();
            }
            assert(date != null)
            dateMap["date"] = dateFormat.format(date)
            dateMap["time"] = timeFormat.format(date)
            return dateMap
        }

        fun isValidEmail(email : String) : Boolean{
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }


        fun convertDateToUTCDate(date: String): String {
            val dateFormats = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timeFormats = SimpleDateFormat("h:mm a", Locale.getDefault())
            var dates = Date()
            var convertedDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            try{
                convertedDate = dateFormat.parse(date)
                // dates = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(convertedDate)
            } catch (e: ParseException) {
                ""
            }

            return dateFormats.format(convertedDate) + " - " + timeFormats.format(convertedDate)
        }

        fun convertDateToUTCDates(date: String): String {
            val dateFormats = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val timeFormats = SimpleDateFormat("h:mm a", Locale.getDefault())
            var dates = Date()
            var convertedDate = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("GMT")
            try{
                convertedDate = dateFormat.parse(date)
                // dates = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(convertedDate)
            } catch (e: ParseException) {
                ""
            }

            return dateFormats.format(convertedDate)
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager  =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetworkInfo = connectivityManager?.getActiveNetworkInfo()
            return activeNetworkInfo != null && activeNetworkInfo.isConnected()
        }

        fun playBeepSound(context : Context){
            val mp = MediaPlayer.create(context, R.raw.beep)
            mp.start()
        }

        fun getAppBitmap(context : Context) : Bitmap {
            var bitmap : Bitmap? = null
            bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.epawo_logo)
            return bitmap
        }
    }
}