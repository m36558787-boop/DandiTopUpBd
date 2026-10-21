package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_uids")
data class SavedUid(
    @PrimaryKey
    val uid: String,
    val nickname: String = "Player",
    val lastUsed: Long = System.currentTimeMillis()
)
