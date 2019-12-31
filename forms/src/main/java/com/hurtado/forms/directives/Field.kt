package com.hurtado.forms.directives

import android.view.View
import com.hurtado.forms.control.Validation
import com.hurtado.forms.widget.base.FormField

interface Field<InputType, Child: View> {

    /**
     * Form Field view for Validation criteria
     * @return View
     */
    fun view(): FormField<InputType, Child>

    /**
     * @see FormField
     * @return form field child
     */
    fun child(): Child

    /**
     * Clear inherited validations
     */
    fun clearValidations()

    /**
     * Get View Validations
     * @see Validation
     */
    fun validations(): ArrayList<Validation<InputType>>

    /**
     * Get validation control implementation
     * Which will be used against user logic
     */
    fun controller(host: (FormController<InputType, Child>) -> Unit): FormController<InputType, Child>
}