package com.antip.ui

import android.app.AppOpsManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.antip.R
import com.example.antip.adapters.AppAdapter
import com.example.antip.databinding.FragmentMainBinding
import com.example.antip.model.dataclasses.AppEntity
import com.example.antip.viewmodels.MainFragmentViewModel
import com.example.antip.viewmodels.State


class MainFragment : Fragment(R.layout.fragment_main) {
    private var bindingOrNull: FragmentMainBinding? = null // ? is for nullable variable type
    private val binding get() = bindingOrNull!! // !! is for turning off null safety
    private var state: State = State.USEFUL
    private var harmfulAppEntities: ArrayList<AppEntity> = ArrayList<AppEntity>()
    private var usefulAppEntities: ArrayList<AppEntity> = ArrayList<AppEntity>()
    private var lastTimeClicked: Long = 0


    private val viewModel by viewModels<MainFragmentViewModel>() // View model initialization with delegate property


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingOrNull = FragmentMainBinding.bind(view)
        if (viewModel.getIsLostHardcore())
            binding.imageLife.setImageResource(R.drawable.heart_broken)

        if (!checkUsagePerm())
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToFirstTimeFragment())

        initRv()


        viewModel.provideTimeInModel()
        viewModel.initApps()
        initObservers()



        binding.buttonChangeAdapter.setOnClickListener {
            onClickChangeButton()
        }
        binding.buttonSettings.setOnClickListener {
            onClickSettingsButton()
        }
        binding.refreshButton.setOnClickListener {
            onClickRefreshButton(it)
        }


    }

    override fun onDestroyView() {
        usefulAppEntities.clear()
        harmfulAppEntities.clear()
        super.onDestroyView()
    }

    private fun initObservers() = with(binding) {
        viewModel.usefulApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {
                it[i]?.let { it1 -> usefulAppEntities.add(it1) }
            }

        }
        viewModel.harmfulApps.observe(viewLifecycleOwner) {
            for (i in it.indices) {

                it[i]?.let { it1 -> harmfulAppEntities.add(it1) }
            }


        }
        viewModel.scoresAll.observe(viewLifecycleOwner) {
            if (it < 0) {
                scores.setTextColor(Color.RED)
            } else {
                scores.setTextColor(Color.GREEN)
            }
            scores.text = it.toString()

        }
    }

    private fun initRv() = with(binding) {
        when (state) {
            State.USEFUL -> {
                MainTable.init(usefulAppEntities)
                buttonChangeAdapter.setText(R.string.useful)
            }
            State.HARMFUL -> {
                MainTable.init(harmfulAppEntities)
                buttonChangeAdapter.setText(R.string.harmful)
            }
        }

    }


    private fun onClickChangeButton() = with(binding) {
        when (state) {
            State.USEFUL -> {
                (MainTable.adapter as AppAdapter).updateList(harmfulAppEntities)
                state = State.HARMFUL
                buttonChangeAdapter.setText(R.string.harmful)
            }
            State.HARMFUL -> {
                (MainTable.adapter as AppAdapter).updateList(usefulAppEntities)
                state = State.USEFUL
                buttonChangeAdapter.setText(R.string.useful)
            }
        }
    }

    private fun RecyclerView.init(list: ArrayList<AppEntity>) {
        this.layoutManager = LinearLayoutManager(context)
        val adapter = AppAdapter(list)
        this.adapter = adapter
    }

    private fun onClickRefreshButton(view: View) {
        if (System.currentTimeMillis() > lastTimeClicked + 10000) {
            usefulAppEntities.clear()
            harmfulAppEntities.clear()
            viewModel.refresh()
            initObservers()
            lastTimeClicked = System.currentTimeMillis()


        } else
            Toast.makeText(context, R.string.wait, Toast.LENGTH_SHORT).show()


    }

    private fun onClickSettingsButton() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToMenuFragment())


    }

    private fun checkUsagePerm(): Boolean {
        return try {

            val applicationInfo =
                requireContext().packageManager.getApplicationInfo(requireContext().packageName, 0)
            val appOpsManager =
                requireContext().getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


}