package com.inexture.uber.localdb

import androidx.room.*
import com.inexture.uber.model.Repo


@Dao
interface RepoDao {
    @Query("SELECT * FROM repo")
    fun getAll(): List<Repo>

    @Query("SELECT * FROM repo WHERE repo.fullName LIKE (:query)")
    fun getQueriedResult(query: String): List<Repo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg repos: Repo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRepos(repositories: List<Repo>)


    @Delete
    fun delete(repo: Repo)
}