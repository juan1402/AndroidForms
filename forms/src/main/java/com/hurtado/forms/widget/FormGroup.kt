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
 * @see com.hurtado.forms.control.FieldAction
 *
 */
open class FormGroup<T : FormResult>(context: Context, attrs: AttributeSet?) :
    ConstraintLayout(context, attrs) {

    /**
     * Use this public variable to request form
     * Validity at any moment
     */
    var isFormValid = false

    interface CompleteListener<T : FormResult> {
        fun onFormComplete(result: T)
    }

    private var validationCallback: (
        Boolean,
        ValidationControl
    ) -> Boolean = { isValid, control ->

        mapUserInput(control)
        if (::submitButton.isInitialized)
            submitButton.isEnabled = isValid
        isFormValid = isValid

        isValid
    }

    private val properties = ArrayList<KMutableProperty<*>>()

    private lateinit var callback: CompleteListener<T>
    private val controls = ArrayList<ValidationControl>()
    private lateinit var submitButton: Button
    private lateinit var result: T

    /**
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
     * @see com.hurtado.forms.control.FieldAction
     * is a TextInputEditText validation wrapper
     */
    override fun onViewAdded(view: View?) = findFormElements(view)


    /**
     * Recursive function to find any view form elements
     * Regardless  hierarchy level
     */
    private fun findFormElements(view: View?) {
        matchFormElements(view)

        if (
            view is ViewGroup &&
            view.childCount > 0
        ) {
            view.children.forEach { child ->
                matchFormElements(child)
                if (
                    child is ViewGroup &&
                    view.childCount > 0
                ) findFormElements(child)
            }
        }
    }

    /**
     * Match Form view elements and assign validations
     * Searches for buttons and form fields
     */
    private fun matchFormElements(view: View?) =
        view?.let {
            if (it is FormField) {
                controls.add(
                    FieldAction(
                        it,
                        it.validation,
                        validationCallback
                    )
                )
            }

            if (it is Button) {
                it.isEnabled = false
                submitButton = it
                it.setOnClickListener {
                    callback.onFormComplete(result)
                }
            }
        }

    /**
     * @return package name in format package.name:id/resource-id
     */
    private fun resourceId(view: View?) =
        if (view?.id == View.NO_ID) ":id/no-id"
        else view?.resources?.getResourceName(view.id)

    fun onDestroy() {
        if (::submitButton.isInitialized)
            submitButton.setOnClickListener(null)

        controls.onEach { it.clear() }
        properties.clear()
        controls.clear()
    }

}