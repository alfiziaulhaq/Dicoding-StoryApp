package com.dicoding.alfistoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.alfistoryapp.data.preferences.SessionModel
import com.dicoding.alfistoryapp.data.preferences.SessionPreference
import com.dicoding.alfistoryapp.data.response.ListStoryItem
import com.dicoding.alfistoryapp.data.retrofit.ApiConfig
import com.dicoding.alfistoryapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SessionRepository private constructor(
    private val sessionPreference: SessionPreference,
    private val api: ApiService,
    private val apitoken: ApiService
) {
    //repository for api request
    suspend fun signup(name: String, email: String, password: String) =
        api.register(name, email, password)

    suspend fun login(email: String, password: String) =
        api.login(email, password)

    suspend fun getStories() =
        apitoken.getStories()

    fun getStories2(token:String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                StoryPagingSource(api,"Bearer $token")
            }
        ).liveData
    }

    suspend fun getDetailStory(id:String)=
        apitoken.getDetailStory(id)

    suspend fun getDetailStory2(token:String,id:String)=
        ApiConfig.getApiToken(token).getDetailStory2(id)

    suspend fun  postStory(image: MultipartBody.Part, description: RequestBody)=
        apitoken.postStory(image,description)


    //repository for preference of session
    suspend fun saveSession(user: SessionModel) {
        sessionPreference.saveSession(user)
    }

    fun getSession(): Flow<SessionModel> {
        return sessionPreference.getSession()
    }

    suspend fun logout() {
        sessionPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: SessionRepository? = null
        fun getInstance(
            sessionPreference: SessionPreference,
            api: ApiService,
            apitoken: ApiService
        ): SessionRepository =
            instance ?: synchronized(this) {
                instance ?: SessionRepository(sessionPreference, api, apitoken)
            }.also { instance = it }
    }
}