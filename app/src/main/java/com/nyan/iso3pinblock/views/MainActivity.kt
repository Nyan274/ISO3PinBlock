package com.nyan.iso3pinblock.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nyan.iso3pinblock.R
import com.nyan.iso3pinblock.ServiceLocator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.regex.Pattern

class MainActivity : AppCompatActivity(), MainContract.View {
    private val presenter: MainContract.Presenter by lazy {
        ServiceLocator.instance(this).getMainPresenter(this)
    }
    private val regex = Pattern.compile("^[0-9]{4,12}$")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnGenerate.setOnClickListener {
            if (isValid(etPin.text)) {
                presenter.encodeISO3(etPin.text.toString(), getString(R.string.defaultPan))
            } else {
                etPin.error = getString(R.string.incorrect_pin_length)
            }
        }
    }

    private fun isValid(s: CharSequence): Boolean {
        return regex.matcher(s).matches()
    }

    override fun showResult(result: String) {
        tvResult.text = result.toUpperCase(Locale.getDefault());
    }
}
