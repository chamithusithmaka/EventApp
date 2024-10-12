package com.example.act4

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.*

class AddEvent : AppCompatActivity() {

    private lateinit var eventName: EditText
    private lateinit var eventDate: EditText
    private lateinit var eventDescription: EditText
    private lateinit var eventPrioritySpinner: Spinner
    private lateinit var eventDeadline: EditText
    private lateinit var addEventButton: Button
    private lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        // Initialize views
        eventName = findViewById(R.id.eventName)
        eventDate = findViewById(R.id.eventDate)
        eventDescription = findViewById(R.id.eventDescription)
        eventPrioritySpinner = findViewById(R.id.eventPrioritySpinner)
        eventDeadline = findViewById(R.id.eventDeadline)
        addEventButton = findViewById(R.id.addEventButton)
        clearButton = findViewById(R.id.clearButton)

        // Set up DatePicker for Event Date
        eventDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                eventDate.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }

        // Set up DatePicker for Deadline
        eventDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                eventDeadline.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }

        // Set up the Spinner for Priority (1-5)
        val priorityOptions = arrayOf("1", "2", "3", "4", "5")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventPrioritySpinner.adapter = adapter

        // Add event button click listener
        addEventButton.setOnClickListener {
            // Get input values
            val name = eventName.text.toString()
            val date = eventDate.text.toString()
            val description = eventDescription.text.toString()
            val priority = eventPrioritySpinner.selectedItem.toString().toInt()  // Get selected priority
            val deadline = eventDeadline.text.toString()

            // Perform input validation
            if (name.isNotEmpty() && date.isNotEmpty() && description.isNotEmpty()) {
                val eventDatabaseHelper = EventDatabaseHelper(this)

                // Save event to SQLite database
                val result = eventDatabaseHelper.addEvent(name, date, description, priority, deadline)

                if (result != -1L) {
                    Toast.makeText(this, "Event added successfully!", Toast.LENGTH_SHORT).show()
                    // Clear input fields after successful addition (optional)
                    eventName.text.clear()
                    eventDate.text.clear()
                    eventDescription.text.clear()
                    eventDeadline.text.clear()
                } else {
                    Toast.makeText(this, "Error adding event. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }

        // Clear button click listener
        clearButton.setOnClickListener {
            eventName.text.clear()
            eventDate.text.clear()
            eventDescription.text.clear()
            eventDeadline.text.clear()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
