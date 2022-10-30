package com.gtime.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.App
import com.gtime.AppManagerFragmentViewComponent
import com.gtime.AppManagerViewController
import javax.inject.Inject


class AppManagerFragment : Fragment(R.layout.fragment_app_manager) {
    private val appManagerFragmentComponent by lazy {
        (requireContext().applicationContext as App).appComponent.activity()
            .appManagerFragmentComponent().create(this)
    }
    private var appManagerFragmentViewComponent: AppManagerFragmentViewComponent? = null

    @Inject
    lateinit var appManagerViewController: AppManagerViewController

    override fun onCreate(savedInstanceState: Bundle?) {
        appManagerFragmentComponent.appManagerFragmentViewComponent()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appManagerFragmentViewComponent =
            appManagerFragmentComponent.appManagerFragmentViewComponent()
                .create(view, viewLifecycleOwner).apply {
                    inject(this@AppManagerFragment)
                    appManagerViewController.setupViews()
                }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        appManagerFragmentViewComponent = null
        super.onDestroyView()
    }


}