package android.andrespin.notes.utils.watch

import kotlinx.coroutines.flow.MutableStateFlow

interface ICurrentTime {

    val currentTimeAndDate: MutableStateFlow<String>

    fun startTimeObserving()

    fun stopTimeObserving()

}