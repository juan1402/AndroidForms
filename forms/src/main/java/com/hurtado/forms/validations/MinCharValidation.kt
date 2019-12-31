package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.*

open class MinCharValidation(private val minChar: Int = 5) : Validation<String>(R.string.min_validation_error) {
    override fun onValidate(context: Context?, value: String) = when {
        value.length >= minChar -> null
        else -> super.onValidate(context, String.format(Locale.getDefault(),
            value, minChar))
    }

    override fun formatError(context: Context?, error: Int)
            = String.format(context?.getString(error) ?: "", minChar)
}