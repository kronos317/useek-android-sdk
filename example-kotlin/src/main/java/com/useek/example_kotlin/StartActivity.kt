package com.useek.example_kotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        button_kotlin_java.setOnClickListener {

        }

        button_kotlin_kotlin.setOnClickListener {
            val intent = Intent(this, com.useek.example_kotlin.kotlin_kotlin.MainActivity::class.java)
            startActivity(intent)
        }
    }
}
