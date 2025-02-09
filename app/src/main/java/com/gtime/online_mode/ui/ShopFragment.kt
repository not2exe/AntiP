package com.gtime.online_mode.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gtime.general.app.App
import com.gtime.online_mode.domain.ioc.ShopFragmentComponent
import com.gtime.online_mode.domain.ioc.ShopFragmentViewComponent
import com.gtime.online_mode.ui.logic.ShopViewController
import com.notexe.gtime.R
import javax.inject.Inject

class ShopFragment : Fragment() {
    private val fragmentComponent: ShopFragmentComponent by lazy {
        (requireContext().applicationContext as App).appComponent.activity()
            .create(requireActivity())
            .shopFragmentComponent().create(this)
    }
    private var fragmentViewComponent: ShopFragmentViewComponent? = null

    @Inject
    lateinit var fragmentViewController: ShopViewController


    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.shopFragmentViewComponent()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewComponent =
            fragmentComponent.shopFragmentViewComponent().apply {
                inject(this@ShopFragment)
                fragmentViewController.setupViews()
            }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        fragmentViewComponent = null
        super.onDestroyView()
    }
}