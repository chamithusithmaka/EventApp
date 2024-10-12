package com.example.act4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)


        // Initialize buttons
        val buttonEvents = findViewById<Button>(R.id.buttonEvents)
        val buttonAddEvent = findViewById<Button>(R.id.buttonAddEvent)

        // Set click listeners for buttons
        buttonEvents.setOnClickListener {
            // Navigate to ViewEventsActivity
            val intent = Intent(this, ViewEventsActivity::class.java)
            startActivity(intent)
        }

        buttonAddEvent.setOnClickListener {
            // Navigate to AddEventActivity
            val intent = Intent(this, AddEvent::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}