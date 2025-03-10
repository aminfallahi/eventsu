package com.aminfallahi.eventsu.general.auth

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Patterns
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import com.aminfallahi.eventsu.general.common.SingleLiveEvent
import com.aminfallahi.eventsu.general.data.Network
import timber.log.Timber

class LoginFragmentViewModel(
    private val authService: AuthService,
    private val network: Network
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val progress = MutableLiveData<Boolean>()
    val user = MutableLiveData<User>()
    val error = SingleLiveEvent<String>()
    val showNoInternetDialog = MutableLiveData<Boolean>()
    val requestTokenSuccess = MutableLiveData<Boolean>()
    val isCorrectEmail = MutableLiveData<Boolean>()
    val loggedIn = SingleLiveEvent<Boolean>()

    fun isLoggedIn() = authService.isLoggedIn()

    fun login(email: String, password: String) {
        if (!isConnected()) return
        if (hasErrors(email, password)) return
        compositeDisposable.add(authService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progress.value = true
                }.doFinally {
                    progress.value = false
                }.subscribe({
                    loggedIn.value = true
                }, {
                    error.value = "Unable to Login. Please check your credentials"
                }))
    }

    private fun hasErrors(email: String?, password: String?): Boolean {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            error.value = "Email or Password cannot be empty!"
            return true
        }
        return false
    }

    fun sendResetPasswordEmail(email: String) {
        if (!isConnected()) return
        compositeDisposable.add(authService.sendResetPasswordEmail(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    progress.value = true
                }.doFinally {
                    progress.value = false
                }.subscribe({
                    requestTokenSuccess.value = verifyMessage(it.message)
                }, {
                    error.value = "Email address not present in server. Please check your email"
                }))
    }

    fun verifyMessage(message: String): Boolean {
        if (message.equals("Email Sent")) {
            return true
        }
        return false
    }

    fun fetchProfile() {
        if (!isConnected()) return
        compositeDisposable.add(authService.getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe({
                    progress.value = true
                }).doFinally {
                    progress.value = false
                }.subscribe({ it ->
                    Timber.d("User Fetched")
                    user.value = it
                }) {
                    Timber.e(it, "Failure")
                    error.value = "Failure"
        })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun checkEmail(email: String) {
        isCorrectEmail.value = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isConnected(): Boolean {
        val isConnected = network.isNetworkConnected()
        if (!isConnected) showNoInternetDialog.value = true
        return isConnected
    }
}
