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
        private const val DATABASE_VERSION = 1
        private const val TABLE_EVENTS = "events"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_DESCRIPTION = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_EVENTS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_DATE TEXT, " +
                "$COLUMN_DESCRIPTION TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        onCreate(db)
    }

    // Insert event data into the database
    fun addEvent(name: String, date: String, description: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DATE, date)
            put(COLUMN_DESCRIPTION, description)
        }

        val result = db.insert(TABLE_EVENTS, null, values)
        db.close()
        return result
    }

    // Update an existing event
    fun updateEvent(id: Int, name: String, date: String, description: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_DATE, date)
            put(COLUMN_DESCRIPTION, description)
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

    // Retrieve all events
    fun getAllEvents(): List<Event> {
        val events = mutableListOf<Event>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVENTS", null)

        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            do {
                // Safely get the column indexes
                val idIndex = cursor.getColumnIndex(COLUMN_ID)
                val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
                val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
                val descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION)

                // Check if all indexes are valid (≥ 0)
                if (idIndex >= 0 && nameIndex >= 0 && dateIndex >= 0 && descriptionIndex >= 0) {
                    val id = cursor.getInt(idIndex)
                    val name = cursor.getString(nameIndex)
                    val date = cursor.getString(dateIndex)
                    val description = cursor.getString(descriptionIndex)
                    events.add(Event(id, name, date, description))
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
