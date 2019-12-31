package com.hurtado.validation

import com.hurtado.forms.directives.FormResult
import java.util.*

class SampleForm(
    var time: Date,
    var date: Date,
    var switchChild: Boolean,
    var checkBox: Boolean,
    var input: String
) : FormResult {
    constructor() : this(
        Date(),
        Date(),
        false,
        false,
        ""
    )
}