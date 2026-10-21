package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TopUpOrder
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.CardBackground
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardSelected
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.MintGreen
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminPanelScreen(
    orders: List<TopUpOrder>,
    onUpdateOrderStatus: (String, String) -> Unit,
    onDeleteOrder: (TopUpOrder) -> Unit,
    initialBkash: String,
    initialNagad: String,
    initialRocket: String,
    initialUpay: String,
    onSavePaymentNumbers: (String, String, String, String) -> Unit,
    currentNotice: String,
    isServerOnline: Boolean,
    onSaveNoticeAndStatus: (String, Boolean) -> Unit,
    currentPasswordDisplay: String,
    onChangePassword: (String, String) -> Boolean,
    onLogout: () -> Unit,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Orders, 1: Numbers, 2: Settings

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .testTag("admin_panel_screen")
    ) {
        // Admin Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DeepNavy)
                .border(0.8.dp, CardBorder)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NeonCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "👑 এডমিন প্যানেল",
                        color = NeonCyan,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "DENDI TOP UP BD • কন্ট্রোল রুম",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            IconButton(
                onClick = onLogout,
                modifier = Modifier.testTag("admin_logout_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Logout",
                    tint = ErrorRed,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = DeepNavy,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 2.dp,
                    color = NeonCyan
                )
            },
            divider = {}
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "📦 অর্ডার (${orders.size})",
                        color = if (selectedTab == 0) NeonCyan else TextSecondary,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "💳 পেমেন্ট নম্বর",
                        color = if (selectedTab == 1) NeonCyan else TextSecondary,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        text = "⚙️ সেটিংস ও পাসওয়ার্ড",
                        color = if (selectedTab == 2) NeonCyan else TextSecondary,
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            )
        }

        when (selectedTab) {
            0 -> AdminOrdersTab(
                orders = orders,
                onUpdateOrderStatus = onUpdateOrderStatus,
                onDeleteOrder = onDeleteOrder,
                onCopy = { text, label ->
                    clipboardManager.setText(AnnotatedString(text))
                    onShowToast("$label কপি হয়েছে: $text")
                }
            )
            1 -> AdminPaymentNumbersTab(
                initialBkash = initialBkash,
                initialNagad = initialNagad,
                initialRocket = initialRocket,
                initialUpay = initialUpay,
                onSave = onSavePaymentNumbers
            )
            2 -> AdminSettingsTab(
                currentNotice = currentNotice,
                isServerOnline = isServerOnline,
                onSaveNoticeAndStatus = onSaveNoticeAndStatus,
                currentPassword = currentPasswordDisplay,
                onChangePassword = onChangePassword
            )
        }
    }
}

// ---------------- TAB 1: ORDERS ----------------

