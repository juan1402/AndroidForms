package com.hurtado.forms.control

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.hurtado.forms.directives.ValidationControl

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
    private var layout: TextInputLayout?,
    private val validators: ArrayList<Validation>,
    private var validationCallback: (Boolean, ValidationControl) -> Boolean
) : ValidationControl {

    init {
        layout?.editText?.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> isValid() }
        layout?.editText?.addTextChangedListener(object : TextWatcher {
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
                isValid()
            }
        }
        )
    }

    private lateinit var errorCallback: (
        layout: TextInputLayout?,
        errors: String?
    ) -> Unit

    override fun onCreateFormError(value: String): String {
        val error = String()

        validators.forEach { validators ->
            validators.onValidate(
                layout?.context,
                value
            )?.let { error ->
                return error
            }
        }

        return error
    }

    override fun isValid(): Boolean {
        val formError = onCreateFormError(
            layout?.editText
                ?.text.toString()
        )

        val isFormValid = formError.isEmpty()

        if (!isFormValid) {
            layout?.error = formError

            if (::errorCallback.isInitialized)
                errorCallback.invoke(layout, formError)
        } else {
            layout?.error = null
        }

        return validationCallback.invoke(isFormValid, this)
    }

    override fun getId() = layout?.editText?.id ?: -1

    override fun getView() = layout?.editText

    override fun input() = layout?.editText?.text.toString()

    override fun clear() {
        validators.clear()
        layout = null
    }

}