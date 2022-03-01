package com.dmadunts.testingstudycase.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.MediumTest
import com.dmadunts.testingstudycase.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@MediumTest
@HiltAndroidTest
class ShoppingDaoTest {

    @Inject
    @Named("test_db")
    lateinit var database: ShoppingItemDatabase

    private lateinit var dao: ShoppingDao

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
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