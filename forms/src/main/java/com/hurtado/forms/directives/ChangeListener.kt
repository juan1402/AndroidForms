package com.hurtado.forms.directives

interface ChangeListener {
    /**
     * executed each time a form field Input changes
     * @see FormController
     */
    fun onChange(controller: FormController<*, *>)
}