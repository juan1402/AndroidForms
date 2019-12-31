package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.regex.Pattern

class EmailValidation: Validation<String>(R.string.email_validation_error) {
    override fun onValidate(context: Context?, value: String) = when {
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE).matcher(value).find() -> null
        else -> super.onValidate(context, value)
    }
}