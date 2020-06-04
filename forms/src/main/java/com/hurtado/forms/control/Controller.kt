package com.hurtado.forms.control

import android.view.View
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.FormController
import com.hurtado.forms.directives.ChangeListener

abstract class Controller<T, K : View>(
        protected val action: Field<T, K>,
        private var listener: ChangeListener?
) : FormController<T, K> {

    private var isFieldValid = false

    override fun onCreateFormError(value: T) = String().apply {
        action.validations().forEach { validators ->
            validators.onValidate(action.view().context, value)
                    ?.let { error -> return error }
        }
    }

    protected fun assertFieldValidity(criteria: T): Unit =
            with(action.view()) {
                onCreateFormError(criteria).let { error ->
                    isFieldValid = error.isEmpty()
                    if (!isFieldValid) this.error = error
                    else this.error = null
                }
                listener?.onChange(this@Controller)
            }

    override fun controllerId() = child().id

    override fun isValid() = isFieldValid

    override fun child() = action.child()

    override fun view() = action.view()

    override fun clear() = with(action) {
        validations().clear()
        clearValidations(); listener = null
    }
}