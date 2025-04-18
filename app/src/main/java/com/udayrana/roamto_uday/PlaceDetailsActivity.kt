package com.udayrana.roamto_uday

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.udayrana.roamto_uday.databinding.ActivityPlaceDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale

class PlaceDetailsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityPlaceDetailsBinding
    private lateinit var mMap: GoogleMap
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

        binding.materialToolbar.title = place!!.title
        binding.textViewDescription.text = place!!.description
        binding.textViewDate.text = "Date: ${
            SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(
                place!!.date
            )
        }"
        binding.textViewAddress.text = "Address: ${place!!.address}"
        binding.textViewCoordinates.text =
            "Coordinates: ${String.format("%.6f", place!!.latitude)}, ${
                String.format(
                    "%.6f",
                    place!!.longitude
                )
            }"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val pos = LatLng(place!!.latitude, place!!.longitude)
        Log.d("RoamTO_UDAY", "pos: ${pos}")

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.addMarker(
            MarkerOptions()
                .position(pos)
                .title(place!!.title)
                .contentDescription(place!!.description)
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15f))
    }


}