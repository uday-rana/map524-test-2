package com.udayrana.roamto_uday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udayrana.roamto_uday.databinding.ActivityPlaceFormBinding

class PlaceFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaceFormBinding
    private lateinit var placeDao: PlaceDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeDao = AppDatabase.getInstance(this).placeDao()

        binding.buttonCancel.setOnClickListener { finish() }
    }
}