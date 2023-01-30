package com.gtime.online_mode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.general.app.App
import javax.inject.Inject


class TopFragment : Fragment() {
    private val fragmentComponent: TopFragmentComponent by lazy {
        (requireContext().applicationContext as App).appComponent.activity()
            .create(requireActivity())
            .topFragmentComponent().create(this)
    }
    private var fragmentViewComponent: TopFragmentViewComponent? = null

    @Inject
    lateinit var fragmentViewController: TopScoresViewController


    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.topFragmentViewComponent()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewComponent =
            fragmentComponent.topFragmentViewComponent().apply {
                inject(this@TopFragment)
                fragmentViewController.setupViews()
            }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        fragmentViewComponent = null
        super.onDestroyView()
    }
}