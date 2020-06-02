package com.hurtado.forms.utils

import android.view.View
import androidx.databinding.BindingAdapter
import com.hurtado.forms.control.Validation
import com.hurtado.forms.widget.base.FormField

object FormBinding {
    @JvmStatic
    @BindingAdapter("validate")
    fun <K, T: View> FormField<K, T>.validate(validations: List<Validation<K>>) {
        this.validate(validations)
    }

    @JvmStatic
    @BindingAdapter("validate")
    fun <K, T: View> FormField<K, T>.validate(validations: Validation<K>) {
        this.validate(validations)
    }
}