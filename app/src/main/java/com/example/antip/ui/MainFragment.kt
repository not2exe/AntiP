package com.example.antip.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.App
import com.example.antip.AppAdapter
import com.example.antip.R
import com.example.antip.databinding.FragmentMainBinding
import com.example.antip.viewmodels.MainFragmentViewModel




class MainFragment : Fragment(R.layout.fragment_main) {
    private var bindingOrNull: FragmentMainBinding?  = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private val adapter=AppAdapter()
    private var dataSize=0

    private val viewModel by viewModels<MainFragmentViewModel>() // View model initialization with delegate property


    private fun initObservers()= with(binding){
        TableUsefuls.adapter=adapter
        TableUsefuls.layoutManager=LinearLayoutManager(context)
        viewModel.currentAppsLD.observe(viewLifecycleOwner){
            for (i in it.indices){
                adapter.addApp(it[i])
                dataSize++
            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentMainBinding.bind(view)
        binding.button.setOnClickListener {
            viewModel.clear()
            binding.TableUsefuls.adapter?.notifyItemRangeRemoved(0,dataSize)


        }

        initObservers()


    }


}