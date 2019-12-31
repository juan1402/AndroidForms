package com.hurtado.forms.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.FormController
import com.hurtado.forms.validations.ValidationHelper
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class FormField<InputType, Child : View>(
    context: Context,
    attrs: AttributeSet,
    private val clazz: KClass<Child>
) : TextInputLayout(context, attrs), Field<InputType, Child> {

    private var validation = ValidationHelper<InputType>().build(context, attrs)
    private var child = WeakReference<Child>(null)

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
