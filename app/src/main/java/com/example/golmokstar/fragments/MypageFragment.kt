package com.example.golmokstar.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.golmokstar.R
import com.example.golmokstar.databinding.FragmentCalendarBinding
import com.example.golmokstar.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {

    private lateinit var binding : FragmentMypageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)

        binding.button1.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_homeFragment)
        }

        binding.button2.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_calendarFragment)
        }

        binding.button3.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_mapsFragment)
        }

        binding.button4.setOnClickListener {
            it.findNavController().navigate(R.id.action_mypageFragment_to_historyFragment)
        }


        return binding.root
    }
}