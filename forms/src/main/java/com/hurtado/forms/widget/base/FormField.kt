package com.hurtado.forms.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.hurtado.forms.control.Validation
import com.hurtado.forms.directives.Field
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class FormField<InputType, Child : View>(
        context: Context,
        attrs: AttributeSet,
        private val clazz: KClass<Child>
) : TextInputLayout(context, attrs), Field<InputType, Child> {

    private var validation = ArrayList<Validation<InputType>>()
    private var child = WeakReference<Child>(null)

    fun validate(validations: List<Validation<InputType>>) {
        this.validation.addAll(validations)
    }

    fun validate(validation: Validation<InputType>) {
        this.validation.add(validation)
    }

    override fun clearValidations() = validation.clear()

    override fun validations() = validation

    override fun view() = this

    override fun child() = child.get()
            ?: throw RuntimeException("You need to add a ${clazz.simpleName} as a child")

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        child?.let {
            if (clazz == it::class)
                this.child = WeakReference(it as Child)
        }
    }
}
