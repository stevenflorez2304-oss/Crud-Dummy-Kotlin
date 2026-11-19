package com.example.crud_dummy.di

import com.example.crud_dummy.data.repository.ProductRepositoryImpl
import com.example.crud_dummy.domain.repository.ProductRepository
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
    abstract fun bindCharacterRepository(
        impl: ProductRepositoryImpl
    ): ProductRepository


}