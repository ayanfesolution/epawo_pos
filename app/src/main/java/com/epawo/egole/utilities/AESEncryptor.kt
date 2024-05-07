package com.epawo.egole.utilities

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESEncryptor {

    fun encrypt(key: String, initVector: String, value: String): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            val iv = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)
            val encrypted = cipher.doFinal(value.toByteArray())
            return android.util.Base64.encodeToString(encrypted, android.util.Base64.NO_WRAP)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }

    fun decrypt(key: String, initVector: String, encrypted: String?): String? {
        try {
            val iv = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
            val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)

            val original = cipher.doFinal(Base64.getDecoder().decode(encrypted))

            return String(original)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return null
    }
}