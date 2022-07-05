package com.sfs.artery.certification.app.util

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonModule {

    @Binds
    abstract fun resourceProvide(resourceProviderImpl: ResourceProviderImpl): ResourceProvider
}