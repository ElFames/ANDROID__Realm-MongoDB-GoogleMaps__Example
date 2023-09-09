package com.example.descubertorr.ui.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.descubertorr.R
import com.example.descubertorr.data.ServiceLocator
import com.example.descubertorr.databinding.FragmentMapBinding
import com.example.descubertorr.ui.models.Site
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

const val REQUEST_CODE_LOCATION = 100

class MapFragment(private val site: Site? = null) : Fragment(), OnMapReadyCallback {
    lateinit var map: GoogleMap
    private lateinit var binding: FragmentMapBinding
    private var lat = 0.0
    private var long = 0.0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        createMap()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkMainSite()
    }

    private fun checkMainSite() {
        if(site != null) {
            createMarker(site.latitud.toDouble(), site.longitud.toDouble(), site.name)
        } else {

        }
    }

    private fun createMap(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            map = googleMap
            val sites = ServiceLocator.siteRepository.allSites
            if(sites.isNotEmpty()) {
                sites.forEach {
                    createMarker(it.latitud.toDouble(), it.longitud.toDouble(), it.name)
                }
            }
        } catch (e: Exception) {
            Log.e("CARGAR LOCALIZACIONES","Funcion MapFragment.OnMapReady -> ${e.message} // ${e.printStackTrace()}")
        } finally {
            enableLocation()
        }
    }

    private fun createMarker(latitud: Double, longitud: Double, name: String? = null){
        val coordinates = LatLng(latitud, longitud)
        val myMarker = MarkerOptions()
            .position(coordinates)
            .title(name ?: "Nombre Desconocido")
        map.addMarker(myMarker)
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if(!::map.isInitialized) return
        if(!isLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(requireContext(), "Acepta los permisos de ubicación", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermissionGranted()){
            map.isMyLocationEnabled = true
        }
        else{
            requestLocationPermission()
        }
    }
    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(requireContext(), "Ves a ajustes del telefono a permisos de la aplicacion y habilita la opción de ubicación", Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION
            )
        }
    }
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }
            else{
                Toast.makeText(requireContext(), "Accepta els permisos de geolocalització",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}