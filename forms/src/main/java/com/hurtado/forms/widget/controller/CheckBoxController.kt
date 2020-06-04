package com.hurtado.forms.widget.controller

import androidx.appcompat.widget.AppCompatCheckBox
import com.hurtado.forms.control.Controller
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.ChangeListener

class CheckBoxController(
    action: Field<Boolean, AppCompatCheckBox>,
    listener: ChangeListener
) : Controller<Boolean, AppCompatCheckBox>(action, listener) {

    override fun onCreate() {
        assertFieldValidity(child().isChecked)
        child().setOnCheckedChangeListener { _, status ->
            assertFieldValidity(status)
        }
    }

    override fun input() = child().isChecked
}