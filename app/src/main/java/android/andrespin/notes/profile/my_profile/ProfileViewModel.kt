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
        initAuthorizationStatus()

        handleIntent()
    }

    private fun initAuthorizationStatus() {
        val reg = regPreference.getRegData()
        if (reg.login != null && reg.password != null)
            isAuthorized = true

        viewModelScope.launch(Dispatchers.Main) {
            sendAuthorisationStatus(reg)
        }


    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is ProfileIntent.TurnSyncingOn -> turnSyncingModeOnInDatabase()
                    is ProfileIntent.TurnSyncingOff -> turnSyncingModeOffInDatabase()
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

    private fun logOut() {
        regPreference.removeAllRegData()
        isAuthorized = true
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
            setStateValue(ProfileState.Idle)
            delay(1)
            setStateValue(ProfileState.ProfileIsAuthorized(regData))
        } else {
            setStateValue(ProfileState.Idle)
            delay(1)
            setStateValue(ProfileState.ProfileIsNotAuthorized)
        }
    }

}