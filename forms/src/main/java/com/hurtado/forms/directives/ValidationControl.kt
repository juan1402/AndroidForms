package com.hurtado.forms.directives

import android.view.View
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Juan Hurtado on 05-23-18
 * Abstract control over custom error enabled objects
 */
interface ValidationControl {

    /**
     * Within this function we will determine if our validation
     * expectations are fulfilled, you can create your custom validations
     */
    fun onCreateFormError(value: String): String

    /**
     * Check if there is an error present on the current layout
     * Logic will depend on each control
     * @see TextInputLayout
     */
    fun isValid(): Boolean

    /**
     * @see com.hurtado.forms.control.FieldAction
     * Gets id of child item Override this method
     * to retrieve child's id
     */
    fun getId(): Int

    /**
     * @see com.hurtado.forms.control.FieldAction
     * @return edit text view id exists
     */
    fun getView(): View?

    /**
     * @return edit text input
     */
    fun input(): String

    /**
     * Clear all listeners and saved instances
     */
    fun clear()

}