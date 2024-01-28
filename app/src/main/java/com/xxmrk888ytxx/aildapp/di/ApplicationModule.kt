package com.xxmrk888ytxx.aildapp.di

import com.xxmrk888ytxx.aildapp.data.RecordFileProvider
import com.xxmrk888ytxx.aildapp.data.RecordRepositoryImpl
import com.xxmrk888ytxx.aildapp.domain.RecordRepository
import com.xxmrk888ytxx.aildapp.presentation.RecordViewModel
import org.koin.dsl.module

val applicationModule = module {
    factory<RecordViewModel> { RecordViewModel(get()) }
    single<RecordRepository> { RecordRepositoryImpl(get()) }
    single { RecordFileProvider(get()) }
}