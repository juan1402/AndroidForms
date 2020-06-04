package com.hurtado.forms.widget.controller

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import com.hurtado.forms.control.Controller
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.ChangeListener

class EditController(
        action: Field<String, TextInputEditText>,
        listener: ChangeListener
) : Controller<String, TextInputEditText>(action, listener),
        View.OnFocusChangeListener {

    override fun onCreate() {
        child().apply {
            onFocusChangeListener = this@EditController
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                ) {
                }

                override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                ) = assertFieldValidity(child().text.toString())
            })
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) =
            assertFieldValidity(child().text.toString())

    override fun input() = child().text.toString()
}