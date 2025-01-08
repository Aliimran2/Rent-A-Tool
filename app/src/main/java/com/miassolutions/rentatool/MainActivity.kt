package com.miassolutions.rentatool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.miassolutions.rentatool.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    }
}