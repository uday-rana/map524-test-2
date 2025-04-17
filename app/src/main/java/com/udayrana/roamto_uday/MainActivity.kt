package com.udayrana.roamto_uday

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.recyclerViewPlaceList.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))

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
            val bookListFromRoom = placeDao.getAll().toMutableList()
            placeList.clear()
            placeList.addAll(bookListFromRoom)
            placeAdapter.notifyDataSetChanged()
        }
    }

    private fun addPlace() {
        startActivity(Intent(this@MainActivity, PlaceFormActivity::class.java))
    }

    override fun displayPlaceDetails(place: Place) {
        startActivity(Intent(this@MainActivity, PlaceDetailsActivity::class.java))
    }
}