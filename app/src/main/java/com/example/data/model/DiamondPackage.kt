package com.example.data.model

enum class PackageCategory(val labelBn: String, val labelEn: String) {
    REGULAR("💎 ডায়মন্ড প্যাক", "Diamonds"),
    MEMBERSHIP("🎟️ মেম্বারশিপ ও পাস", "Passes"),
    SPECIAL("🔥 স্পেশাল অফার", "Hot Offers")
}

data class DiamondPackage(
    val id: String,
    val diamonds: Int,
    val bonusDiamonds: Int = 0,
    val price: Int,
    val title: String,
    val category: PackageCategory = PackageCategory.REGULAR,
    val badge: String? = null,
    val description: String = ""
) {
    val totalDiamonds: Int
        get() = diamonds + bonusDiamonds
}
