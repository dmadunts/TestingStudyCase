package com.dmadunts.testingstudycase.data.repositories

import androidx.lifecycle.LiveData
import com.dmadunts.testingstudycase.data.local.ShoppingItem
import com.dmadunts.testingstudycase.data.remote.responses.ImageResponse
import com.dmadunts.testingstudycase.utils.Resource

interface ShoppingRepository {
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImages(query: String): Resource<ImageResponse>
}
