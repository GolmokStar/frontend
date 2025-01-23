package com.example.golmokstar.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.golmokstar.R
import com.example.golmokstar.databinding.FragmentMapsBinding
import com.example.golmokstar.databinding.FragmentMypageBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding  // 타입을 FragmentMapsBinding으로 변경

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maps, container, false)

        binding.button1.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapsFragment_to_homeFragment)
        }

        binding.button2.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapsFragment_to_calendarFragment)
        }

        binding.button4.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapsFragment_to_historyFragment)
        }

        binding.button5.setOnClickListener {
            it.findNavController().navigate(R.id.action_mapsFragment_to_mypageFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}