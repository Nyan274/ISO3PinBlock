package com.nyan.iso3pinblock.views

import org.apache.commons.codec.DecoderException
import org.apache.commons.codec.binary.Hex
import org.apache.commons.lang3.StringUtils
import java.util.*
import kotlin.experimental.xor

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    override fun encodeISO3(pin: String, pan: String) {
        encodeFormat3(pin, pan)?.let { view.showResult(it) }
    }

    private fun encodeFormat3(pin: String, pan: String): String? {
        return try {
            val pinLenHead = StringUtils.leftPad(
                Integer.toHexString(pin.length), 2, '3'
            ).toString() + pin
            val pinData = StringUtils.rightPad(pinLenHead, 16, getRandomHexString(16))
            val bPin = Hex.decodeHex(pinData.toCharArray())
            val panPart = extractPanAccountNumberPart(pan)
            val panData = StringUtils.leftPad(panPart, 16, '0')
            val bPan = Hex.decodeHex(panData.toCharArray())
            val pinBlock = ByteArray(8)
            for (i in 0..7) pinBlock[i] = (bPin[i] xor bPan[i]) as Byte
            Hex.encodeHexString(pinBlock)
        } catch (e: DecoderException) {
            throw RuntimeException("Hex decoder failed!", e)
        }
    }

    private fun getRandomHexString(numChars: Int): String? {
        val sb = StringBuffer()
        while (sb.length < numChars) {
            sb.append(Integer.toHexString(getRandomInt(10, 15)))
        }
        return sb.toString().substring(0, numChars)
    }

    private fun getRandomInt(min: Int, max: Int): Int {
        val r = Random()
        return min + r.nextInt(max - min + 1)
    }

    /**
     * @param accountNumber PAN - primary account number
     * @return extract right-most 12 digits of the primary account number (PAN)
     */
    private fun extractPanAccountNumberPart(accountNumber: String): String {
        var accountNumberPart: String? = null
        accountNumberPart = if (accountNumber.length > 12) accountNumber.substring(
            accountNumber.length - 13,
            accountNumber.length - 1
        ) else accountNumber
        return accountNumberPart
    }
}