@Composable
private fun AdminOrdersTab(
    orders: List<TopUpOrder>,
    onUpdateOrderStatus: (String, String) -> Unit,
    onDeleteOrder: (TopUpOrder) -> Unit,
    onCopy: (String, String) -> Unit
) {
    var statusFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }

    val totalRevenue = orders.filter { it.status == "Completed" }.sumOf { it.price }
    val totalDiamonds = orders.filter { it.status == "Completed" }.sumOf { it.diamonds }
    val pendingCount = orders.count { it.status == "Pending" }
    val completedCount = orders.count { it.status == "Completed" }

    val filteredOrders = orders.filter { order ->
        val matchesFilter = if (statusFilter == "All") true else order.status.equals(statusFilter, ignoreCase = true)
        val matchesSearch = if (searchQuery.isBlank()) true else {
            order.orderId.contains(searchQuery, ignoreCase = true) ||
            order.uid.contains(searchQuery, ignoreCase = true) ||
            order.trxId.contains(searchQuery, ignoreCase = true)
        }
        matchesFilter && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Dashboard metric cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricCard(
                    title = "মোট বিক্রয়",
                    value = "৳$totalRevenue",
                    color = MintGreen,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "মোট ডায়মন্ড",
                    value = "$totalDiamonds 💎",
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "অপেক্ষারত",
                    value = "$pendingCount টি",
                    color = GoldenYellow,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("অর্ডার আইডি, UID বা TrxID লিখুন", color = TextSecondary, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Status Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All" to "সব", "Pending" to "পেন্ডিং", "Processing" to "চলমান", "Completed" to "সম্পন্ন").forEach { (key, label) ->
                    val isSel = statusFilter == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSel) NeonCyan else DeepNavy)
                            .clickable { statusFilter = key }
                            .padding(vertical = 7.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSel) DeepNavy else TextSecondary,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        if (filteredOrders.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "কোনো অর্ডার পাওয়া যায়নি", color = TextSecondary, fontSize = 14.sp)
                }
            }
        } else {
            items(filteredOrders, key = { it.orderId }) { order ->
                AdminOrderCard(
                    order = order,
                    onUpdateStatus = onUpdateOrderStatus,
                    onDeleteOrder = onDeleteOrder,
                    onCopy = onCopy
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(CardBackground)
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, color = TextSecondary, fontSize = 10.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, color = color, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun AdminOrderCard(
    order: TopUpOrder,
    onUpdateStatus: (String, String) -> Unit,
    onDeleteOrder: (TopUpOrder) -> Unit,
    onCopy: (String, String) -> Unit
) {
    val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(order.timestamp))

    val statusColor = when (order.status) {
        "Completed" -> MintGreen
        "Processing" -> NeonCyan
        "Cancelled" -> ErrorRed
        else -> GoldenYellow
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "#${order.orderId}", color = NeonCyan, fontSize = 14.sp, fontWeight = FontWeight.Black)
                Text(text = dateStr, color = TextSecondary, fontSize = 11.sp)
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Body
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "💎 ${order.diamonds} Diamonds (${order.packageName})", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(text = "৳${order.price}", color = MintGreen, fontSize = 15.sp, fontWeight = FontWeight.Black)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = CardBorder)

            // UID & Trx
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "UID: ${order.uid}", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy UID",
                        tint = NeonCyan,
                        modifier = Modifier
                            .size(13.dp)
                            .clickable { onCopy(order.uid, "UID") }
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${order.paymentMethod} Trx: ${order.trxId}", color = GoldenYellow, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Trx",
                        tint = GoldenYellow,
                        modifier = Modifier
                            .size(13.dp)
                            .clickable { onCopy(order.trxId, "TrxID") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Admin Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Status pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .border(1.dp, statusColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = order.status, color = statusColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Quick Status change buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Button(
                        onClick = { onUpdateStatus(order.orderId, "Completed") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F3A2E)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(text = "অনুমোদন", color = MintGreen, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onUpdateStatus(order.orderId, "Processing") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A60)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(text = "চলমান", color = NeonCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { onUpdateStatus(order.orderId, "Cancelled") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E1E28)),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(text = "বাতিল", color = ErrorRed, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    IconButton(
                        onClick = { onDeleteOrder(order) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.DeleteOutline, "Delete", tint = TextSecondary, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

// ---------------- TAB 2: PAYMENT NUMBERS ----------------

@Composable
private fun AdminPaymentNumbersTab(
    initialBkash: String,
    initialNagad: String,
    initialRocket: String,
    initialUpay: String,
    onSave: (String, String, String, String) -> Unit
) {
    var bkash by remember { mutableStateOf(initialBkash) }
    var nagad by remember { mutableStateOf(initialNagad) }
    var rocket by remember { mutableStateOf(initialRocket) }
    var upay by remember { mutableStateOf(initialUpay) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "💳 পেমেন্ট নম্বর পরিবর্তন করুন",
                color = NeonCyan,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "এখানে নম্বর পরিবর্তন করলে ব্যবহারকারীরা সাথে সাথে নতুন নম্বর দেখতে পাবে:",
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        item {
            PaymentNumberField(label = "🌸 bKash (Personal)", value = bkash, onValueChange = { bkash = it })
        }

        item {
            PaymentNumberField(label = "🔥 Nagad (Personal)", value = nagad, onValueChange = { nagad = it })
        }

        item {
            PaymentNumberField(label = "🚀 Rocket (Personal)", value = rocket, onValueChange = { rocket = it })
        }

        item {
            PaymentNumberField(label = "⚡ Upay (Personal)", value = upay, onValueChange = { upay = it })
        }

        item {
            Button(
                onClick = { onSave(bkash, nagad, rocket, upay) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = DeepNavy),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Icon(Icons.Default.Save, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "পেমেন্ট নম্বর সংরক্ষণ করুন", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
private fun PaymentNumberField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardBackground,
                unfocusedContainerColor = CardBackground,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ---------------- TAB 3: SETTINGS & PASSWORD ----------------

@Composable
private fun AdminSettingsTab(
    currentNotice: String,
    isServerOnline: Boolean,
    onSaveNoticeAndStatus: (String, Boolean) -> Unit,
    currentPassword: String,
    onChangePassword: (String, String) -> Boolean
) {
    var notice by remember { mutableStateOf(currentNotice) }
    var online by remember { mutableStateOf(isServerOnline) }

    var oldPass by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var passError by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Server & Notice Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📢 সার্ভার স্ট্যাটাস ও ঘোষণা",
                        color = NeonCyan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "টপ-আপ সার্ভার চালু রাখুন", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = if (online) "সার্ভার চালু আছে" else "সার্ভার মেইনটেন্যান্স বন্ধ", color = if (online) MintGreen else ErrorRed, fontSize = 11.sp)
                        }
                        Switch(
                            checked = online,
                            onCheckedChange = { online = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = NeonCyan, checkedTrackColor = Color(0xFF1E3A60))
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "চলমান ব্যানার নোটিশ:", color = TextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = notice,
                        onValueChange = { notice = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DeepNavy,
                            unfocusedContainerColor = DeepNavy,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { onSaveNoticeAndStatus(notice, online) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan, contentColor = DeepNavy),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "ঘোষণা সংরক্ষণ করুন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Change Password Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🔒 এডমিন পাসওয়ার্ড পরিবর্তন",
                        color = NeonCyan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Current Password Info
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DeepNavy)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "বর্তমান সক্রিয় পাসওয়ার্ড: $currentPassword",
                            color = GoldenYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = oldPass,
                        onValueChange = { oldPass = it; passError = null },
                        placeholder = { Text("বর্তমান পাসওয়ার্ড লিখুন", color = TextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DeepNavy,
                            unfocusedContainerColor = DeepNavy,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = newPass,
                        onValueChange = { newPass = it; passError = null },
                        placeholder = { Text("নতুন পাসওয়ার্ড লিখুন (কমপক্ষে ৪ অক্ষর)", color = TextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DeepNavy,
                            unfocusedContainerColor = DeepNavy,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPass,
                        onValueChange = { confirmPass = it; passError = null },
                        placeholder = { Text("নতুন পাসওয়ার্ড নিশ্চিত করুন", color = TextSecondary) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DeepNavy,
                            unfocusedContainerColor = DeepNavy,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (passError != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = passError!!, color = ErrorRed, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (newPass != confirmPass) {
                                passError = "নতুন পাসওয়ার্ড দুটি মিলছে না!"
                            } else if (newPass.length < 4) {
                                passError = "পাসওয়ার্ড কমপক্ষে ৪ অক্ষরের হতে হবে"
                            } else {
                                val success = onChangePassword(oldPass, newPass)
                                if (success) {
                                    oldPass = ""
                                    newPass = ""
                                    confirmPass = ""
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldenYellow, contentColor = DeepNavy),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "পাসওয়ার্ড আপডেট করুন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
