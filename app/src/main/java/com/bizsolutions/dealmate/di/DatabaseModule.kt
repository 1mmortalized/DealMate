package com.bizsolutions.dealmate.di

import android.content.Context
import com.bizsolutions.dealmate.db.AppDatabase
import com.bizsolutions.dealmate.db.CallDao
import com.bizsolutions.dealmate.db.ClientDao
import com.bizsolutions.dealmate.db.DealDao
import com.bizsolutions.dealmate.db.EventDao
import com.bizsolutions.dealmate.db.KeywordDao
import com.bizsolutions.dealmate.db.KeywordEntity
import com.bizsolutions.dealmate.db.TaskDao
import com.bizsolutions.dealmate.repository.CallRepository
import com.bizsolutions.dealmate.repository.ClientRepository
import com.bizsolutions.dealmate.repository.DealRepository
import com.bizsolutions.dealmate.repository.EventRepository
import com.bizsolutions.dealmate.repository.KeywordRepository
import com.bizsolutions.dealmate.repository.TaskRepository
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
    fun provideClientRepository(dao: ClientDao): ClientRepository = ClientRepository(dao)

    @Provides
    fun provideEventDao(database: AppDatabase): EventDao = database.eventDao()
    @Provides
    fun provideEventRepository(dao: EventDao): EventRepository = EventRepository(dao)

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao = database.taskDao()
    @Provides
    fun provideTaskRepository(dao: TaskDao): TaskRepository = TaskRepository(dao)

    @Provides
    fun provideCallDao(database: AppDatabase): CallDao = database.callDao()
    @Provides
    fun provideCallRepository(dao: CallDao): CallRepository = CallRepository(dao)

    @Provides
    fun provideDealDao(database: AppDatabase): DealDao = database.dealDao()
    @Provides
    fun provideDealRepository(dao: DealDao): DealRepository = DealRepository(dao)

    @Provides
    fun provideKeywordDao(database: AppDatabase): KeywordDao = database.keywordDao()
    @Provides
    fun provideKeywordRepository(dao: KeywordDao): KeywordRepository = KeywordRepository(dao)
}