package com.bizsolutions.dealmate.di

import android.content.Context
import com.bizsolutions.dealmate.db.AppDatabase
import com.bizsolutions.dealmate.db.ClientDao
import com.bizsolutions.dealmate.repository.ClientRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideClientDao(database: AppDatabase): ClientDao = database.clientDao()

    @Provides
    fun provideRepository(dao: ClientDao): ClientRepository = ClientRepository(dao)
}