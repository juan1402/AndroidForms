package com.hurtado.forms.widget

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import com.google.android.material.textfield.TextInputLayout
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation
import java.util.*

open class FormField(context: Context, attrs: AttributeSet?) : TextInputLayout(context, attrs) {

    val validation = ArrayList<Validation>()

    init {
        initStyledAttributes(
            context.obtainStyledAttributes(
                attrs, R.styleable.FormField, 0, 0
            )
        )
    }

    private fun initStyledAttributes(typeArray: TypedArray) {

        val requirementArray = typeArray.getTextArray(R.styleable.FormField_validations)

        requirementArray?.forEach { safeRequirement ->
            if (safeRequirement.isNotEmpty())
                buildRequirement(safeRequirement.toString())
        }

        typeArray.recycle()
    }

    private fun buildRequirement(requirementClass: String) {
        try {
            val requirement = Class.forName(
                requirementClass, true, this.javaClass.classLoader
            ).newInstance()
            if (requirement is Validation) validation.add(requirement)
        } catch (e: Exception) {
            Log.e(
                FormField::class.java.name,
                "Requirement not found, please check your validations array"
            )
        }
    }

}