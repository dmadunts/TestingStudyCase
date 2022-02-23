package com.dmadunts.testingstudycase.ui

import androidx.lifecycle.ViewModel
import com.dmadunts.testingstudycase.data.repositories.ShoppingRepository
import javax.inject.Inject

class ShoppingViewModel @Inject constructor(private val shoppingRepository: ShoppingRepository) :
    ViewModel() {

}