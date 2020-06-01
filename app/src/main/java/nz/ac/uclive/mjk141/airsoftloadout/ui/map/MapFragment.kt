package nz.ac.uclive.mjk141.airsoftloadout.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.fragment_map.view.*
import nz.ac.uclive.mjk141.airsoftloadout.R
import nz.ac.uclive.mjk141.airsoftloadout.data.AirsoftLocation
import nz.ac.uclive.mjk141.airsoftloadout.databinding.FragmentMapBinding
import nz.ac.uclive.mjk141.airsoftloadout.utils.LOCATION_MANAGER_MAX_DISTANCE
import nz.ac.uclive.mjk141.airsoftloadout.utils.LOCATION_MANAGER_MIN_TIME
import nz.ac.uclive.mjk141.airsoftloadout.utils.REQUEST_LOCATION_PERMISSION
import nz.ac.uclive.mjk141.airsoftloadout.utils.getJsonDataFromAssets
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private lateinit var applicationMap: GoogleMap
    private lateinit var locationManager: LocationManager

    private lateinit var binding: FragmentMapBinding
    private lateinit var mapViewModel: MapViewModel

    private val TAG = MapFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mapViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)
        binding = FragmentMapBinding
            .inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        locationManager = requireNotNull(activity).getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return binding.root
    }

    private fun loadLocations() {
        val jsonFileString = getJsonDataFromAssets(requireNotNull(activity).applicationContext,
            getString(R.string.store_locations_filename)
        )
        val gson = Gson()
        val listAirsoftLocationType = object : TypeToken<List<AirsoftLocation>>() {}.type

        val locations: List<AirsoftLocation> = gson.fromJson(jsonFileString, listAirsoftLocationType)

        locations.forEach {
            val snippet = "${it.description}\nAnd Let's test the newline"
            applicationMap.addMarker(MarkerOptions()
                .position(it.latLng)
                .title(it.name)
                .snippet(snippet)
            )
        }
    }

    private fun enableLocation() {
        if (isPermissionGranted()) {
            applicationMap.isMyLocationEnabled = true
            // Complains about missing permission
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                LOCATION_MANAGER_MIN_TIME, LOCATION_MANAGER_MAX_DISTANCE, this)
        } else {
            ActivityCompat.requestPermissions(
                requireNotNull(activity),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireNotNull(activity).application,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableLocation()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            applicationMap = it
            setMapStyle(applicationMap)
            enableLocation()
            loadLocations()
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context,
                    R.raw.map_style_dark_mode
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Could not find style. Error: ", e)
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11F)
            applicationMap.moveCamera(cameraUpdate)
            locationManager.removeUpdates(this)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) { }

    override fun onProviderDisabled(provider: String?) { }

    override fun onProviderEnabled(provider: String?) { }


    override fun onStart() {
        binding.mapView.onStart()
        super.onStart()
    }

    override fun onResume() {
        binding.mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        binding.mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        binding.mapView.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.mapView.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        binding.mapView.onLowMemory()
        super.onLowMemory()
    }
}
