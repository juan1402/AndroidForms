package com.hurtado.forms.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatCheckBox
import com.hurtado.forms.directives.FormController
import com.hurtado.forms.widget.base.FormField
import com.hurtado.forms.widget.controller.CheckBoxController

class CheckBoxCompatField(
    context: Context,
    attrs: AttributeSet
) : FormField<Boolean, AppCompatCheckBox>(context, attrs, AppCompatCheckBox::class) {
    override fun controller(host: (FormController<Boolean, AppCompatCheckBox>) -> Unit) =
        CheckBoxController(this, host)
}
