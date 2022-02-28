package com.dmadunts.testingstudycase.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.dmadunts.testingstudycase.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class ShoppingDaoTest {
    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        dao = database.shoppingDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("name", 1f, 1, "imageUrl", 1)
        dao.insertShoppingItem(shoppingItem)
        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem("name", 1f, 1, "imageUrl")
        dao.insertShoppingItem(shoppingItem)
        dao.deleteShoppingItem(shoppingItem)
        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(allShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeTotalPrice() = runTest {
        val items = arrayListOf(
            ShoppingItem("name", 100f, 5, "imageUrl1"),
            ShoppingItem("name2", 50f, 3, "imageUr2"),
            ShoppingItem("name3", 33f, 10, "imageUr3")
        )

        items.forEach { item ->
            dao.insertShoppingItem(item)
        }

        var expectedTotalPrice = 0f
        items.forEach { item ->
            expectedTotalPrice += item.price * item.amount
        }
        val actualTotalPrice = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(expectedTotalPrice).isEqualTo(actualTotalPrice)
    }
}