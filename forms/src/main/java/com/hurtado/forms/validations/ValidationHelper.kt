package com.hurtado.forms.validations

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import com.hurtado.forms.R
import com.hurtado.forms.control.Validation

class ValidationHelper<InputType> {

    fun build(context: Context, attrs: AttributeSet?) =
        buildRequirements(
            context.obtainStyledAttributes(
                attrs,
                R.styleable.FormField,
                0,
                0
            )
        )

    private fun buildRequirements(typeArray: TypedArray): ArrayList<Validation<InputType>> {
        val validation = ArrayList<Validation<InputType>>()
        val requirementArray = typeArray.getTextArray(R.styleable.FormField_validations)

        requirementArray?.forEach { safeRequirement ->
            if (safeRequirement.isNotEmpty())
                buildRequirements(
                    safeRequirement.toString()
                )?.let {
                    validation.add(it)
                }
        }

        typeArray.recycle()
        return validation
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildRequirements(requirementClass: String): Validation<InputType>? {
        var retVal: Validation<InputType>? = null
        try {
            val requirement = Class.forName(
                requirementClass, true, this.javaClass.classLoader
            ).newInstance()
            if (requirement is Validation<*>)
                retVal = requirement as Validation<InputType>?
        } catch (e: ClassNotFoundException) {
            Log.e(
                ValidationHelper::class.java.name,
                "Requirement not found, please check your validations array"
            )
        } catch (e: ClassCastException) {
            Log.e(
                ValidationHelper::class.java.name,
                "Class cast exception, please check your " +
                        "validations InputType match with your field InputType"
            )
        } catch (e: Exception) {
            Log.e(
                ValidationHelper::class.java.name,
                "Unknown error we are sorry we were not able to foresee this exception," +
                        " please make sure you are following our documentation correctly, please " +
                        "submit a bug on our github page after you fix this error we would " +
                        "really appreciate it, please include exception cause"
            )
        } finally {
            return retVal
        }
    }
}