package com.example.boris_perdija_stress_meter

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AcceptImageFragment : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_accept_image)

        val btnSave: Button = findViewById(R.id.acceptButton)
        val btnCancel: Button = findViewById(R.id.cancelButton)
        val myImage: ImageView = findViewById(R.id.myImage)

        val selectedImageResource = intent.getIntExtra("selectedImageResource", 0)
        val selectedImageNumber = intent.getIntExtra("selectedImageNumber", 0)

        myImage.setImageResource(selectedImageResource)

        btnSave.setOnClickListener {
            writeToCsv(selectedImageNumber)
            // finish()
           finishAffinity()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun writeToCsv(score: Int) {
        val csvFile = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "outputs.csv")

        if (!csvFile.exists()) {
            csvFile.createNewFile()
        }

        val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time)

        FileWriter(csvFile, true).use { writer ->
            writer.append("$timeStamp,$score\n")
        }
    }
}




