package com.defey.testapp.di

import com.defey.testapp.data.remote.RemoteDataSource
import com.defey.testapp.data.remote.RemoteDataSourceImpl
import com.defey.testapp.data.repository.MainRepositoryImpl
import com.defey.testapp.domain.MainRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    @Singleton
    abstract fun provideMainRepository(impl: MainRepositoryImpl): MainRepository

}