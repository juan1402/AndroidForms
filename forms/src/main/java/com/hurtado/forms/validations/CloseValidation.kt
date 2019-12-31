package com.hurtado.forms.validations

import android.content.Context
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.*

class CloseValidation : Validation<Date>(R.string.close_validation_error) {
    override fun onValidate(context: Context?, value: Date): String? {
        val start = Calendar.getInstance()
        start.time = value
        start.set(Calendar.HOUR_OF_DAY, 15)
        start.set(Calendar.MINUTE, 0)

        val end = Calendar.getInstance()
        end.time = value
        end.set(Calendar.HOUR_OF_DAY, 21)
        end.set(Calendar.MINUTE, 0)


        return if (value.after(start.time) && value.before(end.time)) {
            null
        } else {
            super.onValidate(context, value)
        }
    }
}

