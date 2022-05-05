package com.example.antip.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.App
import com.example.antip.R
import com.example.antip.databinding.FragmentSettingsBinding
import com.example.antip.viewmodels.CustomAdapter
import com.example.antip.viewmodels.CustomListener
import com.example.antip.viewmodels.MainFragmentViewModel


class SettingsFragment : Fragment(R.layout.fragment_settings), CustomListener {
    private var bindingOrNull: FragmentSettingsBinding? = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private var useful= arrayListOf<App>()
    private var useless= arrayListOf<App>()
    private val viewModel by viewModels<MainFragmentViewModel>() // View model initialization with delegate property

    private fun initObservers() = with(binding) {

        viewModel.usefulApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                if (i % 2 == 0)
                    useful.add(it[i])
                else
                    useless.add(it[i])
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingOrNull = FragmentSettingsBinding.bind(view)
        initObservers()
        binding.rvUseful.init(useful)
        binding.rvUseless.init(useless)
        binding.rvUndefined.init(useful)


    }

    private fun RecyclerView.init(list: List<App>) {
        this.layoutManager = LinearLayoutManager(context)
        val adapter = CustomAdapter(list, this@SettingsFragment)
        this.adapter = adapter
        this.setOnDragListener(adapter.dragInstance)
    }

    override fun setEmptyList(visibility: Int, recyclerView: Int) {
        TODO("Not yet implemented")
    }


}