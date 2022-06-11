package com.example.offloader.offloader.data.remote

import com.example.offloader.offloader.data.models.ClusterListResponse
import com.example.offloader.offloader.data.models.ClusterRegister
import com.example.offloader.offloader.data.models.DeviceRegister
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface OffloadApi {

    @GET("cluster_list")
    suspend fun getClusters(): ClusterListResponse

    @Headers("Content-Type: application/json")
    @POST("cluster/register")
    suspend fun registerCluster(@Body cluster: ClusterRegister): Unit

    @Headers("Content-Type: application/json")
    @POST("device/register")
    suspend fun registerDevice(@Body device: DeviceRegister): Unit
}