package com.example.offloader.offloader.util

sealed class LocalResource<out T> {
    data class Success<out T>(val value: T) : LocalResource<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val isBatteryLowException: Boolean,
        val isNetworkUnstableException: Boolean,
        val throwable: Throwable
    ) : LocalResource<Nothing>()
}