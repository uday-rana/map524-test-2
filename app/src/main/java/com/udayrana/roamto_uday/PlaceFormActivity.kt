package com.udayrana.roamto_uday

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.udayrana.roamto_uday.databinding.ActivityPlaceFormBinding
import kotlinx.coroutines.launch
import java.util.Locale

class PlaceFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceFormBinding
    private lateinit var placeDao: PlaceDao
    private lateinit var geocoder: Geocoder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeDao = AppDatabase.getInstance(this).placeDao()
        geocoder = Geocoder(applicationContext, Locale.getDefault())

        binding.buttonCancel.setOnClickListener { finish() }
        binding.buttonSubmitForm.setOnClickListener { submitForm() }
    }

    private fun submitForm() {
        binding.textInputLayoutTitle.error = null
        binding.textInputLayoutDescription.error = null
        binding.textInputLayoutDate.error = null
        binding.textInputLayoutAddress.error = null
        var error = false

        val titleInput = binding.editTextTitle.text.toString().trim()
        val descriptionInput = binding.editTextDescription.text.toString().trim()
        val dateInput = binding.editTextDate.text.toString().trim()
        val addressInput = binding.editTextAddress.text.toString().trim()


        if (titleInput.isBlank()) {
            binding.textInputLayoutTitle.error = "Enter title"
            error = true
        }
        if (descriptionInput.isBlank()) {
            binding.textInputLayoutDescription.error = "Enter description"
            error = true
        }
        if (addressInput.isBlank()) {
            binding.textInputLayoutAddress.error = "Enter address"
            error = true
        }
        if (dateInput.isBlank()) {
            binding.textInputLayoutAddress.error = "Enter address"
            error = true
        }
        if (error) {
            return
        }

        val foundAddress: Address? = try {
            val searchResults: MutableList<Address>? = geocoder.getFromLocationName(addressInput, 1)
            when {
                searchResults == null -> {
                    binding.textViewGeocoderError.text =
                        getString(R.string.could_not_connect_to_geocoder_services)
                    null
                }

                searchResults.isEmpty() -> {
                    binding.textViewGeocoderError.text = getString(R.string.no_coordinates_found)
                    null
                }

                else -> searchResults[0]
            }
        } catch (ex: Exception) {
            Log.e("RoamTO_UDAY", "Error: ", ex)
            null
        }

        if (foundAddress == null) return

        val place = Place(
            uid = 0,
            title = titleInput,
            description = descriptionInput,
            address = addressInput,
            date = dateInput,
            longitude = foundAddress.longitude,
            latitude = foundAddress.latitude
        )

        lifecycleScope.launch {
            placeDao.insert(place)
        }
        Toast.makeText(
            this@PlaceFormActivity,
            "Added ${place.title}",
            Toast.LENGTH_LONG
        ).show()
        finish()


    }
}