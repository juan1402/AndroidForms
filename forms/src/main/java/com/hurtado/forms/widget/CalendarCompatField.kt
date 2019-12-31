package com.hurtado.forms.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.hurtado.forms.directives.FormController
import com.hurtado.forms.widget.base.FormField
import com.hurtado.forms.widget.controller.CalendarController
import java.util.*

class CalendarCompatField(
    context: Context,
    attrs: AttributeSet
) : FormField<Date, TextInputEditText>(context, attrs, TextInputEditText::class) {
    override fun controller(host: (FormController<Date, TextInputEditText>) -> Unit) =
        CalendarController(this, host)

    override fun child() = this.editText as TextInputEditText? ?: TextInputEditText(context)
}