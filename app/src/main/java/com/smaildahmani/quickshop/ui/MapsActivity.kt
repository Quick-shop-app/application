package com.smaildahmani.quickshop.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.smaildahmani.quickshop.R
import com.smaildahmani.quickshop.databinding.ActivityMapsBinding
import com.smaildahmani.quickshop.model.Store

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private val storeLocations = listOf(
        Store("Granada Central Store", "Calle Reyes Católicos, 10, Granada, Spain", LatLng(37.1761, -3.5975)),
        Store("Alhambra Gifts", "Calle Real de la Alhambra, 3, Granada, Spain", LatLng(37.1760, -3.5881)),
        Store("Mirador Shop", "Callejón de San Nicolás, 8, Granada, Spain", LatLng(37.1799, -3.5922)),
        Store("Granada Tech Supplies", "Av. de la Constitución, 18, Granada, Spain", LatLng(37.1794, -3.6018)),
        Store("Granada Market Place", "Calle Mesones, 21, Granada, Spain", LatLng(37.1730, -3.5973)),
        Store("Zaidín Local Shop", "Calle Palencia, 15, Granada, Spain", LatLng(37.1616, -3.6055))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLocationPermission()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Map View"

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            checkLocationPermission()
        }

        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))

        for (store in storeLocations) {
            mMap.addMarker(
                MarkerOptions()
                    .position(store.coordinates)
                    .title(store.name)
                    .snippet(store.address)
            )
        }
        mMap.setOnInfoWindowClickListener { marker ->
            val gmmIntentUri = Uri.parse("google.navigation:q=${marker.position.latitude},${marker.position.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            Toast.makeText(this, "Navigating to ${marker.title}", Toast.LENGTH_SHORT).show()
            startActivity(mapIntent)
        }


        if (storeLocations.isNotEmpty()) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocations[0].coordinates, 12f))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

class CustomInfoWindowAdapter(
    private val context: Context,
) : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_info_window, null)

        val title = view.findViewById<TextView>(R.id.title)
        val snippet = view.findViewById<TextView>(R.id.snippet)

        title.text = marker.title
        snippet.text = marker.snippet

        return view
    }

}

