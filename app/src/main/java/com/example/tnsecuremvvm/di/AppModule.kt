package com.example.tnsecuremvvm.di

import com.domain.usecase.TNSecureInteractor
import com.domain.usecase.TNSecureUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideApplication(): BaseApplication =
        BaseApplication()

    @Provides
    @Singleton
    fun provideInteractor(tnSecureInteractor: TNSecureInteractor): TNSecureUseCase =
            tnSecureInteractor

}