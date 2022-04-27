package android.andrespin.notes.main

import android.andrespin.notes.main.intent.MainEvent
import android.andrespin.notes.main.intent.MainIntent
import android.andrespin.notes.main.intent.MainState
import android.andrespin.notes.model.di.RegPreference
import android.andrespin.notes.model.hasUserComeBefore
import android.andrespin.notes.notes.intent.NotesState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val regPreference: RegPreference
) : ViewModel() {

    val intent = Channel<MainIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState> get() = _state

    private val _event = MutableSharedFlow<MainEvent>()
    val event: SharedFlow<MainEvent> get() = _event

    init {
        handleIntent()
    }

    private fun handleIntent() {

        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    MainIntent.CheckIfFirst -> check()
                }
            }
        }
    }

    private fun setStateValue(value: MainState) {
        _state.value = value
    }

    private fun check() {

        setStateValue(MainState.Launch)

//        println("regPreference.getHasUserComeBefore() ${regPreference.getHasUserComeBefore()}")
//        if (regPreference.getHasUserComeBefore() == hasUserComeBefore) {
//            setStateValue(MainState.Launch)
//        } else {
//            regPreference.setHasUserComeBefore()
//            setStateValue(MainState.FirstLaunch)
//        }

    }


}