package com.huangrui.dormitory.test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.huangrui.dormitory.MainActivity
import com.huangrui.dormitory.R


class TestActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val button:Button = findViewById(R.id.button_1)
        button.setOnClickListener {
            super.onBackPressed()
        }

    }

}
