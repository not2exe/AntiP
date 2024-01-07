package com.gtime.online_mode.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gtime.general.app.App
import com.gtime.online_mode.domain.ioc.LoginViewComponent
import com.gtime.online_mode.ui.logic.LoginViewController
import com.notexe.gtime.R
import javax.inject.Inject


class LoginFragment : Fragment() {
    private var loginFragmentViewComponent: LoginViewComponent? = null

    @Inject
    lateinit var loginViewController: LoginViewController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loginFragmentViewComponent =
            (requireContext().applicationContext as App).appComponent.activity()
                .create(requireActivity()).loginFragmentViewComponent().create(this).apply {
                    inject(this@LoginFragment)
                    loginViewController.setupViews()
                }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        loginFragmentViewComponent = null
        super.onDestroyView()
    }


}