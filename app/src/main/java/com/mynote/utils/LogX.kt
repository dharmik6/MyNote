package com.project.app.utils

import android.util.Log
import com.facebook.shimmer.BuildConfig

class LogX {

    companion object {

        fun E(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun E(tag: String, message: String) {
            if (BuildConfig.DEBUG) {
                Log.e(tag, "" + message)
            }
        }

        fun D(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun V(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }

        fun W(message: String) {
            if (BuildConfig.DEBUG) {
                Log.e("LogX", "" + message)
            }
        }
    }
}
