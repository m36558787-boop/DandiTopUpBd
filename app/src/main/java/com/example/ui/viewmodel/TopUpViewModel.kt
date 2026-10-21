package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AdminPreferences
import com.example.data.local.AppDatabase
import com.example.data.local.SavedUid
import com.example.data.local.TopUpOrder
import com.example.data.model.DiamondPackage
import com.example.data.model.PackageCategory
import com.example.data.model.PaymentMethodInfo
import com.example.data.repository.TopUpRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TopUpUiState(
    val selectedCategory: PackageCategory = PackageCategory.REGULAR,
    val selectedPackage: DiamondPackage? = null,
    val playerUid: String = "",
    val uidError: String? = null,
    val selectedPayment: PaymentMethodInfo,
    val trxId: String = "",
    val trxError: String? = null,
    val isSubmitting: Boolean = false,
    val lastSubmittedOrder: TopUpOrder? = null,
    val showSuccessModal: Boolean = false,
    val statusFilter: String = "All",
    val searchQuery: String = "",
    val toastMessage: String? = null,
    val currentTab: Int = 0, // 0: Top-Up Shop, 1: Order History, 2: Help & Guide, 3: Admin Panel
    // Custom Calculator
    val customDiamondInput: String = "100",
    val calculatedPrice: Int = 75,
    // Admin state
    val isAdminLoggedIn: Boolean = false,
    val showAdminLoginDialog: Boolean = false,
    val adminLoginError: String? = null,
    val paymentProviders: List<PaymentMethodInfo> = emptyList(),
    val isServerOnline: Boolean = true,
    val noticeText: String = "",
    val adminTab: Int = 0 // 0: Orders, 1: Payment Numbers, 2: Settings
)

class TopUpViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TopUpRepository
    private val adminPrefs = AdminPreferences(application)

    private val _uiState: MutableStateFlow<TopUpUiState>
    val uiState: StateFlow<TopUpUiState>

    val packages: List<DiamondPackage>

    val allOrders: StateFlow<List<TopUpOrder>>
    val recentUids: StateFlow<List<SavedUid>>

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TopUpRepository(database)
        packages = repository.getPackages()

        val initialProviders = adminPrefs.getPaymentProviders()
        val defaultPkg = packages.firstOrNull { it.diamonds == 25 } ?: packages.first()

        _uiState = MutableStateFlow(
            TopUpUiState(
                selectedPackage = defaultPkg,
                selectedPayment = initialProviders[0],
                paymentProviders = initialProviders,
                isServerOnline = adminPrefs.isServerOnline,
                noticeText = adminPrefs.noticeText
            )
        )
        uiState = _uiState.asStateFlow()

        allOrders = repository.allOrders.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        recentUids = repository.recentUids.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun selectCategory(category: PackageCategory) {
        val firstInCat = packages.firstOrNull { it.category == category }
        _uiState.value = _uiState.value.copy(
            selectedCategory = category,
            selectedPackage = firstInCat ?: _uiState.value.selectedPackage
        )
    }

    fun selectPackage(pkg: DiamondPackage) {
        _uiState.value = _uiState.value.copy(
            selectedPackage = pkg,
            selectedCategory = pkg.category
        )
    }

    fun onUidChange(newUid: String) {
        val filtered = newUid.filter { it.isDigit() }.take(12)
        _uiState.value = _uiState.value.copy(
            playerUid = filtered,
            uidError = null
        )
    }

    fun onTrxIdChange(newTrx: String) {
        val filtered = newTrx.filter { it.isLetterOrDigit() }.take(16).uppercase()
        _uiState.value = _uiState.value.copy(
            trxId = filtered,
            trxError = null
        )
    }

    fun selectPayment(provider: PaymentMethodInfo) {
        _uiState.value = _uiState.value.copy(selectedPayment = provider)
    }

    fun setTab(index: Int) {
        _uiState.value = _uiState.value.copy(currentTab = index)
    }

    fun setAdminTab(index: Int) {
        _uiState.value = _uiState.value.copy(adminTab = index)
    }

    fun setStatusFilter(filter: String) {
        _uiState.value = _uiState.value.copy(statusFilter = filter)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onRecentUidSelected(uid: String) {
        _uiState.value = _uiState.value.copy(
            playerUid = uid,
            uidError = null,
            toastMessage = "UID $uid সিলেক্ট করা হয়েছে"
        )
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }

    fun showToast(msg: String) {
        _uiState.value = _uiState.value.copy(toastMessage = msg)
    }

    fun dismissSuccessModal() {
        _uiState.value = _uiState.value.copy(showSuccessModal = false)
    }

    fun submitOrder() {
        val state = _uiState.value
        val uid = state.playerUid.trim()
        val trx = state.trxId.trim()
        val pkg = state.selectedPackage

        var hasError = false
        var uidErr: String? = null
        var trxErr: String? = null

        if (uid.isEmpty()) {
            uidErr = "দয়া করে Free Fire Player UID লিখুন"
            hasError = true
        } else if (uid.length < 7) {
            uidErr = "সঠিক প্লেয়ার UID লিখুন (কমপক্ষে ৭-১২ ডিজিট)"
            hasError = true
        }

        if (trx.isEmpty()) {
            trxErr = "পেমেন্টের ট্রানজেকশন আইডি (TrxID) লিখুন"
            hasError = true
        } else if (trx.length < 6) {
            trxErr = "সঠিক TrxID দিন (কমপক্ষে ৬ অক্ষর)"
            hasError = true
        }

        if (pkg == null) {
            hasError = true
            showToast("দয়া করে একটি ডায়মন্ড প্যাকেজ বেছে নিন")
        }

        if (hasError) {
            _uiState.value = state.copy(
                uidError = uidErr,
                trxError = trxErr
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true)
            delay(500)

            val order = repository.createOrder(
                uid = uid,
                pkg = pkg!!,
                paymentMethod = state.selectedPayment.name,
                trxId = trx
            )

            _uiState.value = _uiState.value.copy(
                isSubmitting = false,
                lastSubmittedOrder = order,
                showSuccessModal = true,
                trxId = "",
                toastMessage = "অর্ডার সফলভাবে জমা হয়েছে!"
            )
        }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, newStatus)
            showToast("অর্ডার স্ট্যাটাস: $newStatus")
        }
    }

    fun deleteOrder(order: TopUpOrder) {
        viewModelScope.launch {
            repository.deleteOrder(order)
            showToast("অর্ডার ডিলিট করা হয়েছে")
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            showToast("সব হিস্টোরি মুছে ফেলা হয়েছে")
        }
    }

    fun updateCustomCalculator(diamondsStr: String) {
        val diamonds = diamondsStr.filter { it.isDigit() }.toIntOrNull() ?: 0
        val price = when {
            diamonds <= 0 -> 0
            diamonds <= 50 -> (diamonds * 0.9).toInt()
            diamonds <= 200 -> (diamonds * 0.75).toInt()
            diamonds <= 1000 -> (diamonds * 0.70).toInt()
            else -> (diamonds * 0.67).toInt()
        }
        _uiState.value = _uiState.value.copy(
            customDiamondInput = diamondsStr.filter { it.isDigit() }.take(6),
            calculatedPrice = price
        )
    }

    // --- Admin Operations ---

    fun openAdminLogin() {
        if (_uiState.value.isAdminLoggedIn) {
            _uiState.value = _uiState.value.copy(currentTab = 3)
        } else {
            _uiState.value = _uiState.value.copy(
                showAdminLoginDialog = true,
                adminLoginError = null
            )
        }
    }

    fun dismissAdminLogin() {
        _uiState.value = _uiState.value.copy(
            showAdminLoginDialog = false,
            adminLoginError = null
        )
    }

    fun loginAdmin(password: String) {
        if (adminPrefs.verifyPassword(password)) {
            _uiState.value = _uiState.value.copy(
                isAdminLoggedIn = true,
                showAdminLoginDialog = false,
                adminLoginError = null,
                currentTab = 3,
                toastMessage = "এডমিন প্যানেলে স্বাগতম!"
            )
        } else {
            _uiState.value = _uiState.value.copy(
                adminLoginError = "ভুল পাসওয়ার্ড! সঠিক পাসওয়ার্ড দিন।"
            )
        }
    }

    fun logoutAdmin() {
        _uiState.value = _uiState.value.copy(
            isAdminLoggedIn = false,
            currentTab = 0,
            toastMessage = "এডমিন লগআউট সম্পন্ন"
        )
    }

    fun getBkashNumber(): String = adminPrefs.bkashNumber
    fun getNagadNumber(): String = adminPrefs.nagadNumber
    fun getRocketNumber(): String = adminPrefs.rocketNumber
    fun getUpayNumber(): String = adminPrefs.upayNumber
    fun getAdminPassword(): String = adminPrefs.adminPassword

    fun savePaymentNumbers(bkash: String, nagad: String, rocket: String, upay: String) {
        adminPrefs.bkashNumber = bkash.trim()
        adminPrefs.nagadNumber = nagad.trim()
        adminPrefs.rocketNumber = rocket.trim()
        adminPrefs.upayNumber = upay.trim()

        val updatedProviders = adminPrefs.getPaymentProviders()
        val curSelected = _uiState.value.selectedPayment
        val newSelected = updatedProviders.firstOrNull { it.id == curSelected.id } ?: updatedProviders[0]

        _uiState.value = _uiState.value.copy(
            paymentProviders = updatedProviders,
            selectedPayment = newSelected,
            toastMessage = "পেমেন্ট নম্বর সফলভাবে সংরক্ষিত হয়েছে!"
        )
    }

    fun saveNoticeAndStatus(notice: String, isOnline: Boolean) {
        adminPrefs.noticeText = notice.trim()
        adminPrefs.isServerOnline = isOnline
        _uiState.value = _uiState.value.copy(
            noticeText = notice.trim(),
            isServerOnline = isOnline,
            toastMessage = "সার্ভার সেটিংস আপডেট হয়েছে!"
        )
    }

    fun changeAdminPassword(oldPass: String, newPass: String): Boolean {
        if (!adminPrefs.verifyPassword(oldPass)) {
            showToast("বর্তমান পাসওয়ার্ড সঠিক নয়!")
            return false
        }
        if (newPass.length < 4) {
            showToast("নতুন পাসওয়ার্ড কমপক্ষে ৪ অক্ষরের হতে হবে")
            return false
        }
        adminPrefs.adminPassword = newPass.trim()
        showToast("এডমিন পাসওয়ার্ড সফলভাবে পরিবর্তিত হয়েছে!")
        return true
    }
}
