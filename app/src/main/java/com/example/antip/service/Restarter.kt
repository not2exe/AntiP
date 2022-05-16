package com.example.antip.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class Restarter: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(Intent(context,DailyStatsService::class.java))
    }
}