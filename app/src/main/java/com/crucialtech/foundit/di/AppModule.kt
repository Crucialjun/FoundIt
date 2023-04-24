package com.crucialtech.foundit.di

import com.crucialtech.foundit.repository.AuthRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

}

@Module
@InstallIn(ViewModelComponent::class)
object  AuthModule{

    @Provides
    fun provideAuthRepo() : AuthRepo{
        return AuthRepo()
    }
}