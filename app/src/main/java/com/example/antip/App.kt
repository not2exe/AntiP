package com.example.antip

import android.graphics.drawable.Drawable
import androidx.room.Entity

@Entity(tableName = "usage_time_table")
data class App(val image:Drawable,val packageName:String,val scores:Long)
