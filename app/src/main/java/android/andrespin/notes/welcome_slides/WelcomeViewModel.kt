package android.andrespin.notes.welcome_slides

import android.andrespin.notes.BaseViewModel
import android.andrespin.notes.main.intent.MainState
import android.andrespin.notes.profile.entrance.intent.EntranceEvent
import android.andrespin.notes.welcome_slides.intent.WelcomeEvent
import android.andrespin.notes.welcome_slides.intent.WelcomeIntent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class WelcomeViewModel : BaseViewModel() {

    val intent = Channel<WelcomeIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState> get() = _state

    private val _event = MutableSharedFlow<WelcomeEvent>()
    val event: SharedFlow<WelcomeEvent> get() = _event

    private var slide = 1

    init {
        handleIntent()
    }

    private fun handleIntent() {

        viewModelScope.launch {
            intent.consumeAsFlow().collect {
                when (it) {
                    is WelcomeIntent.NextSlide -> nextSlide(it)
                }
            }
        }

    }

    private fun nextSlide(it: WelcomeIntent.NextSlide) {
        if (it.currentItem < it.size - 1) {
            openNextSlide()
        } else {
            openApp()
        }
    }

    private fun openNextSlide() = viewModelScope.launch {
        _event.emit(WelcomeEvent.OpenSlide(slide++))
    }

    private fun openApp() = viewModelScope.launch {
        _event.emit(WelcomeEvent.OpenApp)
    }


}