package com.example.act4

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

class AddEvent : AppCompatActivity() {

    private lateinit var eventName: EditText
    private lateinit var eventDate: EditText
    private lateinit var eventDescription: EditText
    private lateinit var addEventButton: Button
    private lateinit var clearButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_event)

        // Initialize views
        eventName = findViewById(R.id.eventName)
        eventDate = findViewById(R.id.eventDate)
        eventDescription = findViewById(R.id.eventDescription)
        addEventButton = findViewById(R.id.addEventButton)
        clearButton = findViewById(R.id.clearButton)

        // Set up DatePicker for Event Date
        eventDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Format selected date and set to eventDate EditText
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                eventDate.setText(formattedDate)
            }, year, month, day)

            datePicker.show()
        }

        // Add event button click listener
        addEventButton.setOnClickListener {
            // Get input values
            val name = eventName.text.toString()
            val date = eventDate.text.toString()
            val description = eventDescription.text.toString()

            // Perform input validation and save to SQLite
            if (name.isNotEmpty() && date.isNotEmpty() && description.isNotEmpty()) {
                // Save event to SQLite database (you'll add this logic later)
            } else {
                // Show error message
            }
        }

        // Clear button click listener
        clearButton.setOnClickListener {
            eventName.text.clear()
            eventDate.text.clear()
            eventDescription.text.clear()


            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }
}