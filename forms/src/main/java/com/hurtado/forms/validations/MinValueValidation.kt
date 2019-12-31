package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation

class MinValueValidation: Validation<Int>(R.string.min_value_error) {
    override fun onValidate(context: Context?, value: Int) = when {
        value <= 3 -> null
        else -> super.onValidate(context, value)
    }
}