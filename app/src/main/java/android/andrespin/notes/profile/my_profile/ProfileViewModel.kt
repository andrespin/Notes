package android.andrespin.notes.profile.my_profile

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val regPreference: RegPreference
) : BaseViewModel() {

    private var isAuthorized: Boolean = false

    val intent = Channel<ProfileIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> get() = _state

    private fun setStateValue(value: ProfileState) {
        _state.value = value
    }

    init {
        handleIntent()
    }

    private suspend fun initAuthorizationStatus() {
        println("initAuthorizationStatus")
        val reg = regPreference.getRegData()
        isAuthorized = !reg.login.isNullOrBlank() && !reg.password.isNullOrBlank()
        println("reg $reg")
        sendAuthorisationStatus(reg)

    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is ProfileIntent.TurnSyncingOn -> turnSyncingModeOnInDatabase()
                    is ProfileIntent.TurnSyncingOff -> turnSyncingModeOffInDatabase()
                    is ProfileIntent.CheckAuthorization -> initAuthorizationStatus()
                    is ProfileIntent.LogOut -> logOut()
                    is ProfileIntent.LogIn -> logIn(it.login, it.pass)
                }
            }
        }
    }

    private fun logIn(login: String, pass: String) {
        regPreference.setPassword(pass)
        regPreference.setLogin(login)
    }

    private suspend fun logOut() {
        regPreference.removeAllRegData()
        isAuthorized = false
        initAuthorizationStatus()
    }

    private fun turnSyncingModeOffInDatabase() {
        println("On")
        TODO("add false to db")
    }

    private fun turnSyncingModeOnInDatabase() {
        println("Off")
        TODO("add true to db")
    }

    private suspend fun sendAuthorisationStatus(regData: RegData) {
        if (isAuthorized) {
            println("isAuthorized $isAuthorized")
            setStateValue(ProfileState.Idle)
            //   delay(1)
            setStateValue(ProfileState.ProfileIsAuthorized(regData))
        } else {
            println("isAuthorized $isAuthorized")
            setStateValue(ProfileState.Idle)
            //  delay(1)
            setStateValue(ProfileState.ProfileIsNotAuthorized)
        }
    }

}