package com.hurtado.forms.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.hurtado.forms.control.FieldAction
import com.hurtado.forms.directives.ValidationControl
import java.util.*

/**
 * Created by Juan Hurtado on 05-22-18
 *
 * This is a class container for all Form controls available on the screen
 * Use this class for an easier management of collective controls
 * @see com.hurtado.forms.control.FieldAction
 *
 */
class FormGroup(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val validationCallback: (Boolean) -> Boolean = { isValid ->
        if (::submitButton.isInitialized)
            submitButton.isEnabled = isValid

        isValid
    }

    private val disabledActions = ArrayList<ValidationControl>()
    private val controls = ArrayList<ValidationControl>()
    private lateinit var submitButton: Button

    /**
     * Add individual FormField controls to this form group
     * @see com.hurtado.forms.control.FieldAction
     * is a TextInputEditText validation wrapper
     */
    override fun onViewAdded(view: View?) {
        super.onViewAdded(view)

        if (view is FormField) {
            controls.add(FieldAction(view,
                view.validation, validationCallback))
        }

        if (view is Button) {
            view.isEnabled = false
            submitButton = view
        }

    }


    /**
     * @param id layout id to find specific action and disable it's validations
     * As a user you will be able to enable this actions later on with
     * @see enableAllActions this is useful for dynamic forms
     */
    fun disableById(id: Int) {
        controls.find { control ->
            control.getLayoutId() == id
        }?.let { safeItem ->
            disabledActions.add(safeItem)
            safeItem.setIsEnable(false)
        }
    }

    /**
     * Enable all disabled actions by
     * @see disableById
     */
    fun enableAllActions() {
        disabledActions.forEach { action ->
            action.setIsEnable(true)
        }
        disabledActions.clear()
    }

    /**
     * Adds a listener to each given control
     * @see com.hurtado.forms.control.FieldAction
     */
    fun addControlErrorListener(
        callback: (
            layout: TextInputLayout?,
            errors: String?
        ) -> Unit
    ) {
        controls.forEach { safeControl ->
            safeControl.setCallback(callback)
        }
    }

    /**
     * Check all form controls validity
     * @return if all forms are meeting given directives
     */
    fun isValid(): Boolean {
        controls.forEach { safeValidation ->
            if (!safeValidation.isValid()) {
                submitButton.isEnabled = false
                return false
            }
        }

        submitButton.isEnabled = true
        return true
    }

}