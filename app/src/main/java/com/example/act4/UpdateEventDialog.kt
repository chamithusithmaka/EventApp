package com.example.act4

import Event
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import java.util.*

class UpdateEventDialog(
    context: Context,
    private val event: Event,
    private val onUpdate: (String, String, String, Int, String) -> Unit
) {
    private val dialog: Dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.dialog_update_event)

        // Set the dialog width to match the parent width
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Initialize views
        val editTextEventName = dialog.findViewById<EditText>(R.id.editTextEventName)
        val editTextEventDate = dialog.findViewById<EditText>(R.id.editTextEventDate)
        val editTextEventDescription = dialog.findViewById<EditText>(R.id.editTextEventDescription)
        val eventPrioritySpinner = dialog.findViewById<Spinner>(R.id.eventPrioritySpinner) // Spinner for priority
        val editTextEventDeadline = dialog.findViewById<EditText>(R.id.editTextEventDeadline)
        val buttonUpdateEvent = dialog.findViewById<Button>(R.id.buttonUpdateEvent)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        // Set existing event data to EditTexts
        editTextEventName.setText(event.name)
        editTextEventDate.setText(event.date)
        editTextEventDescription.setText(event.description)
        editTextEventDeadline.setText(event.deadline)

        // Set up the Spinner for Priority (1-5)
        val priorityOptions = arrayOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, priorityOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventPrioritySpinner.adapter = adapter

        // Set current priority in Spinner
        eventPrioritySpinner.setSelection(event.priority - 1)

        // Show calendar for Event Date
        editTextEventDate.setOnClickListener {
            showDatePicker(context, editTextEventDate)
        }

        // Show calendar for Event Deadline
        editTextEventDeadline.setOnClickListener {
            showDatePicker(context, editTextEventDeadline)
        }

        // Update button click listener
        buttonUpdateEvent.setOnClickListener {
            val updatedName = editTextEventName.text.toString()
            val updatedDate = editTextEventDate.text.toString()
            val updatedDescription = editTextEventDescription.text.toString()
            val updatedPriority = eventPrioritySpinner.selectedItem.toString().toInt() // Get selected priority
            val updatedDeadline = editTextEventDeadline.text.toString()

            // Trigger the onUpdate function passed in
            onUpdate(updatedName, updatedDate, updatedDescription, updatedPriority, updatedDeadline)
            dialog.dismiss() // Dismiss dialog
        }

        // Cancel button click listener
        buttonCancel.setOnClickListener {
            dialog.dismiss() // Dismiss dialog
        }
    }

    // Function to show DatePickerDialog
    private fun showDatePicker(context: Context, editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            // Format and set the selected date in the EditText
            val selectedDate = "${selectedYear}-${selectedMonth + 1}-${selectedDay}"
            editText.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

    // Show the dialog
    fun show() {
        dialog.show()
    }
}
