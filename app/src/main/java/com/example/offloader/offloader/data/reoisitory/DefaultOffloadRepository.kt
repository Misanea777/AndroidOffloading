package com.example.offloader.offloader.data.reoisitory

import com.example.offloader.offloader.data.models.ClusterListResponse
import com.example.offloader.offloader.data.models.ClusterRegister
import com.example.offloader.offloader.data.models.DeviceRegister
import com.example.offloader.offloader.data.remote.OffloadApi
import com.example.offloader.offloader.network.Resource
import com.example.offloader.offloader.network.safeApiCall
import kotlinx.coroutines.CoroutineDispatcher

class DefaultOffloadRepository(
    private val api: OffloadApi,
    private  val ioDispatcher: CoroutineDispatcher
) : OffloadRepository {

    override suspend fun getClusters(): Resource<ClusterListResponse> =
        safeApiCall(dispatcher = ioDispatcher) {
            api.getClusters()
    }

    override suspend fun registerCluster(cluster: ClusterRegister): Resource<Unit> =
        safeApiCall(dispatcher = ioDispatcher) {
            api.registerCluster(cluster)
        }

    override suspend fun registerDevice(device: DeviceRegister): Resource<Unit> =
        safeApiCall(dispatcher = ioDispatcher) {
            api.registerDevice(device)
        }
}