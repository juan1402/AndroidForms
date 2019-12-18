package com.hurtado.forms.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.hurtado.forms.control.FieldAction
import com.hurtado.forms.directives.FormResult
import com.hurtado.forms.directives.ValidationControl
import java.util.regex.Pattern
import kotlin.reflect.KMutableProperty

/**
 *
 * Created by Juan Hurtado on 05-22-18
 *
 * This is a class container for all Form controls available on the screen
 * Use this class for an easier management of collective controls
 * @see FieldAction
 *
 */
open class FormGroup<T : FormResult>(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    /**
     * Use this public variable to request form
     * Validity at any moment
     */
    var isFormValid = false

    /**
     * Form complete listener
     * Form result type object will be sent as parameter
     *
     * @see FormResult
     */
    interface CompleteListener<T : FormResult> {
        fun onFormComplete(result: T)
    }

    /**
     * Internal callback
     * Is executed each time a form field
     * Input changes
     *
     * @see mapUserInput
     * @see ValidationControl
     */
    private var inputChangeCallback: (
        ValidationControl
    ) -> Unit = { control ->
        mapUserInput(control)
        assertFormValidity()
    }

    private val properties = ArrayList<KMutableProperty<*>>()

    private lateinit var callback: CompleteListener<T>
    private val controls = ArrayList<ValidationControl>()
    private lateinit var submitButton: Button
    private lateinit var result: T

    /**
     * @param control validation control
     * @see ValidationControl
     *
     * Matches user input with a field
     * package.name:id/resource-id , resource id will be extracted
     * And matched against text input edit text inside your form field
     */
    private fun mapUserInput(control: ValidationControl) = properties.forEach { field ->
        val controlId = resourceId(control.getView())?.split(":id/")?.get(1)
        if (Pattern.compile(controlId).matcher(field.name).find()) {
            field.setter.call(result, control.input())
        }
    }

    /**
     * @param callback complete listener
     * @param result Result empty type object
     *
     * Register result class mutable properties
     * for a later id match attempt
     *
     * sets completion callback
     * sets empty result class
     */
    fun setOnCompleteListener(
        callback: CompleteListener<T>,
        result: T
    ) {
        this.callback = callback
        this.result = result

        result::class.members.forEach { field ->
            if (field is KMutableProperty) properties.add(field)
        }
    }

    /**
     * Add individual FormField controls to this form group
     * @see FieldAction
     *
     * is a TextInputEditText validation wrapper
     */
    override fun onViewAdded(view: View?) = findFormElements(view)


    /**
     * @param view form attached view
     *
     * Recursive function to find any view form elements
     * Regardless  hierarchy level
     */
    private fun findFormElements(view: View?) {
        matchFormElements(view)
        if (view is ViewGroup) {
            view.children.forEach { child ->
                matchFormElements(child)
                if (
                    child is ViewGroup &&
                    view.childCount > 0
                ) findFormElements(child)
            }
        }
    }

    private fun assertFormValidity() {
        isFormValid = controls.find { !it.isValid() } == null
        if (::submitButton.isInitialized)
            submitButton.isEnabled = isFormValid

    }

    /**
     * @param view form attached view
     *
     * Match Form view elements and assign validations
     * Searches for buttons and form fields
     */
    private fun matchFormElements(view: View?) = view?.apply {
        if (this is Button && !::submitButton.isInitialized) {
            this.isEnabled = false
            submitButton = this

            this.setOnClickListener {
                callback.onFormComplete(result)
            }
        }

        if (this is FormField)
            if (controls.find { it.getId() == this.editText?.id } == null)
                controls.add(FieldAction(this, inputChangeCallback))
    }

    /**
     * package.name:id/no-id will be returned if
     * @param view has no valid id
     *
     * @return package name in format package.name:id/resource-id
     */
    private fun resourceId(view: View?) =
        if (view?.id == View.NO_ID) ":id/no-id"
        else view?.resources?.getResourceName(view.id)

    /**
     * Forces form complete listener to trigger
     * Use this method to trigger form information if a button
     * is not preset inside our form group
     *
     * @see CompleteListener
     * @return form result data may be incomplete at this point
     */
    fun forceComplete() {
        if (::callback.isInitialized) {
            callback.onFormComplete(result)
        }
    }

    fun onDestroy() {
        if (::submitButton.isInitialized)
            submitButton.setOnClickListener(null)

        controls.onEach { it.clear() }
        properties.clear()
        controls.clear()
    }

}