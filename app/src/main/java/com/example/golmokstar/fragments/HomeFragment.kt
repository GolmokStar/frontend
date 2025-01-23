package com.example.golmokstar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.golmokstar.R
import com.example.golmokstar.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.button2.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }

        binding.button3.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }

        binding.button4.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_historyFragment)
        }

        binding.button5.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_mypageFragment)
        }


        return binding.root
    }
}