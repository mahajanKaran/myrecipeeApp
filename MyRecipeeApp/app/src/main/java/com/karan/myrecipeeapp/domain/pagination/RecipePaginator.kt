package com.karan.myrecipeeapp.domain.pagination

import com.karan.myrecipeeapp.core.Resource
import javax.inject.Inject

class RecipePaginator<Key, Item> @Inject constructor(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Resource<List<Item>>,
    private inline val getNextKey: suspend (items: List<Item>) -> Key,
    private inline val onError: suspend (throwable: Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit,
) : Paginator<Key, Item> {
    private var currentKey = initialKey

    // this will be true as long as we are making request to datasource (database/API)
    private var isMakingRequest = false
    override suspend fun loadNextItems() {
        // don't make request again if we are making request already
        if (isMakingRequest) {
            return
        }

        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        when (result) {
            is Resource.Error -> {
                onError(Throwable(result.error))
                onLoadUpdated(false)
            }

            is Resource.Loading -> {
                onLoadUpdated(true)
            }
            is Resource.Success -> {
                currentKey = getNextKey(result.data!!)
                onSuccess(result.data, currentKey)
                onLoadUpdated(false)
            }
        }
    }

    override suspend fun reset() {
        currentKey = initialKey
    }
}