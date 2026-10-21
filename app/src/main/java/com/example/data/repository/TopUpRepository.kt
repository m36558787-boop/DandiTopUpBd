package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.SavedUid
import com.example.data.local.TopUpOrder
import com.example.data.model.DiamondPackage
import com.example.data.model.PackageCategory
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class TopUpRepository(private val database: AppDatabase) {

    private val orderDao = database.orderDao()
    private val savedUidDao = database.savedUidDao()

    val allOrders: Flow<List<TopUpOrder>> = orderDao.getAllOrders()
    val recentUids: Flow<List<SavedUid>> = savedUidDao.getRecentUids()

    fun getPackages(): List<DiamondPackage> {
        return listOf(
            // Regular packages from user HTML
            DiamondPackage(
                id = "pkg_25",
                diamonds = 25,
                price = 25,
                title = "25 Diamonds",
                category = PackageCategory.REGULAR,
                badge = "Starter"
            ),
            DiamondPackage(
                id = "pkg_50",
                diamonds = 50,
                price = 40,
                title = "50 Diamonds",
                category = PackageCategory.REGULAR
            ),
            DiamondPackage(
                id = "pkg_100",
                diamonds = 100,
                bonusDiamonds = 5,
                price = 75,
                title = "100 + 5 Diamonds",
                category = PackageCategory.REGULAR,
                badge = "🔥 Popular"
            ),
            DiamondPackage(
                id = "pkg_210",
                diamonds = 210,
                bonusDiamonds = 10,
                price = 150,
                title = "210 + 10 Diamonds",
                category = PackageCategory.REGULAR,
                badge = "⭐ Best Seller"
            ),
            DiamondPackage(
                id = "pkg_530",
                diamonds = 530,
                bonusDiamonds = 35,
                price = 370,
                title = "530 + 35 Diamonds",
                category = PackageCategory.REGULAR,
                badge = "⚡ Instant"
            ),
            DiamondPackage(
                id = "pkg_1080",
                diamonds = 1080,
                bonusDiamonds = 108,
                price = 740,
                title = "1080 + 108 Diamonds",
                category = PackageCategory.REGULAR,
                badge = "💎 Mega Deal"
            ),
            DiamondPackage(
                id = "pkg_2180",
                diamonds = 2180,
                bonusDiamonds = 220,
                price = 1480,
                title = "2180 + 220 Diamonds",
                category = PackageCategory.REGULAR,
                badge = "👑 Pro Gamer"
            ),
            DiamondPackage(
                id = "pkg_5600",
                diamonds = 5600,
                bonusDiamonds = 600,
                price = 3650,
                title = "5600 + 600 Diamonds",
                category = PackageCategory.REGULAR,
                badge = "🏆 Whale Pack"
            ),

            // Memberships & Passes
            DiamondPackage(
                id = "pass_weekly",
                diamonds = 450,
                price = 160,
                title = "Weekly Membership",
                category = PackageCategory.MEMBERSHIP,
                badge = "🔥 450 💎 Total",
                description = "প্রতিদিন ৫০ ডায়মন্ড এবং বিশেষ ব্যাজ"
            ),
            DiamondPackage(
                id = "pass_monthly",
                diamonds = 2600,
                price = 790,
                title = "Monthly Membership",
                category = PackageCategory.MEMBERSHIP,
                badge = "⭐ 2600 💎 Total",
                description = "৩০ দিনের প্রিমিয়াম বেনিফিট ও ডায়মন্ড"
            ),
            DiamondPackage(
                id = "pass_levelup",
                diamonds = 800,
                price = 150,
                title = "Level Up Pass",
                category = PackageCategory.MEMBERSHIP,
                badge = "800 💎 Level 30",
                description = "লেভেল অনুযায়ী ৮০০ ডায়মন্ড একবারে আনলক"
            ),
            DiamondPackage(
                id = "pass_weekly_lite",
                diamonds = 100,
                price = 45,
                title = "Weekly Lite",
                category = PackageCategory.MEMBERSHIP,
                badge = "সস্তা প্যাক",
                description = "৭ দিনে ১০০ ডায়মন্ড"
            ),

            // Special Offers
            DiamondPackage(
                id = "special_evo",
                diamonds = 350,
                price = 250,
                title = "EVO Access Token Pack",
                category = PackageCategory.SPECIAL,
                badge = "⚡ Gun Skin Boost",
                description = "EVO গান আপগ্রেডের জন্য স্পেশাল অফার"
            ),
            DiamondPackage(
                id = "special_airdrop",
                diamonds = 299,
                price = 95,
                title = "Special Airdrop ৳95",
                category = PackageCategory.SPECIAL,
                badge = "🎁 লিমিটেড",
                description = "স্পেশাল এয়ারড্রপ সরাসরি ইন-গেম টপ-আপ"
            )
        )
    }

    suspend fun createOrder(
        uid: String,
        pkg: DiamondPackage,
        paymentMethod: String,
        trxId: String,
        note: String = ""
    ): TopUpOrder {
        val randomSuffix = Random.nextInt(10000, 99999)
        val orderId = "DND-$randomSuffix"

        val order = TopUpOrder(
            orderId = orderId,
            shop = "DENDI TOP UP BD",
            uid = uid.trim(),
            diamonds = pkg.totalDiamonds,
            packageName = pkg.title,
            price = pkg.price,
            paymentMethod = paymentMethod,
            trxId = trxId.trim().uppercase(),
            status = "Pending",
            timestamp = System.currentTimeMillis(),
            serverRegion = "BD Server",
            note = note
        )

        orderDao.insertOrder(order)
        savedUidDao.insertUid(SavedUid(uid = uid.trim(), nickname = "FreeFire Player", lastUsed = System.currentTimeMillis()))

        return order
    }

    suspend fun updateOrderStatus(orderId: String, newStatus: String) {
        orderDao.updateStatus(orderId, newStatus)
    }

    suspend fun deleteOrder(order: TopUpOrder) {
        orderDao.deleteOrder(order)
    }

    suspend fun clearHistory() {
        orderDao.clearAllOrders()
    }
}
