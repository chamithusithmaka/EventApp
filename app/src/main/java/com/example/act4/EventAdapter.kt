package com.example.act4

import Event
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private val context: Context,
    private var events: List<Event>,
    private val onDeleteClick: (Int) -> Unit,
    private val onUpdateClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>(), Filterable {

    private var eventsFiltered: List<Event> = events // This will hold the filtered events

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        val eventDescription: TextView = itemView.findViewById(R.id.eventDescription)
        val eventPriority: TextView = itemView.findViewById(R.id.eventPriority) // New field for priority
        val eventDeadline: TextView = itemView.findViewById(R.id.eventDeadline) // New field for deadline
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        // Bind event data to the views
        fun bind(event: Event) {
            eventName.text = event.name
            eventDate.text = event.date
            eventDescription.text = event.description
            eventPriority.text = "Priority: ${event.priority}" // Bind priority
            eventDeadline.text = "Deadline: ${event.deadline}" // Bind deadline

            // Set update button click listener
            updateButton.setOnClickListener {
                onUpdateClick(event) // Trigger the update function with the current event
            }

            // Set delete button click listener
            deleteButton.setOnClickListener {
                onDeleteClick(event.id) // Trigger the delete function with the event ID
            }
        }
    }

    // Inflate the event item layout and return a view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(eventsFiltered[position])
    }

    // Return the size of the filtered events list
    override fun getItemCount(): Int = eventsFiltered.size

    // Update the list of events and notify the adapter
    fun setEvents(newEvents: List<Event>) {
        events = newEvents
        eventsFiltered = newEvents // Update filtered events as well
        notifyDataSetChanged()
    }

    // Implementing the Filterable interface
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: List<Event> = if (constraint.isNullOrEmpty()) {
                    events // If the search is empty, return the original list
                } else {
                    val searchQuery = constraint.toString().lowercase() // Use lowercase() in Kotlin
                    events.filter { event ->
                        event.name.lowercase().contains(searchQuery) // Filter based on event name
                    }
                }

                return FilterResults().apply { values = filteredList }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                eventsFiltered = results?.values as? List<Event> ?: emptyList() // Safely cast and handle null case
                notifyDataSetChanged() // Notify the adapter of the changes
            }
        }
    }
}
