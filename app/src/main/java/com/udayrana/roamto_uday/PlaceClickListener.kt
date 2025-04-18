package com.udayrana.roamto_uday

interface PlaceClickListener {
    fun displayPlaceDetails(place: Place)
    fun deletePlace(place: Place)
    fun editPlace(place: Place)
}