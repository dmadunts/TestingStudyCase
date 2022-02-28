package com.dmadunts.testingstudycase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmadunts.testingstudycase.data.local.ShoppingItem
import com.dmadunts.testingstudycase.data.remote.responses.ImageResponse
import com.dmadunts.testingstudycase.data.repositories.ShoppingRepository
import com.dmadunts.testingstudycase.utils.Constants
import com.dmadunts.testingstudycase.utils.Event
import com.dmadunts.testingstudycase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) :
    ViewModel() {
    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()
    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItem: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    private fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountPar: String, pricePar: String) {
        if (name.isEmpty() || amountPar.isEmpty() || pricePar.isEmpty()) {
            _insertShoppingItemStatus.value = Event(Resource.error("Fields must not be empty"))
            return
        }

        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.value =
                Event(Resource.error("The name should not be longer than ${Constants.MAX_NAME_LENGTH} characters"))
            return
        }

        if (pricePar.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.value =
                Event(Resource.error("The price of the item should not be greater than ${Constants.MAX_PRICE_LENGTH}"))
            return
        }

        val amount = try {
            amountPar.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.value = Event(Resource.error("Enter valid amount"))
            return
        }

        val shoppingItem = ShoppingItem(name, pricePar.toFloat(), amount, _curImageUrl.value ?: "")
        insertShoppingItemIntoDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.value = Event(Resource.success(shoppingItem))
    }

    fun searchForImage(query: String) {
        if (query.isEmpty()) {
            return
        }
        _images.value = Event(Resource.loading())
        viewModelScope.launch {
            val response = repository.searchForImages(query)
            _images.value = Event(response)
        }
    }

}