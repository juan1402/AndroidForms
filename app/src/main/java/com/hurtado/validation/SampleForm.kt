package com.hurtado.validation

import com.hurtado.forms.directives.FormResult

class SampleForm(
    var lastName: String,
    var name: String,
    var age: String
) : FormResult {
    constructor() : this("", "", "")
}