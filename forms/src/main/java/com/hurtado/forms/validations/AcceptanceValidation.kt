package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation

class AcceptanceValidation : Validation<Boolean>(R.string.acceptance_validation_error) {
    override fun onValidate(context: Context?, value: Boolean) = when {
        value -> null
        else -> super.onValidate(context, value)
    }
}