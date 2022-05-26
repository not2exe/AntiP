package com.example.antip.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.adapters.ManagerAdapter
import com.example.antip.databinding.FragmentAppManagerBinding
import com.example.antip.model.dataclasses.AppManager
import com.example.antip.viewmodels.AppManagerFragmentViewModel


class AppManagerFragment : Fragment(R.layout.fragment_app_manager) {
    private var bindingOrNull: FragmentAppManagerBinding? = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private val viewModel by viewModels<AppManagerFragmentViewModel>()
    private val harmfulApps: ArrayList<AppManager> = ArrayList()
    private val usefulApps: ArrayList<AppManager> = ArrayList()
    private val otherApps: ArrayList<AppManager> = ArrayList()


    private fun initObservers() = with(binding) {
        viewModel.usefulApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> usefulApps.add(it1) }
            }
            (rvUseful.adapter as ManagerAdapter).updateList(usefulApps)


        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> harmfulApps.add(it1) }
            }
            (rvHarmful.adapter as ManagerAdapter).updateList(harmfulApps)

        }
        viewModel.otherApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> otherApps.add(it1) }
            }
            (rvOthers.adapter as ManagerAdapter).updateList(otherApps)

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentAppManagerBinding.bind(view)
        with(binding) {
            rvHarmful.init(harmfulApps)
            rvUseful.init(usefulApps)
            rvOthers.init(otherApps)
            buttonBack.setOnClickListener {
                findNavController().navigate(AppManagerFragmentDirections.actionAppManagerFragmentToMenuFragment())
            }

        }

        viewModel.initApps()
        initObservers()
    }


    private fun RecyclerView.init(list: ArrayList<AppManager>) {
        this.layoutManager = LinearLayoutManager(context)
        val adapter = ManagerAdapter(list, this@AppManagerFragment)
        this.adapter = adapter
        this.setOnDragListener(adapter.dragInstance)

    }


}