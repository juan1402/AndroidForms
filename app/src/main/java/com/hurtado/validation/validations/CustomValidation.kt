package com.hurtado.validation.validations

import android.content.Context
import com.hurtado.forms.control.Validation
import com.hurtado.validation.R

class CustomValidation: Validation<String>(R.string.custom_error) {

    override fun onValidate(context: Context?, value: String): String? {
        // Error is triggered if input contains letter A
        return if (value.contains("A"))
            super.onValidate(context, value)
        else
            null
    }
}