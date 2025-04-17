package com.udayrana.roamto_uday

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.udayrana.roamto_uday.databinding.ActivityPlaceDetailsBinding

class PlaceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceDetailsBinding
    private var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonBack.setOnClickListener { finish() }

        place = intent.getSerializableExtra("place") as? Place

        if (place == null) {
            Log.e("RoamTO_UDAY", "Received null place")
            finish()
            return
        }

        binding.textViewTitle.text = "Title: ${place!!.title}"
        binding.textViewDescription.text = "Description: ${place!!.description}"
        binding.textViewDate.text = "Date: ${place!!.date}"
        binding.textViewAddress.text = "Address: ${place!!.address}"
        binding.textViewCoordinates.text = "Coordinates: ${place!!.latitude}, ${place!!.longitude}"

    }
}