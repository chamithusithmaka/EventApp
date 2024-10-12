package com.example.act4

import Event
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class EventDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "events.db"
        private const val DATABASE_VERSION = 2 // Incremented version
        private const val TABLE_EVENTS = "events"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_PRIORITY = "priority" // New column for priority
        private const val COLUMN_DEADLINE = "deadline" // New column for deadline
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_EVENTS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_PRIORITY INTEGER, " + // Priority as an integer
                "$COLUMN_DEADLINE TEXT)") // Deadline as text (could be a date format)
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_EVENTS ADD COLUMN $COLUMN_PRIORITY INTEGER")
            db?.execSQL("ALTER TABLE $TABLE_EVENTS ADD COLUMN $COLUMN_DEADLINE TEXT")
        }
    }

    // Insert event data into the database
    fun addEvent(name: String, date: String, description: String, priority: Int, deadline: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DATE, date)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_PRIORITY, priority) // Add priority
            put(COLUMN_DEADLINE, deadline) // Add deadline
        }

        val result = db.insert(TABLE_EVENTS, null, values)
        db.close()
        return result
    }

    // Update an existing event
    fun updateEvent(id: Int, name: String, date: String, description: String, priority: Int, deadline: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DATE, date)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_PRIORITY, priority) // Update priority
            put(COLUMN_DEADLINE, deadline) // Update deadline
        }

        val result = db.update(TABLE_EVENTS, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Delete an event
    fun deleteEvent(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_EVENTS, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // Retrieve all events with optional sorting/filtering
    fun getAllEvents(sortBy: String? = null): List<Event> {
        val events = mutableListOf<Event>()
        val db = this.readableDatabase
        val query = if (sortBy != null) {
            "SELECT * FROM $TABLE_EVENTS ORDER BY $sortBy"
        } else {
            "SELECT * FROM $TABLE_EVENTS"
        }
        val cursor = db.rawQuery(query, null)

        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(COLUMN_ID)
                val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
                val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
                val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)
                val priorityIndex = cursor.getColumnIndex(COLUMN_PRIORITY)
                val deadlineIndex = cursor.getColumnIndex(COLUMN_DEADLINE)

                if (idIndex >= 0 && nameIndex >= 0 && dateIndex >= 0 && descriptionIndex >= 0 &&
                    priorityIndex >= 0 && deadlineIndex >= 0) {
                    val id = cursor.getInt(idIndex)
                    val name = cursor.getString(nameIndex)
                    val date = cursor.getString(dateIndex)
                    val description = cursor.getString(descriptionIndex)
                    val priority = cursor.getInt(priorityIndex)
                    val deadline = cursor.getString(deadlineIndex)
                    events.add(Event(id, name, date, description, priority, deadline))
                } else {
                    Log.e("EventDatabaseHelper", "Invalid column index detected.")
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return events
    }
}
