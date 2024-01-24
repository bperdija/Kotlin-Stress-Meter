package com.example.boris_perdija_stress_meter

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.db.williamchart.data.DataPoint
import com.db.williamchart.view.LineChartView
import com.example.boris_perdija_stress_meter.databinding.FragmentResultsBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Math.ceil
import java.text.SimpleDateFormat
import java.util.*

var lineSet: List<Pair<String, Float>> = listOf()
var instances = 0;

// Some logic for sending data to the linechart was adopted from "Working with Charts in Kotlin"
// https://www.youtube.com/watch?v=4ou5yRJtuKU&t=1994s

class ResultsFragment : Fragment() {

    private lateinit var tableLayout: TableLayout
    private lateinit var lineChartView: LineChartView
    private lateinit var myText: TextView


    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_results, container, false)
        _binding = FragmentResultsBinding.inflate(inflater, container, false)

        // Initialize LineChartView
        lineChartView = view.findViewById(R.id.chartData)

        // Initialize TableLayout
        tableLayout = view.findViewById(R.id.tableLayout)

        // Read data from CSV and populate the table
        readDataFromCsv()

        // Animate the linechart
        binding.apply {
            lineChartView.animate(lineSet)
        }

        myText = view.findViewById(R.id.warningLabel)

        // Check if there is only one or fewer lines in the CSV file. Display warning if there is.
        if (instances <= 1) {
            myText.visibility = View.VISIBLE
        } else {
            myText.visibility = View.GONE
        }

        return view
    }

    // The readDataFromCsv() function takes in the .CSV file from the user (if it exists)
    private fun readDataFromCsv() {
        val csvFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "outputs.csv")

        if (csvFile.exists()) {
            // Count how many CSV rows you have. This will be used to space out the x-axis later
            instances = BufferedReader(FileReader(csvFile)).use { reader ->
                reader.lines().count().toInt()
            }

            // Handle the table at the bottom of the app
            val tableRowHeader = TableRow(requireContext())
            val headerDate = TextView(requireContext())
            tableRowHeader.addView(headerDate)
            val headerScore = TextView(requireContext())
            tableRowHeader.addView(headerScore)
            tableLayout.addView(tableRowHeader)

            // Create a list of dots for the lineGraph (x-axis, y-axis)
            var updatedLineSet = mutableListOf<Pair<String, Float>>()

            // Looking at your CSV file values, look at each line until it is null
            // The x-axis will be dynamically change based on how many rows there are.
            BufferedReader(FileReader(csvFile)).use { reader ->
                var line = reader.readLine() // Skip header line

                // jumper is used to determine whether the current rows x-axis value will be shown (only if it's a multiple of separator)
                var jumper = 0;
                while (line != null) {
                    // There will only be 5 x-axis labels. We can change the spacing here.
                    var seperator = (kotlin.math.ceil((instances / 10).toDouble()) * 10f)/5
                    //  println("your separator is: $seperator")
                    //  println("You have this many instances: $instances")
                    val tokens = line.split(",")
                    // For each line in the CSV, add the Data and Score to the table and line-graph respectively
                    if (tokens.size >= 2) {
                        val date = tokens[0]
                        val score = tokens[1].toFloat()

                        val tableRow = TableRow(requireContext())

                        val textDate = TextView(requireContext())
                        textDate.text = date
                        tableRow.addView(textDate)

                        val textScore = TextView(requireContext())
                        textScore.text = score.toString()
                        tableRow.addView(textScore)

                        tableLayout.addView(tableRow)

                        // Update lineSet with CSV data
                        // if the currently looked at data value is a multiple of the amount between
                        // seperated x-axis, add it to the x-axis. Otherwise, to prevent cluttering,
                        // do not add the current data value to the x-axis.
                        if ((jumper % seperator).toInt() == 0) {
                            //  println("Adding: $jumper")
                            updatedLineSet.add(jumper.toString() to score)
                        }
                        else{
                            //  println("Adding: space")
                            updatedLineSet.add("" to score)
                        }
                        lineSet = updatedLineSet.toList()
                        jumper++
                    }
                    line = reader.readLine()
                }

                // Add an extra empty row to the table (This is to avoid any data being cut off from
                // the border.
                val tableRow = TableRow(requireContext())
                val textDate = TextView(requireContext())
                textDate.text = ""
                tableRow.addView(textDate)
                val textScore = TextView(requireContext())
                textScore.text = ""
                tableRow.addView(textScore)
                tableLayout.addView(tableRow)
            }
        }
    }
}
