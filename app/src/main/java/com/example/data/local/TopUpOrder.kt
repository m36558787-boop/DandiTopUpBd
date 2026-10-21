package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class TopUpOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: String,
    val shop: String = "DENDI TOP UP BD",
    val uid: String,
    val diamonds: Int,
    val packageName: String,
    val price: Int,
    val paymentMethod: String,
    val trxId: String,
    val status: String = "Pending", // "Pending", "Processing", "Completed", "Cancelled"
    val timestamp: Long = System.currentTimeMillis(),
    val serverRegion: String = "BD Server",
    val note: String = ""
)
