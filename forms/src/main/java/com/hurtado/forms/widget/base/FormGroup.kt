package com.hurtado.forms.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.FormButton
import com.hurtado.forms.directives.FormController
import com.hurtado.forms.directives.FormResult
import java.util.regex.Pattern
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance

/**
 *
 * Created by Juan Hurtado on 05-22-18
 *
 * This is a class container for all Form controls available on the screen
 * Use this class for an easier management of collective controls
 * @see FormController<InputType>
 *
 */
open class FormGroup<TypeResult : FormResult>(context: Context, attrs: AttributeSet?) :
        ConstraintLayout(context, attrs) {

    /**
     * Use this public variable to request form
     * Validity at any moment
     */
    private var isFormValid = false

    /**
     * Form complete listener
     * Form result type object will be sent as parameter
     *
     * @see FormResult
     */
    private val data = MutableLiveData<TypeResult>()

    /**
     * Internal callback
     * Is executed each time a form field
     * Input changes
     *
     * @see mapUserInput
     * @see FormController
     */
    protected var inputChangeCallback: (
            FormController<*, *>
    ) -> Unit = { control ->
        mapUserInput(control)
        assertFormValidity()
    }

    private val properties = ArrayList<KMutableProperty<*>>()
    protected val controllers = ArrayList<FormController<*, *>>()
    private lateinit var submitButton: Button
    private lateinit var result: TypeResult

    /**
     * @param control validation control
     * @see FormController
     *
     * Matches user input with a field
     * package.name:id/resource-id , resource id will be extracted
     * And matched against text input edit text inside your form field
     */
    private fun mapUserInput(control: FormController<*, *>) = properties.forEach { field ->
        val controlId = resourceId(control.child())?.split(DELIMITERS)?.get(1) ?: String()
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
    fun of(clazz : KClass<TypeResult>): LiveData<TypeResult> {
        result = clazz.createInstance()
        result::class.members.forEach { field ->
            if (field is KMutableProperty) properties.add(field)
        }
        return data
    }

    /**
     * @param view form attached view
     *
     * Recursive function to find any view form elements
     * Regardless  hierarchy level
     */
    private fun recursiveChildLoop(parent: ViewGroup) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            matchFormElements(child)
            if (child is ViewGroup) recursiveChildLoop(child)
        }
    }

    private fun assertFormValidity() {
        isFormValid = controllers.find { !it.isValid() } == null
        if (::submitButton.isInitialized)
            submitButton.isEnabled = isFormValid
    }

    /**
     * @param view form attached view
     *
     * Match Form view elements and assign validations
     * Searches for buttons and form fields
     */
    protected fun matchFormElements(view: View?) = view?.apply {
        if (this is FormButton && !::submitButton.isInitialized) {
            submitButton = this as Button
            submitButton.isEnabled = false

            this.setOnClickListener {
                data.value = result
            }
        }

        if (this is Field<*, *>) {
            val controller = this.controller(inputChangeCallback)

            if (controllers.find {
                        it.controllerId() ==
                                controller.controllerId()
                    } == null) {
                controllers.add(controller)
            }

        }
    }

    /**
     * package.name:id/no-id will be returned if
     * @param view has no valid id
     *
     * @return package name in format package.name:id/resource-id
     */
    private fun resourceId(view: View?) =
            if (view?.id == View.NO_ID) NO_ID
            else view?.resources?.getResourceName(view.id)

    /**
     * If form is valid a live data will be triggered
     * @see data
     * @return true if form is valid and live data
     * was triggered false otherwise
     */
    fun isFormComplete(): Boolean {
        val isValid = isFormValid
        if (isValid) data.value = result
        return isValid
    }

    override fun onViewAdded(view: View?) {
        matchFormElements(view)
        if (view is ViewGroup) recursiveChildLoop(view)
    }

    /**
     * Must be called to initialized controller validations
     */
    fun requireValidation() = controllers.forEach { it.onCreate() }

    fun onDestroy() {
        if (::submitButton.isInitialized)
            submitButton.setOnClickListener(null)

        controllers.onEach { it.clear() }
        properties.clear()
        controllers.clear()
    }

    companion object {
        private const val DELIMITERS = ":id/"
        private const val NO_ID = ":id/no-id"
    }
}