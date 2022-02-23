package com.dmadunts.testingstudycase.data.remote.repository

import androidx.lifecycle.LiveData
import com.dmadunts.testingstudycase.data.local.ShoppingDao
import com.dmadunts.testingstudycase.data.local.ShoppingItem
import com.dmadunts.testingstudycase.data.networking.NetworkManager
import com.dmadunts.testingstudycase.data.remote.apis.PixabayApi
import com.dmadunts.testingstudycase.data.remote.responses.ImageResponse
import com.dmadunts.testingstudycase.data.repositories.ShoppingRepository
import com.dmadunts.testingstudycase.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val api: PixabayApi
) :
    ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImages(query: String): Resource<ImageResponse> =
        withContext(Dispatchers.IO) {
            return@withContext NetworkManager.apiCall {
                api.searchImage(query)
            }
        }

}