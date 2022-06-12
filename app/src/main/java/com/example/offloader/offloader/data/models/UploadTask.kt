package com.example.offloader.offloader.data.models

import android.content.Context
import com.example.offloader.offloader.util.getMacAddress
import com.example.offloader.offloader.util.getUUID
import com.google.gson.Gson

data class UploadTask(
    val args: Array<String>,
    val cluster_id: String,
    val device_id: String,
    val hash: String,
    val task_id: String
)

fun buildUploadTaskAsJson(context: Context, hashCode: Int, vararg params: Any?): String {
    val argsAsJson = params.map { Gson().toJson(it) }.toTypedArray()
    val taskToUpload = UploadTask(
        args = argsAsJson,
        cluster_id = "some id",
        device_id = getMacAddress(context),
        hash = hashCode.toString(),
        task_id = getUUID()
    )
    return Gson().toJson(taskToUpload)
}