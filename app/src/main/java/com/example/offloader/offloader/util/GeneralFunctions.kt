package com.example.offloader.offloader.util

import android.content.Context
import android.provider.Settings
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


fun getMacAddress(context: Context) =
    Settings.Secure.getString(context.getApplicationContext().getContentResolver(), "android_id")


fun getUUID() = UUID.randomUUID().toString()

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(this.toByteArray())).toString(16).padStart(32, '0')
}
