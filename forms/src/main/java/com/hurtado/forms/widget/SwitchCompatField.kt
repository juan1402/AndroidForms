package com.hurtado.forms.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.hurtado.forms.directives.ChangeListener
import com.hurtado.forms.widget.base.FormField
import com.hurtado.forms.widget.controller.SwitchController

class SwitchCompatField(context: Context, attrs: AttributeSet)
    : FormField<Boolean, SwitchCompat>(context, attrs, SwitchCompat::class) {
    override fun controller(l: ChangeListener) = SwitchController(this, l)
}
