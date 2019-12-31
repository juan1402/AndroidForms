package com.hurtado.forms.control

import android.view.View
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.FormController

abstract class Controller<InputType, Child : View>(
    protected val action: Field<InputType, Child>,
    private val callback: (FormController<InputType, Child>) -> Unit
) : FormController<InputType, Child> {

    private var isFieldValid = false

    override fun onCreateFormError(value: InputType) =
        String().apply {
            action.validations().forEach { validators ->
                validators.onValidate(
                    action.view().context,
                    value
                )?.let { error ->
                    return error
                }
            }
            return this
        }

    protected fun assertFieldValidity(criteria: InputType) {
        val layout = action.view()
        val formError = onCreateFormError(criteria)
        isFieldValid = formError.isEmpty()
        if (!isFieldValid) layout.error = formError
        else layout.error = null

        return callback.invoke(this)
    }

    override fun controllerId() = child().id

    override fun isValid() = isFieldValid

    override fun child() = action.child()

    override fun view() = action.view()

    override fun clear() {
        action.validations().clear()
        action.clearValidations()
    }

}