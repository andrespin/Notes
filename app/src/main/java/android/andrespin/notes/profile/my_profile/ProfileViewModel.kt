package android.andrespin.notes.profile.my_profile

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel() {

    private var isAuthorized: Boolean = false

    val intent = Channel<ProfileIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> get() = _state

    private fun setStateValue(value: ProfileState) {
        _state.value = value
    }

    init {
        initAuthorizationStatus()
        sendAuthorisationStatus()
        handleIntent()
    }

    private fun initAuthorizationStatus() {
        // TODO
        isAuthorized = true
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is ProfileIntent.TurnSyncingOn -> turnSyncingModeOnInDatabase()
                    is ProfileIntent.TurnSyncingOff -> turnSyncingModeOffInDatabase()
                }
            }
        }
    }

    private fun turnSyncingModeOffInDatabase() {
        println("On")
        TODO("add false to db")
    }

    private fun turnSyncingModeOnInDatabase() {
        println("Off")
        TODO("add true to db")
    }

    private fun sendAuthorisationStatus() {
        if (isAuthorized) {
            setStateValue(ProfileState.ProfileIsAuthorized)
        } else {
            setStateValue(ProfileState.ProfileIsNotAuthorized)
        }
    }


}