package android.andrespin.notes.utils.watch

import android.andrespin.notes.model.dateFormat
import android.annotation.SuppressLint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.SimpleDateFormat
import java.util.*

class CurrentTime(
    private val scope: CoroutineScope
) : ICurrentTime {

    private val mutableTimeAndDate = MutableStateFlow("")
    override val currentTimeAndDate = mutableTimeAndDate

    override fun startTimeObserving() {
        scope
            .launch {
                while (isActive) {
                    mutableTimeAndDate.value = getCurrentTimeAndDate()
                //    println("getCurrentTimeAndDate() ${getCurrentTimeAndDate()}")
                    delay(20)
                }
            }
    }

    override fun stopTimeObserving() {
        scope.coroutineContext.cancelChildren()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentTimeAndDate() = SimpleDateFormat(dateFormat)
        .format(Date())

}