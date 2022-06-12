package com.example.offloader.offloader.data.models

import com.google.gson.Gson

data class ReceivedTask(
    val args: Array<String>,
    val cluster_id: String,
    val device_id: String,
    val hash: String,
    val id: String,
    val task_id: String
)

fun receivedTaskFromJson(json: String): ReceivedTask {
    return Gson().fromJson(json, ReceivedTask::class.java)
}

fun ReceivedTask.getArgs() = this.args.map { Gson().fromJson(it, Any::class.java) }.toTypedArray()