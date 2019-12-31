package com.hurtado.forms.widget.controller

import androidx.appcompat.widget.SwitchCompat
import com.hurtado.forms.control.Controller
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.FormController

class SwitchController(
    action: Field<Boolean, SwitchCompat>,
    callback: (FormController<Boolean, SwitchCompat>) -> Unit
) : Controller<Boolean, SwitchCompat>(action, callback) {

    override fun onCreate() {
        assertFieldValidity(child().isChecked)
        child().setOnCheckedChangeListener { _, status ->
            assertFieldValidity(status)
        }
    }

    override fun input() = child().isChecked
}