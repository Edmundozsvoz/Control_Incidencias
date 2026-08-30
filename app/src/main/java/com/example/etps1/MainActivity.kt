package com.example.etps1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val statusText = findViewById<TextView>(R.id.statusText)
        findViewById<Button>(R.id.registerButton).setOnClickListener {
            statusText.text = getString(R.string.incident_registered)
            statusText.setTextColor(getColor(R.color.success_green))
            Toast.makeText(this, R.string.saved_message, Toast.LENGTH_SHORT).show()
        }
    }
}
