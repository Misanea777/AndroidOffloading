package com.example.offloader.offloader.data.models

data class ClusterListResponse(
    val message: List<ClusterItem>,
    val success: Boolean
)