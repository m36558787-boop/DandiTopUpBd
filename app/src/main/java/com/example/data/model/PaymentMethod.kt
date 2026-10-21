package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.BkashPink
import com.example.ui.theme.NagadOrange
import com.example.ui.theme.RocketViolet
import com.example.ui.theme.UpayNavy

data class PaymentMethodInfo(
    val id: String,
    val name: String,
    val type: String,
    val number: String,
    val brandColor: Color,
    val iconEmoji: String,
    val steps: List<String>
)

object PaymentProviders {
    val providers = listOf(
        PaymentMethodInfo(
            id = "bKash",
            name = "bKash",
            type = "Personal (Send Money)",
            number = "01859992211",
            brandColor = BkashPink,
            iconEmoji = "🌸",
            steps = listOf(
                "বিকাশ অ্যাপ ওপেন করুন বা *247# ডায়াল করুন।",
                "‘Send Money’ অপশন বেছে নিন।",
                "নম্বর লিখুন: 01859992211",
                "প্যাকেজ অনুযায়ী সঠিক টাকার পরিমাণ দিন।",
                "লেনদেন সফল হলে ফিরতি এসএমএস থেকে TrxID কপি করে নিচে বসান।"
            )
        ),
        PaymentMethodInfo(
            id = "Nagad",
            name = "Nagad",
            type = "Personal (Send Money)",
            number = "01788334455",
            brandColor = NagadOrange,
            iconEmoji = "🔥",
            steps = listOf(
                "নগদ অ্যাপ খুলুন অথবা *167# ডায়াল করুন।",
                "‘Send Money’ অপশনে ট্যাপ করুন।",
                "নম্বর লিখুন: 01788334455",
                "প্যাকেজের মূল্য উল্লেখ করে পিন দিন।",
                "প্রাপ্ত ট্রানজেকশন আইডি (TrxID) ফর্মটিতে পেস্ট করুন।"
            )
        ),
        PaymentMethodInfo(
            id = "Rocket",
            name = "Rocket",
            type = "Personal (Send Money)",
            number = "019123344558",
            brandColor = RocketViolet,
            iconEmoji = "🚀",
            steps = listOf(
                "রকেট অ্যাপ বা *322# ডায়াল করুন।",
                "‘Send Money’ অপশন নির্বাচন করুন।",
                "নম্বর দিন: 019123344558",
                "পেমেন্ট শেষ করে TrxID সংগ্রহ করুন।"
            )
        ),
        PaymentMethodInfo(
            id = "Upay",
            name = "Upay",
            type = "Personal (Send Money)",
            number = "01600998877",
            brandColor = UpayNavy,
            iconEmoji = "⚡",
            steps = listOf(
                "উপায় অ্যাপ এ যান এবং ‘Send Money’ করুন।",
                "নম্বর দিন: 01600998877",
                "নির্ধারিত মূল্য পাঠিয়ে TrxID দিন।"
            )
        )
    )
}
