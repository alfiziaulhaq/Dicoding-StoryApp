package com.dicoding.alfistoryapp.data.retrofit

import com.dicoding.alfistoryapp.data.response.DetailStoryResponse
import com.dicoding.alfistoryapp.data.response.GetStoryResponse
import com.dicoding.alfistoryapp.data.response.LoginResponse
import com.dicoding.alfistoryapp.data.response.PostStoryResponse
import com.dicoding.alfistoryapp.data.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): SignupResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
    ): GetStoryResponse

    @GET("stories")
   fun getStory(
    ): Call<GetStoryResponse>

    @GET("stories")
    suspend fun getStories2(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoryResponse

    @GET("stories")
    suspend fun getStoriesLocation(
        @Query("location") location: Int
    ): GetStoryResponse

    @GET("stories")
    fun getStoriesLocation2(
        @Query("location") location: Int = 1
    ): Call<GetStoryResponse>

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String
    ): DetailStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory2(
        @Path("id") id: String
    ): DetailStoryResponse

    @GET("stories/{id}")
    fun getDetailStory3(
        @Path("id") id: String
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): PostStoryResponse

    @Multipart
    @POST("stories")
    fun postStory2(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<PostStoryResponse>
}

