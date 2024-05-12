package com.karan.myrecipeeapp.di

import android.app.Application
import androidx.room.Room
import com.karan.myrecipeeapp.core.Constants
import com.karan.myrecipeeapp.core.Constants.API_KEY
import com.karan.myrecipeeapp.data.local.RecipeDatabase
import com.karan.myrecipeeapp.data.remote.RecipeApi
import com.karan.myrecipeeapp.data.remote.repository.RecipeRepositoryImpl
import com.karan.myrecipeeapp.domain.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private fun getHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request().url
                .newBuilder()
                .build()

            val request = chain.request()
                .newBuilder()
                .header("Content-Type", "application/json")
                .header("x-api-key", API_KEY)
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(interceptor)

        return httpClient.build()
    }

    @Provides
    @Singleton
    fun providesRecipeApi(): RecipeApi = Retrofit
        .Builder()
        .client(getHttpClient())
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RecipeApi::class.java)

    @Provides
    @Singleton
    fun providesRecipeDatabase(app: Application): RecipeDatabase = Room
        .databaseBuilder(app, RecipeDatabase::class.java, RecipeDatabase.DATABASE_NAME)
        .build()

    @Provides
    @Singleton
    fun providesRecipeRepository(recipeApi: RecipeApi, database: RecipeDatabase): RecipeRepository =
        RecipeRepositoryImpl(recipeApi = recipeApi, recipeDatabase = database)
}