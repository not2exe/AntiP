package com.antip


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.antip.model.Cash
import com.example.antip.service.DailyStatsBroadcast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.antip.R.layout.activity_main)
        val cash: Cash = Cash(this.applicationContext)

        if (cash.getAllHarmful().isEmpty()) {
            val dailyStatsBroadcast = DailyStatsBroadcast()
            dailyStatsBroadcast.setAlarm(this.applicationContext)

        }

    }


}





