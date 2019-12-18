package com.hurtado.forms.control

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.hurtado.forms.directives.ValidationControl
import com.hurtado.forms.widget.FormField

/**
 * Created by Juan Hurtado on 05-21-18
 *
 * Form control is a class container for all validation functions the user wants
 * to test layout edit text against
 *
 * It automatically checks while the host edit text is changing if the content is matching expected
 * Directives defined on the concrete validations some built in validators (common ones) are provided
 *
 */
open class FieldAction(
    private var layout: FormField,
    private var inputChangeCallback: (ValidationControl) -> Unit
) : ValidationControl {

    private val fieldId = layout.editText?.id ?: -1
    private val fieldValidations = layout.validation
    private var isFieldValid = false

    init {
        layout.editText?.onFocusChangeListener =
            View.OnFocusChangeListener { _, _ -> assertFieldValidity() }
        layout.editText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(
                s: CharSequence?, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?, start: Int,
                before: Int, count: Int
            ) {
                assertFieldValidity()
            }
        }
        )
    }

    override fun onCreateFormError(value: String) =
        String().apply {
            fieldValidations.forEach { validators ->
                validators.onValidate(
                    layout.context,
                    value
                )?.let { error ->
                    return error
                }
            }
            return this
        }

    fun assertFieldValidity() {
        val formError = onCreateFormError(
            layout.editText?.text.toString()
        )

        isFieldValid = formError.isEmpty()
        if (!isFieldValid) layout.error = formError
        else layout.error = null

        return inputChangeCallback.invoke(this)
    }

    override fun input() = layout.editText?.text.toString()

    override fun getView() = layout.editText

    override fun isValid() = isFieldValid

    override fun getId() = fieldId

    override fun clear() {
        fieldValidations.clear()
        layout.clearValidations()
    }

}