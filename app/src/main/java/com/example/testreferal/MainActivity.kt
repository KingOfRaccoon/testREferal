package com.example.testreferal

import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val link = FirebaseDynamicLinks.getInstance()
            .createDynamicLink()
            .setLink(Uri.parse("https://play.google.com/store/apps/details?id=com.castprogramms.balamutbatut&hl=ru&gl=US"))
            .setDynamicLinkDomain("balamutbatut.page.link")
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .setGoogleAnalyticsParameters(
                DynamicLink.GoogleAnalyticsParameters.Builder()
                    .setCampaign("cast")
                    .setContent("tut")
                    .build())
            .buildDynamicLink()
        Log.e("data", link.uri.toString())

//        val client = InstallReferrerClient.newBuilder(this).build()
//        client.startConnection(object : InstallReferrerStateListener{
//            override fun onInstallReferrerSetupFinished(p0: Int) {
//                when(p0){
//                    InstallReferrerResponse.OK -> {
//                        try {
////                            if (BuildConfig.DEBUG) Log.d("InstallReferrerState", "OK")
//                            val response: ReferrerDetails = client.installReferrer
//                            response.installReferrer
//                            response.referrerClickTimestampSeconds
//                            response.installBeginTimestampSeconds
//                            Log.e("data", response.installReferrer)
//                            client.endConnection()
//                        } catch (e: RemoteException) {
//                            Log.e("data", e.message.toString())
//                        }
//                    }
//                    InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
//                        Log.e("InstallReferrerState", "FEATURE_NOT_SUPPORTED");
//                        // API не поддерживается текущей версией Google Play
//                    }
//                    InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
//                        Log.e("InstallReferrerState", "SERVICE_UNAVAILABLE");
//                    }
//
//                }
//            }
//
//            override fun onInstallReferrerServiceDisconnected() {
//
//            }
//        })
        val actionType = "com.android.vending.INSTALL_REFERRER"
        val install = InstallReferrer()
        registerReceiver(install, IntentFilter(actionType))
        getReferral(::onReferrerSuccess, this)
    }

    private fun onReferrerSuccess(data: String) {
        runOnUiThread {
            Toast.makeText(this , "The Refer Code is :-$data" , Toast.LENGTH_SHORT).show()
        }
    }
    private fun getReferral(cb: (String) -> Unit, mainActivity: MainActivity) {
        thread {
            val referrerClient: InstallReferrerClient = InstallReferrerClient.newBuilder(this).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            val response: ReferrerDetails = referrerClient.installReferrer
                            Toast.makeText(mainActivity, response.installReferrer, Toast.LENGTH_LONG).show()
                        }

                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                            cb("")
                            Toast.makeText(mainActivity, "Referrer Feature not Supported !!!!!", Toast.LENGTH_LONG).show()
                        }

                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            cb("")
                            Toast.makeText(mainActivity, "Referrer Service not Available !!!!!", Toast.LENGTH_LONG).show()
                        }

                        else->{
                            Toast.makeText(mainActivity , "Something went wrong !!!!" , Toast.LENGTH_LONG).show()
                            cb("")
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    referrerClient.startConnection(this)
                }
            })
        }
    }
}