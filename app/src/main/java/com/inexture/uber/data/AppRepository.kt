package com.inexture.uber.data

import androidx.lifecycle.LiveData
import com.inexture.uber.App
import com.inexture.uber.model.Repo
import com.inexture.uber.network.ApiClient
import com.inexture.uber.network.resources.*
import kotlinx.coroutines.CoroutineScope

object AppRepository {

    fun CoroutineScope.searchRepositories(
        query: String,
        data: List<Repo>?,
        forceLoadFromNetwork: Boolean = false
    ): LiveData<Resource<List<Repo>?>> =

        dataApi<List<Repo>?, APIError>(data) {

            // get data from network
            fromNetwork {
                ApiClient.service.searchRepos(query).getResult()
                    .map { it?.items?.sortedBy { it.id } }
            }

            if (!forceLoadFromNetwork) {
                // get data from local db
                fromLocal { App.db.repoDao().getQueriedResult("%$query%").sortedBy { it.id } }

                // save data in local db
                saveResultInLocal { data: List<Repo>? ->
                    data?.let {
                        App.db.repoDao().insertRepos(it)
                    }
                }
            }
        }
}