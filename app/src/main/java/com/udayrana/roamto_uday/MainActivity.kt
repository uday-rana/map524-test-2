package com.udayrana.roamto_uday

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.udayrana.roamto_uday.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), PlaceClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var placeDao: PlaceDao
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var placeList: MutableList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        placeList = mutableListOf()
        placeAdapter = PlaceAdapter(placeList, this)
        binding.recyclerViewPlaceList.adapter = placeAdapter
        binding.recyclerViewPlaceList.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPlaceList.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

        placeDao = AppDatabase.getInstance(this).placeDao()

        binding.buttonAddPlace.setOnClickListener { addPlace() }

        refreshPlaceRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        refreshPlaceRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshPlaceRecyclerView() {
        lifecycleScope.launch {
            val placeListFromRoom = placeDao.getAll().toMutableList()
            placeList.clear()
            placeList.addAll(placeListFromRoom)
            placeAdapter.notifyDataSetChanged()
        }
    }

    private fun addPlace() {
        startActivity(Intent(this@MainActivity, PlaceFormActivity::class.java))
    }

    override fun displayPlaceDetails(place: Place) {
        val displayPlaceDetailsIntent = Intent(this@MainActivity, PlaceDetailsActivity::class.java)
        displayPlaceDetailsIntent.putExtra("place", place)
        startActivity(displayPlaceDetailsIntent)
    }

    override fun deletePlace(place: Place) {
        MaterialAlertDialogBuilder(this@MainActivity).setTitle("Delete place?")
            .setMessage("Are you sure you want to delete ${place.title}?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton("Delete") { dialog, _ ->
                lifecycleScope.launch {
                    placeDao.delete(place)
                    Toast.makeText(
                        this@MainActivity,
                        "Deleted ${place.title}",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    refreshPlaceRecyclerView()
                }
                dialog.dismiss()
            }
            .show()
    }

    override fun editPlace(place: Place) {
        val editPlaceIntent = Intent(this@MainActivity, PlaceFormActivity::class.java)
        editPlaceIntent.putExtra("place", place)
        startActivity(editPlaceIntent)
    }
}