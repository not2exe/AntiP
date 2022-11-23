package com.gtime.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.app.App
import com.gtime.domain.MainFragmentComponent
import com.gtime.domain.MainFragmentViewComponent
import javax.inject.Inject

class MainFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main,container,false)
        fragmentViewComponent =
            fragmentComponent.mainFragmentViewComponent().create(view, viewLifecycleOwner).apply {
                inject(this@MainFragment)
                fragmentViewController.setUpViews()
            }
        return view
    }


    
    override fun onDestroyView() {
        fragmentViewComponent = null
        super.onDestroyView()
    }
}