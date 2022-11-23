package com.gtime.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.app.App
import com.gtime.domain.AppManagerFragmentViewComponent
import javax.inject.Inject


class AppManagerFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_app_manager,container,false)
        appManagerFragmentViewComponent =
            appManagerFragmentComponent.appManagerFragmentViewComponent()
                .create(view, viewLifecycleOwner).apply {
                    inject(this@AppManagerFragment)
                    appManagerViewController.setupViews()
                }
        return view
    }

    override fun onDestroyView() {
        appManagerFragmentViewComponent = null
        super.onDestroyView()
    }


}