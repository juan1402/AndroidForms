package com.hurtado.validation.activity

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.hurtado.forms.validations.EmailValidation
import com.hurtado.forms.validations.FutureDateValidation
import com.hurtado.forms.validations.MinCharValidation
import com.hurtado.forms.validations.RequiredValidation

class LoginViewModel : ViewModel() {
    val usernameValidations = ObservableField(listOf(RequiredValidation(), MinCharValidation(6), EmailValidation()))
    val passwordValidations = ObservableField(listOf(RequiredValidation(), MinCharValidation(8)))
    val calendarValidations = ObservableField(FutureDateValidation())
}