package com.example.act4

import Event
import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText

class UpdateEventDialog(
    context: Context,
    private val event: Event,
    private val onUpdate: (String, String, String) -> Unit
) {
    private val dialog: Dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.dialog_update_event)

        // Initialize views
        val editTextEventName = dialog.findViewById<EditText>(R.id.editTextEventName)
        val editTextEventDate = dialog.findViewById<EditText>(R.id.editTextEventDate)
        val editTextEventDescription = dialog.findViewById<EditText>(R.id.editTextEventDescription)
        val buttonUpdateEvent = dialog.findViewById<Button>(R.id.buttonUpdateEvent)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        // Set existing event data to EditTexts
        editTextEventName.setText(event.name)
        editTextEventDate.setText(event.date)
        editTextEventDescription.setText(event.description)

        // Update button click listener
        buttonUpdateEvent.setOnClickListener {
            val updatedName = editTextEventName.text.toString()
            val updatedDate = editTextEventDate.text.toString()
            val updatedDescription = editTextEventDescription.text.toString()
            onUpdate(updatedName, updatedDate, updatedDescription) // Call the update function
            dialog.dismiss() // Dismiss dialog
        }

        // Cancel button click listener
        buttonCancel.setOnClickListener {
            dialog.dismiss() // Dismiss dialog
        }
    }

    // Show the dialog
    fun show() {
        dialog.show()
    }
}
