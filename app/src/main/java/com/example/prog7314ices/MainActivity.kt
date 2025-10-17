package com.example.prog7314ices

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.prog7314ices.network.GeoPositionDto
import com.example.prog7314ices.network.RetrofitClient
import com.example.prog7314ices.ui.SectionsPagerAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val sharedLocationVM: com.example.prog7314ices.ui.SharedLocationViewModel by viewModels()
    private lateinit var fused: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tabs + pager
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val adapter = SectionsPagerAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()

        // Location client + permission flow
        fused = LocationServices.getFusedLocationProviderClient(this)
        checkLocationPermissionAndFetch()
    }

    private fun checkLocationPermissionAndFetch() {
        val fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMS
            )
            return
        }

        fused.lastLocation
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    val latLon = "${loc.latitude},${loc.longitude}"
                    RetrofitClient.api.getLocationKeyByGeo(
                        apiKey = "zpka_c6f2c81a5688443aad64355246cf2029_a1526508",
                        latCommaLon = latLon
                    ).enqueue(object : Callback<GeoPositionDto> {
                        override fun onResponse(call: Call<GeoPositionDto>, response: Response<GeoPositionDto>) {
                            val key = response.body()?.Key
                            if (response.isSuccessful && !key.isNullOrBlank()) {
                                sharedLocationVM.updateLocationKey(key) // publish to tabs
                                println("Resolved locationKey: $key")
                            } else {
                                println("Geo resolve failed; using default Durban.")
                            }
                        }
                        override fun onFailure(call: Call<GeoPositionDto>, t: Throwable) {
                            println("Geo resolve error: ${t.message}; using default Durban.")
                        }
                    })
                } else {
                    println("Last location is null (emulator may need a mock location).")
                }
            }
            .addOnFailureListener { err ->
                println("Failed to get last location: ${err.message}")
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMS && grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
            checkLocationPermissionAndFetch()
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMS = 1001
    }
}
