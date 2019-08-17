package com.hurtado.forms.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputLayout
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.*

class FormField(context: Context, attrs: AttributeSet?) : TextInputLayout(context, attrs) {

    val validation = ArrayList<Validation>()

    init {
        initStyledAttributes(context.obtainStyledAttributes(
                attrs, R.styleable.FormField, 0, 0))
    }

    private fun initStyledAttributes(typeArray: TypedArray) {

        val requirementArray = typeArray.getTextArray(R.styleable.FormField_validations)

        requirementArray?.forEach { safeRequirement ->
            if (safeRequirement.isNotEmpty())
                buildRequirement(safeRequirement.toString())
        }

        typeArray.recycle()
    }

    private fun isClassPresent(candidate: String) = Class.forName(
        candidate, true, this.javaClass.classLoader).newInstance()


    private fun buildRequirement(requirementClass: String) {
        val requirement = isClassPresent(requirementClass)
        if (requirement is Validation) validation.add(requirement)
    }

}