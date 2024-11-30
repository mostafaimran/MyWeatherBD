package com.my.weather.bd.di

import android.content.Context
import com.google.gson.Gson
import com.my.weather.bd.core.JsonConverter
import com.my.weather.bd.coreandroid.GsonConverter
import com.my.weather.bd.data.repository.WeatherBDRepositoryImpl
import com.my.weather.bd.datamodel.repository.WeatherBDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(@ApplicationContext application: Context): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideNotificationQueueRepository(weatherBDRepositoryImpl: WeatherBDRepositoryImpl): WeatherBDRepository =
        weatherBDRepositoryImpl

    @Singleton
    @Provides
    fun provideGsonConverter(): JsonConverter {
        return GsonConverter(Gson())
    }

}