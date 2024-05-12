package com.karan.myrecipeeapp.domain.pagination

interface Paginator <Key, Item> {
    suspend fun loadNextItems()
    suspend fun reset()
}