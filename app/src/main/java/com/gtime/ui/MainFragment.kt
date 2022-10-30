package com.gtime.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.App
import com.gtime.MainFragmentComponent
import com.gtime.MainFragmentViewComponent
import com.gtime.MainViewController
import javax.inject.Inject

class MainFragment : Fragment(R.layout.fragment_main) {
    private val fragmentComponent: MainFragmentComponent by lazy {
        (requireContext().applicationContext as App).appComponent.activity()
            .mainFragmentComponent().create(this)
    }
    private var fragmentViewComponent: MainFragmentViewComponent? = null

    @Inject
    lateinit var fragmentViewController: MainViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.mainFragmentViewComponent()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewComponent =
            fragmentComponent.mainFragmentViewComponent().create(view, viewLifecycleOwner).apply {
                inject(this@MainFragment)
                fragmentViewController.setUpViews()
            }
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        fragmentViewComponent = null
        super.onDestroyView()
    }
}