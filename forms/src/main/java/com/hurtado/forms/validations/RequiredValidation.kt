package com.hurtado.forms.validations

import android.content.Context
import android.text.TextUtils
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation

class RequiredValidation : Validation(R.string.required_validation_error) {

    override fun onValidate(context: Context?, value: String) = when {
        TextUtils.isEmpty(value) -> super.onValidate(context, value)
        value == "" -> super.onValidate(context, value)
        else -> null
    }

}