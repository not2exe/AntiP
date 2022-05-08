package com.example.antip.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.App
import com.example.antip.R
import com.example.antip.databinding.FragmentSettingsBinding
import com.example.antip.viewmodels.CustomAdapter
import com.example.antip.viewmodels.MainFragmentViewModel


class SettingsFragment : Fragment(R.layout.fragment_settings){
    private var bindingOrNull: FragmentSettingsBinding? = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety

    private var harmuflApps:ArrayList<App> = ArrayList<App>()
    private var usefulApps:ArrayList<App> = ArrayList<App>()
    private var otherApps:ArrayList<App> = ArrayList<App>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentSettingsBinding.bind(view)
        binding.rvUseless.init(harmuflApps)
        binding.rvUseful.init(usefulApps)
        binding.rvUndefined.init(otherApps)


    }

    private fun RecyclerView.init(list: ArrayList<App>) {
        this.layoutManager = LinearLayoutManager(context)
        val adapter = CustomAdapter(list, this@SettingsFragment,App(context.getDrawable(R.drawable.undefined)!!,"Empty",0))
        this.adapter = adapter
        this.setOnDragListener(adapter.dragInstance)

    }





}