package com.dmadunts.testingstudycase.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    val name: String,
    val price: Float,
    val amount: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
