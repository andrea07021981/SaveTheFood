package com.example.savethefood.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.savethefood.BaseFragment
import com.example.savethefood.R
import com.example.savethefood.databinding.FragmentMapBinding
import com.example.savethefood.login.LoginFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : BaseFragment<MapViewModel, FragmentMapBinding>() {
    //TODO This class allows the user to save local places where he found particular food  Then we can use geofence to let him know when he's near
    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private val TAG = MapFragment::class.java.name
    }

    override val viewModel by viewModels<MapViewModel>()

    override val layoutRes: Int
        get() = R.layout.fragment_map

    override val classTag: String
        get() = MapFragment::class.java.simpleName

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationGranted: Boolean = false
    private lateinit var map: GoogleMap
    private lateinit var callback: OnMapReadyCallback

    override fun activateObservers() {

    }

    override fun init() {
        with(dataBinding) {
            mapViewModel = viewModel
        }
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
            requireNotNull(activity))

        // Prepare Map callback SAM
        callback = OnMapReadyCallback { googleMap ->
            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            map = googleMap
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireNotNull(activity), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationGranted = true;
            updateMap()
        } else {
            ActivityCompat.requestPermissions(requireNotNull(activity), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        locationGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationGranted = true
                    updateMap()
                }
            }
        }
    }

    private fun updateMap() {
        try {
            if (locationGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireNotNull(activity)) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val myLocation = LatLng(
                                lastKnownLocation.latitude,
                                lastKnownLocation.longitude)
                            map.addMarker(MarkerOptions().position(myLocation).title("Marker in Sydney"))
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10F))
                        }
                    } else {
                        /*Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false*/
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}