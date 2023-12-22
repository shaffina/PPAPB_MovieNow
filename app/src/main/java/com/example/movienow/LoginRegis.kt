package com.example.movienow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movienow.databinding.ActivityLoginRegisBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoginRegis : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityLoginRegisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            viewPager.adapter = TabAdapter(this@LoginRegis)
            TabLayoutMediator(tabLayout,viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Login"
                    1 -> "Register"
                    else -> ""
                }
            }.attach()
        }
    }

}