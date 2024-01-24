package com.example.boris_perdija_stress_meter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment

import android.content.Context
import android.content.Intent
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class StressMeterFragment : Fragment() {
    private val imageArray = arrayOf(
        Pair(R.drawable.fish_normal017, 2),
        Pair(R.drawable.psm_alarm_clock, 10),
        Pair(R.drawable.psm_alarm_clock2, 10),
        Pair(R.drawable.psm_angry_face, 11),
        Pair(R.drawable.psm_anxious, 6),
        Pair(R.drawable.psm_baby_sleeping, 3),
        Pair(R.drawable.psm_bar, 4),
        Pair(R.drawable.psm_barbed_wire2, 9),
        Pair(R.drawable.psm_beach3, 0),
        Pair(R.drawable.psm_bird3, 5),
        Pair(R.drawable.psm_blue_drop, 3),
        Pair(R.drawable.psm_cat, 2),
        Pair(R.drawable.psm_clutter, 7),
        Pair(R.drawable.psm_clutter3, 8),
        Pair(R.drawable.psm_dog_sleeping, 2),
        Pair(R.drawable.psm_exam4, 10),
        Pair(R.drawable.psm_gambling4, 6),
        Pair(R.drawable.psm_headache, 10),
        Pair(R.drawable.psm_headache2, 11),
        Pair(R.drawable.psm_hiking3, 3),
        Pair(R.drawable.psm_kettle, 5),
        Pair(R.drawable.psm_lake3, 2),
        Pair(R.drawable.psm_lawn_chairs3, 3),
        Pair(R.drawable.psm_lonely, 13),
        Pair(R.drawable.psm_lonely2, 16),
        Pair(R.drawable.psm_mountains11, 2),
        Pair(R.drawable.psm_neutral_child, 9),
        Pair(R.drawable.psm_neutral_person2, 12),
        Pair(R.drawable.psm_peaceful_person, 3),
        Pair(R.drawable.psm_puppy, 0),
        Pair(R.drawable.psm_puppy3, 3),
        Pair(R.drawable.psm_reading_in_bed2, 4),
        Pair(R.drawable.psm_running3, 5),
        Pair(R.drawable.psm_running4, 6),
        Pair(R.drawable.psm_sticky_notes2, 11),
        Pair(R.drawable.psm_stressed_cat, 2),
        Pair(R.drawable.psm_stressed_person12, 15),
        Pair(R.drawable.psm_stressed_person3, 14),
        Pair(R.drawable.psm_stressed_person4, 15),
        Pair(R.drawable.psm_stressed_person6, 16),
        Pair(R.drawable.psm_stressed_person7, 13),
        Pair(R.drawable.psm_stressed_person8, 9),
        Pair(R.drawable.psm_talking_on_phone2, 10),
        Pair(R.drawable.psm_to_do_list, 5),
        Pair(R.drawable.psm_to_do_list3, 6),
        Pair(R.drawable.psm_wine3, 4),
        Pair(R.drawable.psm_work4, 3),
        Pair(R.drawable.psm_yoga4, 0),
    )

    private lateinit var gridImages: GridView
    private lateinit var moreImagesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_stress_meter, container, false)
        gridImages = view.findViewById<GridView>(R.id.gridImages)
        moreImagesButton = view.findViewById<Button>(R.id.moreImagesButton)

        // Set OnClickListener for moreImagesButton
        moreImagesButton.setOnClickListener {
            refreshImages()
        }

        refreshImages()

        return view
    }

    // This function handles refreshing images, either when More Images button is selected
    // or when an image is selected.
    private fun refreshImages() {
        // Generates 16 random numbers which will be used to pick from X images.
        val randomNumbers = mutableSetOf<Int>()

        while (randomNumbers.size < 16) {
            val randomNumber = Random.nextInt(0, imageArray.size)
            randomNumbers.add(randomNumber)
        }

        // Map random numbers to corresponding images from the original array
        val selectedImages = randomNumbers.map { imageArray[it].first }.toTypedArray()

        // Take the selected images and update the gridImages with ImageAdapter
        val adapter = ImageAdapter(requireContext(), selectedImages)
        gridImages.adapter = adapter

        // When an image is clicked, retrieve the selected image's number and run the AcceptImageFragment
        gridImages.setOnItemClickListener { _, _, position, _ ->
            val selectedImageNumber = imageArray[randomNumbers.elementAt(position)].second
            val selectedImageResource = selectedImages[position]

            // Start AcceptImageFragment activity here
            val intent = Intent(requireContext(), AcceptImageFragment::class.java)
            intent.putExtra("selectedImageNumber", selectedImageNumber)
            intent.putExtra("selectedImageResource", selectedImageResource)

            // We can use co-routines to delay refreshing images when a user clicks an image
            lifecycleScope.launch {
                startActivity(intent)
//                delay(1000)
//                refreshImages()
            }
        }
    }
}

// This function is responsible for creating (or reusing) ImageView instances to populate a GridView.
//  It will set the image resource, scale type, and layout parameters for each image in the grid.
class ImageAdapter(context: Context, private val images: Array<Int>) : ArrayAdapter<Int>(context, 0, images) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView = if (convertView == null) {
            ImageView(context)
        } else {
            convertView as ImageView
        }

        imageView.setImageResource(getItem(position) ?: 0)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = ViewGroup.LayoutParams(200, 200) // Set your desired image size

        return imageView
    }
}

