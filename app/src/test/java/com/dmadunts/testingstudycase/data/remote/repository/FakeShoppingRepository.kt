package com.dmadunts.testingstudycase.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dmadunts.testingstudycase.data.local.ShoppingItem
import com.dmadunts.testingstudycase.data.remote.responses.ImageResponse
import com.dmadunts.testingstudycase.data.repositories.ShoppingRepository
import com.dmadunts.testingstudycase.utils.Resource

class FakeShoppingRepository : ShoppingRepository {
    private val shoppingItems = arrayListOf<ShoppingItem>()
    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)
    private val observableTotalPrice = MutableLiveData<Float>()
    private var shouldReturnNetworkError = false

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shoppingItems.sumOf {
            it.price.toDouble()
        }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImages(query: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error")
        } else {
            Resource.success(ImageResponse(listOf(), 0, 0))
        }
    }
}