package com.defey.testapp.di

import com.defey.testapp.data.local.LocalDataSource
import com.defey.testapp.data.local.LocalDataSourceImpl
import com.defey.testapp.data.local.model.CameraEntity
import com.defey.testapp.data.local.model.DoorEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideRealm(): Realm {
        val config = RealmConfiguration.Builder(
            schema = setOf(
                DoorEntity::class,
                CameraEntity::class
            )
        )
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(realm: Realm): LocalDataSource {
        return LocalDataSourceImpl(realm = realm)
    }
}