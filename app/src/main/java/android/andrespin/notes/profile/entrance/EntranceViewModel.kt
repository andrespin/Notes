package android.andrespin.notes.profile.entrance

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.model.RegData
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.profile.entrance.intent.EntranceIntent
import android.andrespin.notes.profile.entrance.intent.EntranceState
import android.andrespin.notes.profile.my_profile.intent.ProfileIntent
import android.andrespin.notes.profile.my_profile.intent.ProfileState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntranceViewModel
@Inject constructor(
    private val regPreference: RegPreference
) : BaseViewModel() {

    val intent = Channel<EntranceIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<EntranceState>(EntranceState.Idle)
    val state: StateFlow<EntranceState> get() = _state

    private fun setStateValue(value: EntranceState) {
        _state.value = value
    }

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is EntranceIntent.LogIn -> logIn(it.reg)
                }
            }
        }
    }

    private suspend fun logIn(reg: RegData) {

        if (!reg.password.isNullOrBlank() && !reg.login.isNullOrBlank()) {
            regPreference.setPassword(reg.password)
            regPreference.setLogin(reg.login)
            delay(1)
            setStateValue(EntranceState.LogIn)
        } else {
            setStateValue(EntranceState.Error)
        }

    }


}