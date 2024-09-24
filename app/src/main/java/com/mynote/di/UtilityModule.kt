package com.mynote.di

import android.content.Context
import android.content.SharedPreferences
import com.mynote.utils.resource.FireBaseResponseHandler
import com.project.app.utils.AppConstant
import com.project.app.utils.ErrorUtils
import com.project.app.utils.PrefUtils
import com.project.app.utils.resource.ResponseHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
class UtilsModules {
    
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(AppConstant.PREF_KEY, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePrefUtils(preferences: SharedPreferences): PrefUtils {
        return PrefUtils(preferences)
    }

    @Singleton
    @Provides
    fun provideErrorUtils(@ApplicationContext context: Context): ErrorUtils {
        return ErrorUtils(context)
    }

    @Singleton
    @Provides
    fun provideResponseHandler(
        @ApplicationContext context: Context,
        errorUtils: ErrorUtils,
    ): ResponseHandler {
        return ResponseHandler(context, errorUtils)
    }

    @Singleton
    @Provides
    fun provideFirebaseResponseHandler(
    ): FireBaseResponseHandler {
        return FireBaseResponseHandler()
    }

}