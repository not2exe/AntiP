package com.gtime.online_mode.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antip.R
import com.gtime.general.app.App
import com.gtime.online_mode.domain.ioc.PromoCodeFragmentComponent
import com.gtime.online_mode.domain.ioc.PromoCodeFragmentViewComponent
import com.gtime.online_mode.ui.logic.PromoCodeViewController
import com.gtime.online_mode.ui.logic.ShopViewController
import javax.inject.Inject


class PromoCodeFragment : Fragment() {
    private val fragmentComponent: PromoCodeFragmentComponent by lazy {
        (requireContext().applicationContext as App).appComponent.activity()
            .create(requireActivity())
            .promoCodeFragmentComponent().create(this)
    }
    private var fragmentViewComponent: PromoCodeFragmentViewComponent? = null

    @Inject
    lateinit var fragmentViewController: PromoCodeViewController


    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.promoCodeFragmentViewComponent()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_promo_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewComponent =
            fragmentComponent.promoCodeFragmentViewComponent().apply {
                inject(this@PromoCodeFragment)
                fragmentViewController.setupViews()
            }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        fragmentViewComponent = null
        super.onDestroyView()
    }
}