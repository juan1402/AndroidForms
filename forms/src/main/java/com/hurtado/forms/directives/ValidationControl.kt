package com.hurtado.forms.directives

import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Juan Hurtado on 05-23-18
 * Abstract control over custom error enabled objects
 */
internal interface ValidationControl {

    /**
     * set's a callback forms errors
     */
    fun setCallback(callback: (layout: TextInputLayout?,
                               errors: String?) -> Unit)
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
     * Gets it's layout idOverride this method to
     * retrieve it's id
     */
    fun getLayoutId(): Int

    /**
     * @see com.hurtado.forms.control.FieldAction
     * enable / disable validations
     */
    fun setIsEnable(enabled: Boolean)

    /**
     * @see com.hurtado.forms.control.FieldAction
     * checks if validation is enabled
     */
    fun isEnabled(): Boolean

}