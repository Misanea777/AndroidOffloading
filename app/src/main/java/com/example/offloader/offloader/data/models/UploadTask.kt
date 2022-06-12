package com.example.offloader.offloader.data.models

data class UploadTask(
    val args: Any?,
    val cluster_id: String,
    val device_id: String,
    val hash: String
)