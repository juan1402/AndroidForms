package com.hurtado.forms.directives

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.hurtado.forms.widget.base.FormField

/**
 * Created by Juan Hurtado on 05-23-18
 *
 * Extend this class over your custom class controller
 *
 */
interface FormController<T, K : View> {

    /**
     * Within this function we will determine if our validation
     * expectations are fulfilled, you can create your custom validations
     */
    fun onCreateFormError(value: T): String

    /**
     * Check if there is an error present on the current layout
     * Logic will depend on each control
     *
     * @see TextInputLayout
     */
    fun isValid(): Boolean

    /**
     * Get Form field TextInputLayout if exists
     * @return TextInputLayout
     */
    fun view(): FormField<T, K>

    /**
     * Get Form field TextInputLayout if exists
     * @return TextInputLayout
     */
    fun child(): K?

    /**
     * Gets TextInputLayout id
     * @return id
     */
    fun input(): T

    /**
     * Controller creation callback
     */
    fun onCreate()

    /**
     * Clear all listeners and saved instances
     * Call this method to avoid memory leaks
     */
    fun clear()

    /**
     * Get controller id to avoid
     * Controller duplication on FormGroup
     */
    fun controllerId(): Int

}