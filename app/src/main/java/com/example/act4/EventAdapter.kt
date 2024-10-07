// EventAdapter.kt
package com.example.act4

import Event
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(
    private val context: Context,
    private var events: List<Event>,
    private val onDeleteClick: (Int) -> Unit,
    private val onUpdateClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventDate: TextView = itemView.findViewById(R.id.eventDate)
        val eventDescription: TextView = itemView.findViewById(R.id.eventDescription)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(event: Event) {
            eventName.text = event.name
            eventDate.text = event.date
            eventDescription.text = event.description

            updateButton.setOnClickListener {
                onUpdateClick(events[adapterPosition]) // Trigger the update function with the current event
            }

            deleteButton.setOnClickListener {
                onDeleteClick(event.id) // Trigger the delete function with the event ID
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    fun setEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}