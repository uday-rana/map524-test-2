package com.udayrana.roamto_uday

import android.location.Location
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
    val location: Location,
) : Serializable
