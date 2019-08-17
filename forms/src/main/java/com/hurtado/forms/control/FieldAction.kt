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
class FieldAction(
    private val layout: TextInputLayout?,
    private val validators: ArrayList<Validation>,
    private val validationCallback: (Boolean) -> Boolean
) : ValidationControl {

    private lateinit var errorCallback: (layout: TextInputLayout?,
                                         errors: String?) -> Unit

    private val focusListener = View.OnFocusChangeListener { _, _ ->
        isValid()
    }

    private val textWatcher = object : TextWatcher {
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

    private var isEnable = true

    init {
        layout?.editText?.onFocusChangeListener = focusListener
        layout?.editText?.addTextChangedListener(textWatcher)
    }

    override fun onCreateFormError(value: String): String {
        val error = String()

        if (isEnable) {
            validators.forEach { validators ->
                validators.onValidate(
                    layout?.context,
                    value
                )?.let { error ->
                    return error
                }
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

        return validationCallback.invoke(isFormValid)
    }

    override fun setCallback(
        callback: (
            layout: TextInputLayout?,
            errors: String?
        ) -> Unit
    ) {
        errorCallback = callback
    }

    override fun getLayoutId() = layout?.editText?.id ?: -1

    override fun getId() = layout?.editText?.id ?: -1

    override fun setIsEnable(enabled: Boolean) {
        layout?.error = null
        isEnable = enabled
    }

    override fun isEnabled() = isEnable

}