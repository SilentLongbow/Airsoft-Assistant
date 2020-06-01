package nz.ac.uclive.mjk141.airsoftloadout.data

import com.google.android.gms.maps.model.LatLng

data class AirsoftLocation(val name: String, val latLng: LatLng, val address: Address, val description: String) {}