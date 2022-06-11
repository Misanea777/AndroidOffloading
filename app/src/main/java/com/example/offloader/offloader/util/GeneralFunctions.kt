package com.example.offloader.offloader.util

import android.content.Context
import android.provider.Settings


fun getMacAddress(context: Context) =
    Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "android_id")
