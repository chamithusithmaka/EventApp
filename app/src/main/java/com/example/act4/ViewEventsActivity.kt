package com.example.act4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewEventsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventDatabaseHelper: EventDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_events)

        eventDatabaseHelper = EventDatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch all events from the database
        val events = eventDatabaseHelper.getAllEvents()
        eventAdapter = EventAdapter(this, events, { eventId ->
            // Handle delete event
            eventDatabaseHelper.deleteEvent(eventId)
            refreshEvents()
        }, { event ->
            // Handle update event using custom dialog with priority and deadline
            val updateDialog = UpdateEventDialog(this, event) { updatedName, updatedDate, updatedDescription, updatedPriority, updatedDeadline ->
                // Update the event in the database
                eventDatabaseHelper.updateEvent(event.id, updatedName, updatedDate, updatedDescription, updatedPriority, updatedDeadline)
                // Refresh the events list
                refreshEvents()
            }
            updateDialog.show() // Show the update dialog
        })

        recyclerView.adapter = eventAdapter
    }

    private fun refreshEvents() {
        val events = eventDatabaseHelper.getAllEvents()
        eventAdapter.setEvents(events)
    }
}
