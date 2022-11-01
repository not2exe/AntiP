package com.gtime.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.antip.R
import com.example.antip.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private var menuViewController: MenuViewController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menuViewController =
            MenuViewController(FragmentMenuBinding.bind(view), findNavController())
                .apply {
                    setupViews()
                }

    }

    override fun onDestroyView() {
        menuViewController = null
        super.onDestroyView()
    }


}