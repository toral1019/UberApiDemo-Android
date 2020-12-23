package com.inexture.uber.localdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inexture.uber.model.Repo

@Database(entities = [Repo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun repoDao(): RepoDao
}