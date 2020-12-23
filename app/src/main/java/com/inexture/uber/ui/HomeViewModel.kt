package com.inexture.uber.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.inexture.uber.data.AppRepository
import com.inexture.uber.model.Repo
import com.inexture.uber.network.resources.CoroutineScopedViewModel
import com.inexture.uber.network.resources.Resource
import java.util.*

class HomeViewModel : CoroutineScopedViewModel() {

    private var mQuery = MutableLiveData<String>()

    private var query: LiveData<String> = mQuery

    private var isRefresh = false

    val results: LiveData<Resource<List<Repo>?>> = Transformations
        .switchMap(query) { search ->
            if (search.isNullOrBlank()) {
                null
            } else {
                with(AppRepository) {
                    val result = searchRepositories(search, list.value, isRefresh)
                    isRefresh = false
                    result
                }
            }
        }


    val list: LiveData<List<Repo>> = Transformations.map(results) {
        it.data
    }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        mQuery.value = input
    }

    fun refreshRepos() {
        isRefresh = true
        setQuery(query.value.orEmpty())
    }
}