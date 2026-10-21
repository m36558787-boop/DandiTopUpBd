package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.PaymentMethodInfo
import com.example.ui.theme.BkashPink
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketViolet
import com.example.ui.theme.UpayNavy

class AdminPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("dendi_admin_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ADMIN_PASSWORD = "key_admin_password"
        private const val KEY_BKASH_NUMBER = "key_bkash_number"
        private const val KEY_NAGAD_NUMBER = "key_nagad_number"
        private const val KEY_ROCKET_NUMBER = "key_rocket_number"
        private const val KEY_UPAY_NUMBER = "key_upay_number"
        private const val KEY_NOTICE_TEXT = "key_notice_text"
        private const val KEY_SERVER_ONLINE = "key_server_online"

        const val DEFAULT_PASSWORD = "admin123"
        const val DEFAULT_BKASH = "01859992211"
        const val DEFAULT_NAGAD = "01788334455"
        const val DEFAULT_ROCKET = "019123344558"
        const val DEFAULT_UPAY = "01600998877"
        const val DEFAULT_NOTICE = "⚡ ফাস্ট ডেলিভারি ৫-১৫ মিনিট | ১০০% নিরাপদ | bKash ও Nagad এ ইনস্ট্যান্ট কনফার্মেশন"
    }

    var adminPassword: String
        get() = prefs.getString(KEY_ADMIN_PASSWORD, DEFAULT_PASSWORD) ?: DEFAULT_PASSWORD
        set(value) = prefs.edit().putString(KEY_ADMIN_PASSWORD, value).apply()

    var bkashNumber: String
        get() = prefs.getString(KEY_BKASH_NUMBER, DEFAULT_BKASH) ?: DEFAULT_BKASH
        set(value) = prefs.edit().putString(KEY_BKASH_NUMBER, value).apply()

    var nagadNumber: String
        get() = prefs.getString(KEY_NAGAD_NUMBER, DEFAULT_NAGAD) ?: DEFAULT_NAGAD
        set(value) = prefs.edit().putString(KEY_NAGAD_NUMBER, value).apply()

    var rocketNumber: String
        get() = prefs.getString(KEY_ROCKET_NUMBER, DEFAULT_ROCKET) ?: DEFAULT_ROCKET
        set(value) = prefs.edit().putString(KEY_ROCKET_NUMBER, value).apply()

    var upayNumber: String
        get() = prefs.getString(KEY_UPAY_NUMBER, DEFAULT_UPAY) ?: DEFAULT_UPAY
        set(value) = prefs.edit().putString(KEY_UPAY_NUMBER, value).apply()

    var noticeText: String
        get() = prefs.getString(KEY_NOTICE_TEXT, DEFAULT_NOTICE) ?: DEFAULT_NOTICE
        set(value) = prefs.edit().putString(KEY_NOTICE_TEXT, value).apply()

    var isServerOnline: Boolean
        get() = prefs.getBoolean(KEY_SERVER_ONLINE, true)
        set(value) = prefs.edit().putBoolean(KEY_SERVER_ONLINE, value).apply()

    fun verifyPassword(input: String): Boolean {
        return input.trim() == adminPassword
    }

    fun getPaymentProviders(): List<PaymentMethodInfo> {
        val bNum = bkashNumber
        val nNum = nagadNumber
        val rNum = rocketNumber
        val uNum = upayNumber

        return listOf(
            PaymentMethodInfo(
                id = "bKash",
                name = "bKash",
                type = "Personal (Send Money)",
                number = bNum,
                brandColor = BkashPink,
                iconEmoji = "🌸",
                steps = listOf(
                    "বিকাশ অ্যাপ ওপেন করুন বা *247# ডায়াল করুন।",
                    "‘Send Money’ অপশন বেছে নিন।",
                    "নম্বর লিখুন: $bNum",
                    "প্যাকেজ অনুযায়ী সঠিক টাকার পরিমাণ দিন।",
                    "লেনদেন সফল হলে ফিরতি এসএমএস থেকে TrxID কপি করে নিচে বসান।"
                )
            ),
            PaymentMethodInfo(
                id = "Nagad",
                name = "Nagad",
                type = "Personal (Send Money)",
                number = nNum,
                brandColor = NagadOrange,
                iconEmoji = "🔥",
                steps = listOf(
                    "নগদ অ্যাপ খুলুন অথবা *167# ডায়াল করুন।",
                    "‘Send Money’ অপশনে ট্যাপ করুন।",
                    "নম্বর লিখুন: $nNum",
                    "প্যাকেজের মূল্য উল্লেখ করে পিন দিন।",
                    "প্রাপ্ত ট্রানজেকশন আইডি (TrxID) ফর্মটিতে পেস্ট করুন।"
                )
            ),
            PaymentMethodInfo(
                id = "Rocket",
                name = "Rocket",
                type = "Personal (Send Money)",
                number = rNum,
                brandColor = RocketViolet,
                iconEmoji = "🚀",
                steps = listOf(
                    "রকেট অ্যাপ বা *322# ডায়াল করুন।",
                    "‘Send Money’ অপশন নির্বাচন করুন।",
                    "নম্বর দিন: $rNum",
                    "পেমেন্ট শেষ করে TrxID সংগ্রহ করুন।"
                )
            ),
            PaymentMethodInfo(
                id = "Upay",
                name = "Upay",
                type = "Personal (Send Money)",
                number = uNum,
                brandColor = UpayNavy,
                iconEmoji = "⚡",
                steps = listOf(
                    "উপায় অ্যাপ এ যান এবং ‘Send Money’ করুন।",
                    "নম্বর দিন: $uNum",
                    "নির্ধারিত মূল্য পাঠিয়ে TrxID দিন।"
                )
            )
        )
    }
}
