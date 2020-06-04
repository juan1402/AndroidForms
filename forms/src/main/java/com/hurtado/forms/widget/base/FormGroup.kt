package com.hurtado.forms.widget.base

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.*
import com.google.android.material.textfield.TextInputEditText
import com.hurtado.forms.directives.*
import com.hurtado.forms.directives.FormButton
import java.util.regex.Pattern
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance

/**
 * Created by Juan Hurtado on 05-22-18
 * Container class for your form fields
 * Manages all your form validations
 *
 * @see FormField<T, K>
 * @see FormController<T>
 */
open class FormGroup<T : FormResult>(context: Context, attrs: AttributeSet?) :
        ConstraintLayout(context, attrs), LifecycleObserver, ChangeListener {

    private var isFormValid = false

    /**
     * Form complete listener
     * Form result type object will be sent as parameter
     *
     * @see FormResult
     */
    private val resultLive = MutableLiveData<T>()

    private val controller = ArrayList<FormController<*, *>>()
    private lateinit var button: Button

    /**
     * Cached mapped kotlin properties for individual controllers
     * optimize property search using this map
     */
    private val kMap = HashMap<String, ArrayList<KMutableProperty<*>>>()

    /**
     * Child property patterns runtime cache
     * Must be cleared on destroy
     */
    private val pMap = HashMap<String, Pattern>()

    /**
     * Child property resource id runtime cache
     * Must be cleared on destroy
     */
    private val rMap = SparseArray<String>()

    /**
     * Form result object
     * Initialized at after executing
     *
     * @see of<TypeResult>()
     */
    @PublishedApi
    internal lateinit var result: T

    override fun onChange(controller: FormController<*, *>) {
        mapUserInput(controller); assertFormValidity()
    }

    /**
     * @param control validation control
     * @see FormController
     *
     * Matches user input with a field
     * package.name:id/resource-id , resource id will be extracted
     * And matched against text input edit text inside your form field
     */
    private fun mapUserInput(control: FormController<*, *>) {
        val controlName = resourceCache(control)
        /* Loop only through control properties */
        kMap[controlName]?.forEach { f ->
            if (compiledPatterCache(resourceCache(control))
                            .matcher(f.name).find())
                f.setter.call(result, control.input())
        }
    }

    /**
     * look's for field resource pattern in cache
     * or creates it otherwise
     *
     * @param controlId field form controller id
     */
    private fun compiledPatterCache(controlId: String): Pattern {
        val fieldPattern: Pattern
        if (!pMap.containsKey(controlId)) {
            /* resolve new resource id */
            fieldPattern = Pattern.compile(controlId)
            pMap[controlId] = fieldPattern
        } else {
            /* cached resource id */
            fieldPattern = pMap[controlId]
                    ?: Pattern.compile(controlId)
        }
        return fieldPattern
    }


    /**
     * look's for field resource name in cache
     * or creates it otherwise
     *
     * @param control field form controller
     */
    private fun resourceCache(control: FormController<*, *>): String {
        val resourceId = control.child()?.id ?: -1
        var controlName = rMap.get(resourceId)
        if (controlName == null) {
            controlName = resourceId(control.child())
                    ?.split(DELIMITERS)?.get(1) ?: String()
            rMap.put(resourceId, controlName)
        }
        return controlName
    }

    /**
     * initialize form result object
     * @return this form
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified K : FormResult> of(): FormGroup<T> {
        result = K::class.createInstance() as T
        return this@FormGroup
    }

    /**
     * Adds the given observer to the observers list within the lifespan of the given
     * owner. The events are dispatched on the main thread. If LiveData already has data
     * set, it will be delivered to the observer.
     *
     * Also adds this @FormGroup as an observer thi the life cycle owner
     * making this class life cycle aware
     */
    fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        resultLive.observe(owner, observer); owner.lifecycle.addObserver(this)
    }

    /**
     * @param group form attached view
     *
     * Recursive function to find any form elements
     * Regardless  it's hierarchy level
     */
    private fun recursiveHierarchySearch(group: ViewGroup) {
        for (i in 0 until group.childCount) {
            with(group.getChildAt(i)) {
                matchFormElements(this)
                if (this is ViewGroup)
                    recursiveHierarchySearch(this)
            }
        }
    }

    private fun assertFormValidity() {
        isFormValid = controller.find { !it.isValid() } == null
        if (::button.isInitialized) button.isEnabled = isFormValid
    }

    /**
     * @param view form attached view
     *
     * Match Form view elements and assign validations
     * Searches for buttons and form fields
     */
    private fun matchFormElements(view: View?) = view?.apply {
        if (this is FormButton && !::button.isInitialized) {
            button = this as Button
            button.isEnabled = false
            setOnClickListener { resultLive.value = result }
        }

        if (this is Field<*, *>) {
            val controller = this.controller(this@FormGroup)

            if (this@FormGroup.controller.find {
                        it.controllerId() ==
                                controller.controllerId()
                    } == null) {
                this@FormGroup.controller.add(controller)
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
     * @see resultLive
     *
     * @return true if form is valid and live data
     * was triggered false otherwise
     */
    fun isFormComplete() = isFormValid.apply {
        if (this) resultLive.value = result
    }

    override fun onViewAdded(view: View?) {
        matchFormElements(view)
        if (view is ViewGroup)
            recursiveHierarchySearch(view)
    }

    /**
     * Must be called to initialized controller validations
     * maps properties in for shorter runtime loops
     *
     * @see kMap
     * @see mapUserInput(control)
     * Also caches field patterns and field resource names
     */
    fun requireValidation() = ArrayList<KMutableProperty<*>>().apply {
        result::class.members.forEach { f -> if (f is KMutableProperty) add(f) }
    }.also { properties ->
        controller.forEach {
            it.onCreate(); resourceCache(it)
                .apply { properties.forEach { f -> propertyMapper(this, f) } }
        }
    }

    /**
     * @param resourceName xml resource name
     * @param field some field random inside a child control
     *
     * Matches resource name with it's corresponding field
     */
    private fun propertyMapper(resourceName: String, field: KMutableProperty<*>) {
        if (compiledPatterCache(resourceName).matcher(field.name).find()) {
            if (kMap.containsKey(resourceName)) kMap[resourceName]?.add(field)
            else kMap[resourceName] = arrayListOf(field)
        }
    }

    /**
     * Clear all form elements
     * all fields are set to an empty string
     * @see controller
     */
    fun clear() = controller.forEach {
        it.child().apply { if (this is TextInputEditText) setText("") }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        if (::button.isInitialized)
            button.setOnClickListener(null)

        /* clear each controller validations */
        controller.onEach { it.clear() }
        /* clear controller list */
        controller.clear()

        /* clear resource cache */
        rMap.clear()
        /* clear properties cache */
        kMap.clear()
        /* clear pattern cache */
        pMap.clear()
    }

    companion object {
        private const val DELIMITERS = ":id/"
        private const val NO_ID = ":id/no-id"
    }
}