package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.Date

class FutureDateValidation : Validation<Date>(R.string.future_validation_error) {
    override fun onValidate(context: Context?, value: Date) = when {
        Date().before(value) -> null
        else -> super.onValidate(context, value)
    }
}