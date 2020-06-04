package com.hurtado.forms.widget.controller

import android.app.AlertDialog
import android.widget.CalendarView
import com.google.android.material.textfield.TextInputEditText
import com.hurtado.forms.control.Controller
import com.hurtado.forms.directives.Field
import com.hurtado.forms.directives.ChangeListener
import java.text.SimpleDateFormat
import java.util.*

class CalendarController(
    action: Field<Date, TextInputEditText>,
    listener: ChangeListener
) : Controller<Date, TextInputEditText>(action, listener) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private var selectedDate = Date()

    override fun onCreate() {
        action.child().setText(
            dateFormat.format(Date())
        )
        child().setOnClickListener {
            buildAppCompatDialog {
                selectedDate = it
                assertFieldValidity(it)
                action.child().setText(
                    dateFormat.format(it)
                )
                assertFieldValidity(it)
            }
        }
    }

    private fun buildAppCompatDialog(onDateSelected: (time: Date) -> Unit) {
        val datePicker = CalendarView(view().context)
        datePicker.date = datePicker.date

        datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            onDateSelected.invoke(calendar.time)
        }

        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(
            view().context
        ).apply {
            setView(datePicker)
            setPositiveButton(android.R.string.ok) { _, _ ->
                dialog?.dismiss()
            }
            setNegativeButton(android.R.string.cancel) { _, _ ->
                dialog?.dismiss()
            }
        }

        dialog = builder.create()
        dialog.show()
    }

    override fun input() = selectedDate
}