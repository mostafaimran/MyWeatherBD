package com.my.weather.bd.ui.screen.views.custom

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.my.weather.bd.R

class CustomLinearProgressBar(context: Context) : FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.linear_loading, this, true)
    }
}