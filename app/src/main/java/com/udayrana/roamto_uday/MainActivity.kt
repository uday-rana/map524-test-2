package com.udayrana.roamto_uday

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udayrana.roamto_uday.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var placeDao: PlaceDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getInstance(this)
        placeDao = db.placeDao()
    }
}