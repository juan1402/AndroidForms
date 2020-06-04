package com.hurtado.forms.widget

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.hurtado.forms.directives.ChangeListener
import com.hurtado.forms.widget.base.FormField
import com.hurtado.forms.widget.controller.EditController

class InputCompatField(context: Context, attrs: AttributeSet)
    : FormField<String, TextInputEditText>(context, attrs, TextInputEditText::class) {
    override fun controller(l: ChangeListener) = EditController(this, l)
    override fun child() = this.editText as TextInputEditText? ?: TextInputEditText(context)
}