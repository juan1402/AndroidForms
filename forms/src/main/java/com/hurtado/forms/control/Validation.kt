package com.hurtado.forms.control

import android.content.Context
import androidx.annotation.StringRes

/**
 * Created by Juan Hurtado on 05-21-18
 *
 * Extend this class to create your own Validation Functions
 * @see com.hurtado.forms.directives.FormController
 *
 * check some built in examples in
 * @see com.hurtado.forms.control.Validation
 *
 */
open class Validation<InputType>(@StringRes private val error: Int) {

    open fun onValidate(context: Context?, value: InputType) = formatError(context, error)

    open fun formatError(context: Context?, @StringRes error: Int) = context?.getString(error)
}