package com.example.offloader.offloader.data.reoisitory

import com.example.offloader.offloader.data.models.ClusterListResponse
import com.example.offloader.offloader.data.models.ClusterRegister
import com.example.offloader.offloader.data.models.DeviceRegister
import com.example.offloader.offloader.network.Resource
import retrofit2.http.Body

interface OffloadRepository {
    suspend fun getClusters(): Resource<ClusterListResponse>
    suspend fun registerCluster(cluster: ClusterRegister): Resource<Unit>
    suspend fun registerDevice(device: DeviceRegister): Resource<Unit>
}