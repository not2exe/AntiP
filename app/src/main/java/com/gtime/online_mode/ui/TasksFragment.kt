package com.gtime.online_mode.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gtime.general.app.App
import com.gtime.online_mode.domain.ioc.TaskFragmentComponent
import com.gtime.online_mode.domain.ioc.TaskFragmentViewComponent
import com.gtime.online_mode.ui.logic.TaskViewController
import com.notexe.gtime.R
import javax.inject.Inject


class TasksFragment : Fragment() {
    private val fragmentComponent: TaskFragmentComponent by lazy {
        (requireContext().applicationContext as App).appComponent.activity()
            .create(requireActivity())
            .taskFragmentComponent().create(this)
    }
    private var fragmentViewComponent: TaskFragmentViewComponent? = null

    @Inject
    lateinit var fragmentViewController: TaskViewController


    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent.taskFragmentViewComponent()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentViewComponent =
            fragmentComponent.taskFragmentViewComponent().apply {
                inject(this@TasksFragment)
                fragmentViewController.setupViews()
            }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        fragmentViewComponent = null
        super.onDestroyView()
    }
}