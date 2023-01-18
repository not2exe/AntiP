package com.gtime.offline_mode.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.general.app.App
import com.gtime.offline_mode.domain.MainFragmentComponent
import com.gtime.offline_mode.domain.MainFragmentViewComponent
import javax.inject.Inject

class MainFragment : Fragment() {
    private val fragmentComponent: MainFragmentComponent by lazy {
        (requireContext().applicationContext as App).appComponent.activity().create(requireActivity())
            .mainFragmentComponent().create(this)
    }
    private var fragmentViewComponent: MainFragmentViewComponent? = null

    @Inject
    lateinit var fragmentViewController: MainViewController


    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.mainFragmentViewComponent()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewComponent =
            fragmentComponent.mainFragmentViewComponent().apply {
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