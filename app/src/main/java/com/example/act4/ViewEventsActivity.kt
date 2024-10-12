package com.example.act4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.EditText

class ViewEventsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventDatabaseHelper: EventDatabaseHelper
    private lateinit var searchEditText: EditText // Declare EditText for search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)

        eventDatabaseHelper = EventDatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText) // Initialize the EditText

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch all events from the database
        val events = eventDatabaseHelper.getAllEvents()
        eventAdapter = EventAdapter(this, events, { eventId ->
            // Handle delete event
            eventDatabaseHelper.deleteEvent(eventId)
            refreshEvents()
        }, { event ->
            // Handle update event
            val updateDialog = UpdateEventDialog(this, event) { updatedName, updatedDate, updatedDescription, updatedPriority, updatedDeadline ->
                // Update the event in the database
                eventDatabaseHelper.updateEvent(event.id, updatedName, updatedDate, updatedDescription, updatedPriority, updatedDeadline)
                refreshEvents()
            }
            updateDialog.show()
        })

        recyclerView.adapter = eventAdapter

        // Add TextWatcher to the search EditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                eventAdapter.filter.filter(s) // Call filter on the adapter's filter
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun refreshEvents() {
        val events = eventDatabaseHelper.getAllEvents()
        eventAdapter.setEvents(events)
    }
}
