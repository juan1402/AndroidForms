package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.*

/**
 * Validator that requires controls to have a value less than a number.
 * `max()` exists only as a function, not as a directive.
 */
open class MaxCharValidation(private val maxChar: Int = 15) : Validation<String>(R.string.max_validation_error) {
    override fun onValidate(context: Context?, value: String) = when {
        value.length >= maxChar -> super.onValidate(context, String.format(Locale.getDefault(),
            value, maxChar))
        else -> null
    }

    override fun formatError(context: Context?, error: Int)
            = String.format(context?.getString(error) ?: "", maxChar)
}