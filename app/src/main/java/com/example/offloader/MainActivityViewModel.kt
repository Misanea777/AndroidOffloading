package com.example.offloader

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.offloader.offloader.data.models.ClusterListResponse
import com.example.offloader.offloader.data.reoisitory.OffloadRepository
import com.example.offloader.offloader.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val repository: OffloadRepository
) : ViewModel() {

    val cluster_list: MutableLiveData<ClusterListResponse> = MutableLiveData()

    val numbers: MutableLiveData<List<Int>> = MutableLiveData()

    fun randList() = viewModelScope.launch {
        val min = 0
        val max = 1000
        val nums = mutableListOf<Int>()
        for (i in 0..1000000) {
            nums.add(Random().nextInt(max - min + 1) + min)
        }
        numbers.value = nums
    }

    suspend fun sort(list: MutableList<Int>): MutableList<Int> {
        return list.apply {
            sort()
            sortDescending()
            sort()
        }
    }
//
//    init {
//        testNetwork()
//        randList()
//    }

    fun testNetwork() = viewModelScope.launch {
        val response = repository.getClusters()
        when(response) {
            is Resource.Success -> cluster_list.value = response.value
            is Resource.Failure -> println("error. code: ${response.errorCode} ${response.errorBody}")
        }
    }
}