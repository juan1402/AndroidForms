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
     *
     * @see TextInputLayout
     */
    fun isValid(): Boolean

    /**
     * Gets id of child item Override this method
     * to retrieve child's id
     *
     * @see com.hurtado.forms.control.FieldAction
     * @return TextInputLayout id
     */
    fun getId(): Int

    /**
     * Get Form field TextInputLayout if exists
     *
     * @see com.hurtado.forms.control.FieldAction
     * @return TextInputLayout
     */
    fun getView(): View?

    /**
     * Gets TextInputLayout id
     * @return id
     */
    fun input(): String

    /**
     * Clear all listeners and saved instances
     * Call this method to avoid memory leaks
     */
    fun clear()

}