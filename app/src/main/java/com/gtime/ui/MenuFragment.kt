package com.gtime.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.antip.R
import com.example.antip.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var menuViewController: MenuViewController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu,container,false)
        menuViewController =
            MenuViewController(FragmentMenuBinding.bind(view), findNavController())
                .apply {
                    setupViews()
                }
        return view
    }

    override fun onDestroyView() {
        menuViewController = null
        super.onDestroyView()
    }


}