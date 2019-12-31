package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.regex.Pattern

class SymbolsValidation: Validation<String>(R.string.symbols_validation_error) {
    override fun onValidate(context: Context?, value: String) = when {
        Pattern.compile("[^A-Za-z0-9 ]+", Pattern.CASE_INSENSITIVE)
            .matcher(value).find() -> super.onValidate(context, value)
        else -> null
    }
}