package com.hurtado.validation.model

import com.hurtado.forms.directives.FormResult
import java.util.Date

data class LoginFormResult(
        var username: String,
        var password: String,
        var birthDate: Date
) : FormResult {
    constructor() : this(String(), String(), Date())
}