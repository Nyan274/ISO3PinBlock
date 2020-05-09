package com.nyan.iso3pinblock.views

interface MainContract {
    interface View {
        fun showResult(result: String)
    }

    interface Presenter {
        fun encodeISO3(pin: String, pan: String)
    }
}