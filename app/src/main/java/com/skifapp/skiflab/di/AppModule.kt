package com.skifapp.skiflab.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.ibrahimsn.lib.PhoneNumberKit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePhoneNumberKit(@ApplicationContext context: Context): PhoneNumberKit {
        return PhoneNumberKit.Builder(context)
            .setIconEnabled(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideAppScope() : CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
}