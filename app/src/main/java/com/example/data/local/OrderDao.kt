package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrders(): Flow<List<TopUpOrder>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: String): TopUpOrder?

    @Query("SELECT * FROM orders WHERE status = :status ORDER BY timestamp DESC")
    fun getOrdersByStatus(status: String): Flow<List<TopUpOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: TopUpOrder): Long

    @Query("UPDATE orders SET status = :status WHERE orderId = :orderId")
    suspend fun updateStatus(orderId: String, status: String)

    @Delete
    suspend fun deleteOrder(order: TopUpOrder)

    @Query("DELETE FROM orders")
    suspend fun clearAllOrders()
}

@Dao
interface SavedUidDao {
    @Query("SELECT * FROM saved_uids ORDER BY lastUsed DESC LIMIT 6")
    fun getRecentUids(): Flow<List<SavedUid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUid(savedUid: SavedUid)

    @Query("DELETE FROM saved_uids WHERE uid = :uid")
    suspend fun deleteUid(uid: String)
}
