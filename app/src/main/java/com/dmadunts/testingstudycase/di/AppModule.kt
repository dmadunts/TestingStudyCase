package com.dmadunts.testingstudycase.di

import android.content.Context
import androidx.room.Room
import com.dmadunts.testingstudycase.data.local.ShoppingDao
import com.dmadunts.testingstudycase.data.local.ShoppingItemDatabase
import com.dmadunts.testingstudycase.data.remote.apis.PixabayApi
import com.dmadunts.testingstudycase.data.remote.repository.RemoteShoppingRepository
import com.dmadunts.testingstudycase.data.repositories.ShoppingRepository
import com.dmadunts.testingstudycase.utils.Constants.BASE_URL
import com.dmadunts.testingstudycase.utils.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideShoppingItemDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppingItemDao(database: ShoppingItemDatabase) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi(): PixabayApi {
        return Retrofit.Builder().baseUrl(BASE_URL).build()
            .create(PixabayApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRemoteShoppingRepository(dao: ShoppingDao, api: PixabayApi) =
        RemoteShoppingRepository(dao, api) as ShoppingRepository
}
