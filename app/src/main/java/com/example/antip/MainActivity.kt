package com.example.antip

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.ActivityManager
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antip.databinding.ActivityMainBinding
import java.lang.String


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val adapter=AppAdapter()
    private val list = listOf (
        R.drawable.app1,
        R.drawable.app2,
        R.drawable.app3,
        R.drawable.app4,
        R.drawable.app5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() = with(binding) {
        var index=0
        MainTable.layoutManager = LinearLayoutManager(this@MainActivity)
        MainTable.adapter = adapter
        imageButton.setOnClickListener {
            val app=App(list[index%5],(100*index).toString())
            adapter.addApp(app)
            index++
        }

    }
}