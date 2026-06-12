package com.rapsodo.golfperformance.di

import androidx.room.Room
import com.rapsodo.golfperformance.data.local.GolfDatabase
import com.rapsodo.golfperformance.data.remote.GolfApiService
import com.rapsodo.golfperformance.data.repository.GolfRepositoryImpl
import com.rapsodo.golfperformance.domain.repository.GolfRepository
import com.rapsodo.golfperformance.ui.playerdetail.PlayerDetailViewModel
import com.rapsodo.golfperformance.ui.playerlist.PlayerListViewModel
import com.rapsodo.golfperformance.util.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

val appModule = module {
    // Network
    single {
        val logging = HttpLoggingInterceptor { message ->
            Timber.tag("OkHttp").d(message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "GolfPerformanceTracker/1.0")
                    .build()
                chain.proceed(request)
            }
            .build()
    }
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
            .create(GolfApiService::class.java)
    }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            GolfDatabase::class.java,
            "golf_performance.db"
        ).build()
    }
    single { get<GolfDatabase>().dao }

    // Repository
    single<GolfRepository> { GolfRepositoryImpl(get(), get()) }

    // ViewModels
    viewModel { PlayerListViewModel(get()) }
    viewModel { PlayerDetailViewModel(get()) }
}
