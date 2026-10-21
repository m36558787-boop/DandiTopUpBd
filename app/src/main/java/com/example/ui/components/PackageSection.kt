package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DiamondPackage
import com.example.data.model.PackageCategory
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSelected
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.HotOrange
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun PackageSection(
    packages: List<DiamondPackage>,
    selectedPackage: DiamondPackage?,
    selectedCategory: PackageCategory,
    onCategorySelected: (PackageCategory) -> Unit,
    onPackageSelected: (DiamondPackage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // Section Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Diamond Package",
                    color = NeonCyan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "ইনস্ট্যান্ট টপ-আপ",
                color = MintGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Category Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedCategory.ordinal,
            containerColor = DeepNavy,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedCategory.ordinal]),
                    height = 2.dp,
                    color = NeonCyan
                )
            },
            divider = {}
        ) {
            PackageCategory.values().forEach { category ->
                val selected = selectedCategory == category
                Tab(
                    selected = selected,
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier.testTag("category_tab_${category.name}"),
                    text = {
                        Text(
                            text = category.labelBn,
                            color = if (selected) NeonCyan else TextSecondary,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Filtered packages
        val filtered = packages.filter { it.category == selectedCategory }

        // 2-Column Grid using paired rows (matches HTML display: grid; grid-template-columns: 1fr 1fr; gap: 12px)
        val chunked = filtered.chunked(2)
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            chunked.forEach { rowPackages ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowPackages.forEach { pkg ->
                        val isSelected = selectedPackage?.id == pkg.id
                        PackageCard(
                            pkg = pkg,
                            isSelected = isSelected,
                            onSelect = { onPackageSelected(pkg) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // If odd item in row, fill empty space
                    if (rowPackages.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun PackageCard(
    pkg: DiamondPackage,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) NeonCyan else CardBorder,
        label = "cardBorderColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) CardSelected else CardBackground,
        label = "cardBgColor"
    )

    Box(
        modifier = modifier
            .testTag("package_${pkg.id}")
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onSelect)
            .padding(vertical = 14.dp, horizontal = 10.dp)
    ) {
        // Selection Checkmark pill
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(NeonCyan),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = DeepNavy,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Badge if present
        pkg.badge?.let { badgeText ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (badgeText.contains("Hot") || badgeText.contains("🔥")) HotOrange
                        else if (badgeText.contains("Best") || badgeText.contains("⭐")) GoldenYellow
                        else Color(0xFF1E3A5F)
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = if (pkg.badge != null) 14.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Diamond Icon with soft glow
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) NeonCyan.copy(alpha = 0.2f)
                        else DeepNavy
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Diamond,
                    contentDescription = null,
                    tint = if (isSelected) NeonCyan else Color(0xFF64B5F6),
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Diamond count
            Text(
                text = "💎 ${pkg.diamonds} Diamonds",
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            if (pkg.bonusDiamonds > 0) {
                Text(
                    text = "+${pkg.bonusDiamonds} বোনাস",
                    color = GoldenYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            } else if (pkg.description.isNotEmpty()) {
                Text(
                    text = pkg.description,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price tag matching HTML .price { color: #00ffae; font-weight: bold }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isSelected) Color(0xFF0D332B) else DeepNavy
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "৳${pkg.price}",
                    color = MintGreen,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}
