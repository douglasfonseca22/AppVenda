package com.example.appvendas2.data.dao

import androidx.room.*
import com.example.appvendas2.data.models.Client
import com.example.appvendas2.data.models.Item
import com.example.appvendas2.data.models.OrderEntity

@Dao
interface UserDAO {

    @Insert
    suspend fun addUser(item: Item)

    @Query("SELECT * FROM item ORDER BY id DESC")
    suspend fun getAllUser(): List<Item>

}

@Dao
interface OrderDao {
    @Insert
    suspend fun addOrder(orderEntity: OrderEntity)

    @Query("SELECT * FROM orders")
    suspend fun getOrders(): List<OrderEntity>

    @Query("SELECT * FROM orders INNER JOIN client WHERE orders.id = :orderId")
    suspend fun getOrderById(orderId: Int): OrderEntity?
}

@Dao
interface ClientDao {
    @Insert
    suspend fun addClient(client: Client)

    @Query("SELECT * FROM client ORDER BY id DESC")
    suspend fun getAllClient(): List<Client>

    @Query("SELECT MAX(id) FROM client")
    fun getLastOrderId(): Int?
}