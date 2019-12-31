package com.hurtado.forms.widget.controller

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import com.google.android.material.textfield.TextInputEditText
import com.hurtado.forms.control.Controller
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.FormController
import java.util.*

class TimeController(
    action: Field<Date, TextInputEditText>,
    callback: (FormController<Date, TextInputEditText>) -> Unit
) : Controller<Date, TextInputEditText>(action, callback) {

    private var selectedDate = Date()

    @SuppressLint("SetTextI18n")
    override fun onCreate() {
        action.child().setText("00:00")
        child().setOnClickListener {
            buildAppCompatDialog { date ->
                selectedDate = date
                assertFieldValidity(date)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buildAppCompatDialog(onDateSelected: (date: Date) -> Unit) {
        val currentDate = Date()
        TimePickerDialog(
            view().context,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                val time = if (minute < 10) {
                    "$hourOfDay:0$minute"
                } else {
                    "$hourOfDay:$minute"
                }

                action.child().setText(time)
                onDateSelected.invoke(calendar.time)
            }, currentDate.hours, currentDate.minutes, false
        ).show()
    }

    override fun input() = selectedDate

}