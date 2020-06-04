package com.hurtado.forms.directives

import android.view.View
import com.hurtado.forms.control.Validation
import com.hurtado.forms.widget.base.FormField

interface Field<T, K: View> {

    /**
     * Form Field view for Validation criteria
     * @return View
     */
    fun view(): FormField<T, K>

    /**
     * @see FormField
     * @return form field child
     */
    fun child(): K

    /**
     * Clear inherited validations
     */
    fun clearValidations()

    /**
     * Get View Validations
     * @see Validation
     */
    fun validations(): ArrayList<Validation<T>>

    /**
     * Get validation control implementation
     * Which will be used against user logic
     */
    fun controller(l: ChangeListener): FormController<T, K>
}