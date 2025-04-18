package com.udayrana.roamto_uday

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.udayrana.roamto_uday.databinding.ActivityPlaceFormBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaceFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceFormBinding
    private lateinit var placeDao: PlaceDao
    private lateinit var geocoder: Geocoder
    private var placeToEdit: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeDao = AppDatabase.getInstance(this).placeDao()
        geocoder = Geocoder(applicationContext, Locale.getDefault())

        binding.buttonCancel.setOnClickListener { finish() }
        binding.buttonSubmitForm.setOnClickListener { submitForm() }

        placeToEdit = intent.getSerializableExtra("place") as? Place

        if (placeToEdit != null) {
            binding.editTextTitle.setText(placeToEdit!!.title)
            binding.editTextDescription.setText(placeToEdit!!.description)
            binding.editTextDate.setText(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    placeToEdit!!.date
                )
            )
            binding.editTextAddress.setText(placeToEdit!!.address)
            binding.materialToolbar.title = "Edit Place"
        } else {
            binding.materialToolbar.title = "Add a Place"
        }
    }

    private fun submitForm() {
        binding.textInputLayoutTitle.error = null
        binding.textInputLayoutDescription.error = null
        binding.textInputLayoutDate.error = null
        binding.textInputLayoutAddress.error = null
        binding.textViewGeocoderError.visibility = View.GONE
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
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.isLenient = false
        val date: Date? = try {
            sdf.parse(dateInput)
        } catch (ex: Exception) {
            binding.textInputLayoutDate.error =
                "Date must be in the format YYYY-MM-DD (e.g. 2025-04-17)"
            error = true
            null
        }

        if (error) {
            return
        }

        var foundAddress: Address?
        var geocoderErrorMessage: String? = null

        try {
            val searchResults: MutableList<Address>? = geocoder.getFromLocationName(addressInput, 1)

            if (searchResults == null) {
                geocoderErrorMessage = getString(R.string.could_not_connect_to_geocoder_services)
                foundAddress = null
            } else if (searchResults.isEmpty()) {
                geocoderErrorMessage = getString(R.string.no_coordinates_found)
                foundAddress = null
            } else {
                foundAddress = searchResults[0]
            }
        } catch (ex: Exception) {
            Log.e("RoamTO_UDAY", "Error: ", ex)
            geocoderErrorMessage = getString(R.string.could_not_connect_to_geocoder_services)
            foundAddress = null
        }

        if (foundAddress == null) {
            binding.textViewGeocoderError.visibility = View.VISIBLE
            binding.textViewGeocoderError.text = geocoderErrorMessage
            return
        }

        val place = Place(
            uid = placeToEdit?.uid ?: 0,
            title = titleInput,
            description = descriptionInput,
            address = addressInput,
            date = date!!,
            longitude = foundAddress.longitude,
            latitude = foundAddress.latitude
        )

        if (placeToEdit != null) {
            lifecycleScope.launch {
                placeDao.update(place)
            }
        } else {
            lifecycleScope.launch {
                placeDao.insert(place)
            }
        }

        val action = if (placeToEdit != null) "Updated" else "Added"
        Toast.makeText(
            this@PlaceFormActivity,
            "$action ${place.title}",
            Toast.LENGTH_LONG
        ).show()
        finish()
    }
}