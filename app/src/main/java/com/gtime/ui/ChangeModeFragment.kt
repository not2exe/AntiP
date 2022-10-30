package com.gtime.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.App
import com.gtime.ChangeModeViewComponent
import javax.inject.Inject

class ChangeModeFragment : Fragment(R.layout.fragment_change_mode) {
    private var viewComponent: ChangeModeViewComponent? = null

    @Inject
    lateinit var changeModeViewController: ChangeModeViewController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewComponent = (requireContext().applicationContext as App).appComponent.activity()
            .changeMenuViewComponent().create(this).apply {
                inject(this@ChangeModeFragment)
                changeModeViewController.setupViews()
            }
    }

    override fun onDestroyView() {
        viewComponent=null
        super.onDestroyView()
    }
}