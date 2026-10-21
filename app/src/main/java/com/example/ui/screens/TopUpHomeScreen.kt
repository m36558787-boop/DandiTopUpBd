package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedUid
import com.example.data.model.DiamondPackage
import com.example.data.model.PackageCategory
import com.example.data.model.PaymentMethodInfo
import com.example.ui.components.PackageSection
import com.example.ui.components.PaymentSection
import com.example.ui.components.PlayerInfoSection
import com.example.ui.components.TopUpBanner
import com.example.ui.components.TopUpHeader
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.TextSecondary

@Composable
fun TopUpHomeScreen(
    packages: List<DiamondPackage>,
    selectedPackage: DiamondPackage?,
    selectedCategory: PackageCategory,
    onCategorySelected: (PackageCategory) -> Unit,
    onPackageSelected: (DiamondPackage) -> Unit,
    playerUid: String,
    uidError: String?,
    onUidChange: (String) -> Unit,
    recentUids: List<SavedUid>,
    onRecentUidClick: (String) -> Unit,
    selectedPayment: PaymentMethodInfo,
    onPaymentSelected: (PaymentMethodInfo) -> Unit,
    trxId: String,
    trxError: String?,
    onTrxIdChange: (String) -> Unit,
    isSubmitting: Boolean,
    isServerOnline: Boolean,
    noticeText: String,
    onAdminClick: () -> Unit,
    onOrderNow: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .testTag("topup_home_screen")
    ) {
        item {
            TopUpHeader(
                isServerOnline = isServerOnline,
                onAdminClick = onAdminClick
            )
        }

        item {
            TopUpBanner(noticeText = noticeText)
        }

        item {
            PackageSection(
                packages = packages,
                selectedPackage = selectedPackage,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected,
                onPackageSelected = onPackageSelected
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            PlayerInfoSection(
                playerUid = playerUid,
                uidError = uidError,
                onUidChange = onUidChange,
                recentUids = recentUids,
                onRecentUidClick = onRecentUidClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            PaymentSection(
                selectedPayment = selectedPayment,
                onPaymentSelected = onPaymentSelected,
                trxId = trxId,
                trxError = trxError,
                onTrxIdChange = onTrxIdChange,
                selectedPackage = selectedPackage,
                isSubmitting = isSubmitting,
                onOrderNow = onOrderNow,
                onShowToast = onShowToast
            )
        }

        item {
            // Footer note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DeepNavy)
                    .padding(14.dp)
            ) {
                Text(
                    text = "© 2026 DENDI TOP UP BD • বাংলাদেশের সবচেয়ে বিশ্বস্ত ও দ্রুততম ফ্রি ফায়ার ডায়মন্ড টপ-আপ প্ল্যাটফর্ম।",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(60.dp)) // padding for bottom bar
        }
    }
}
