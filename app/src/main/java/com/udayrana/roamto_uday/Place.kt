package com.udayrana.roamto_uday

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity
data class Place(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    val title: String,
    val description: String,
    val date: Date,
    val address: String,
    val latitude: Double,
    val longitude: Double
) : Serializable
