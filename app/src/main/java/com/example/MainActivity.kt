package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AdminLoginDialog
import com.example.ui.components.OrderConfirmationDialog
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.HelpGuideScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.screens.TopUpHomeScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.CardBorder
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.GoldenYellow
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.TopUpViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TopUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: TopUpViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val recentUids by viewModel.recentUids.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Toast message handler
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Handle back button when on sub-tabs
    BackHandler(enabled = uiState.currentTab != 0) {
        viewModel.setTab(0)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .windowInsetsPadding(WindowInsets.statusBars),
        containerColor = BackgroundDark,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            NavigationBar(
                containerColor = DeepNavy,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.8.dp, CardBorder)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.currentTab == 0,
                    onClick = { viewModel.setTab(0) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = "টপ-আপ"
                        )
                    },
                    label = {
                        Text(
                            text = "টপ-আপ",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepNavy,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_shop")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == 1,
                    onClick = { viewModel.setTab(1) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = "অর্ডার"
                        )
                    },
                    label = {
                        Text(
                            text = "অর্ডার (${allOrders.size})",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepNavy,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_orders")
                )

                NavigationBarItem(
                    selected = uiState.currentTab == 2,
                    onClick = { viewModel.setTab(2) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "হেল্প"
                        )
                    },
                    label = {
                        Text(
                            text = "হেল্প",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.currentTab == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = DeepNavy,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan,
                        unselectedIconColor = TextSecondary,
                        unselectedTextColor = TextSecondary
                    ),
                    modifier = Modifier.testTag("nav_tab_help")
                )

                // If admin is logged in, show Admin shortcut in bottom navigation
                if (uiState.isAdminLoggedIn) {
                    NavigationBarItem(
                        selected = uiState.currentTab == 3,
                        onClick = { viewModel.setTab(3) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = "এডমিন"
                            )
                        },
                        label = {
                            Text(
                                text = "এডমিন",
                                fontSize = 11.sp,
                                fontWeight = if (uiState.currentTab == 3) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepNavy,
                            selectedTextColor = GoldenYellow,
                            indicatorColor = GoldenYellow,
                            unselectedIconColor = GoldenYellow.copy(alpha = 0.7f),
                            unselectedTextColor = GoldenYellow.copy(alpha = 0.7f)
                        ),
                        modifier = Modifier.testTag("nav_tab_admin")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                0 -> TopUpHomeScreen(
                    packages = viewModel.packages,
                    selectedPackage = uiState.selectedPackage,
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelected = { viewModel.selectCategory(it) },
                    onPackageSelected = { viewModel.selectPackage(it) },
                    playerUid = uiState.playerUid,
                    uidError = uiState.uidError,
                    onUidChange = { viewModel.onUidChange(it) },
                    recentUids = recentUids,
                    onRecentUidClick = { viewModel.onRecentUidSelected(it) },
                    selectedPayment = uiState.selectedPayment,
                    onPaymentSelected = { viewModel.selectPayment(it) },
                    trxId = uiState.trxId,
                    trxError = uiState.trxError,
                    onTrxIdChange = { viewModel.onTrxIdChange(it) },
                    isSubmitting = uiState.isSubmitting,
                    isServerOnline = uiState.isServerOnline,
                    noticeText = uiState.noticeText,
                    onAdminClick = { viewModel.openAdminLogin() },
                    onOrderNow = { viewModel.submitOrder() },
                    onShowToast = { viewModel.showToast(it) }
                )

                1 -> OrderHistoryScreen(
                    orders = allOrders,
                    statusFilter = uiState.statusFilter,
                    onStatusFilterChange = { viewModel.setStatusFilter(it) },
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { viewModel.setSearchQuery(it) },
                    onUpdateStatus = { orderId, status -> viewModel.updateOrderStatus(orderId, status) },
                    onDeleteOrder = { viewModel.deleteOrder(it) },
                    onClearAll = { viewModel.clearHistory() },
                    onNavigateToShop = { viewModel.setTab(0) },
                    onShowToast = { viewModel.showToast(it) }
                )

                2 -> HelpGuideScreen(
                    customDiamonds = uiState.customDiamondInput,
                    calculatedPrice = uiState.calculatedPrice,
                    onCustomDiamondChange = { viewModel.updateCustomCalculator(it) },
                    onAdminClick = { viewModel.openAdminLogin() },
                    onShowToast = { viewModel.showToast(it) }
                )

                3 -> AdminPanelScreen(
                    orders = allOrders,
                    onUpdateOrderStatus = { orderId, status -> viewModel.updateOrderStatus(orderId, status) },
                    onDeleteOrder = { viewModel.deleteOrder(it) },
                    initialBkash = viewModel.getBkashNumber(),
                    initialNagad = viewModel.getNagadNumber(),
                    initialRocket = viewModel.getRocketNumber(),
                    initialUpay = viewModel.getUpayNumber(),
                    onSavePaymentNumbers = { bkash, nagad, rocket, upay ->
                        viewModel.savePaymentNumbers(bkash, nagad, rocket, upay)
                    },
                    currentNotice = uiState.noticeText,
                    isServerOnline = uiState.isServerOnline,
                    onSaveNoticeAndStatus = { notice, isOnline ->
                        viewModel.saveNoticeAndStatus(notice, isOnline)
                    },
                    currentPasswordDisplay = viewModel.getAdminPassword(),
                    onChangePassword = { oldPass, newPass ->
                        viewModel.changeAdminPassword(oldPass, newPass)
                    },
                    onLogout = { viewModel.logoutAdmin() },
                    onShowToast = { viewModel.showToast(it) }
                )
            }

            // Order Confirmation Digital Ticket Modal
            if (uiState.showSuccessModal && uiState.lastSubmittedOrder != null) {
                OrderConfirmationDialog(
                    order = uiState.lastSubmittedOrder!!,
                    onDismiss = { viewModel.dismissSuccessModal() },
                    onViewHistory = {
                        viewModel.dismissSuccessModal()
                        viewModel.setTab(1)
                    },
                    onShowToast = { viewModel.showToast(it) }
                )
            }

            // Admin Login Dialog Modal
            if (uiState.showAdminLoginDialog) {
                AdminLoginDialog(
                    errorMessage = uiState.adminLoginError,
                    onDismiss = { viewModel.dismissAdminLogin() },
                    onLogin = { pass -> viewModel.loginAdmin(pass) }
                )
            }
        }
    }
}
