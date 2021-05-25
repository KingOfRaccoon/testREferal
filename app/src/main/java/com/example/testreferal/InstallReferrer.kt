package com.example.testreferal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class InstallReferrer : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val referrer = intent?.getStringExtra("referrer")
        Log.e("fls;d", referrer.toString())
        //Use the referrer
    }
}