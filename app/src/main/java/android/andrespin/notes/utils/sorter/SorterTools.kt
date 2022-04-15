package android.andrespin.notes.utils.sorter

import android.andrespin.notes.model.Date
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.Time

abstract class SorterTools {

    fun sortNotesAccordingTimeAndDateInDescendingOrder(notes: MutableList<NoteData>): MutableList<NoteData> {
        val n = notes.size
        var temp: NoteData
        for (i in 0 until n) {
            var indexOfMin = i
            for (j in n - 1 downTo i) {
                if (isTimeAndDate1BiggerOrEqual(notes[j], notes[indexOfMin])) {
                    indexOfMin = j
                }
            }
            if (i != indexOfMin) {
                temp = notes[i]
                notes[i] = notes[indexOfMin]
                notes[indexOfMin] = temp
            }
        }
        return notes
    }

    fun sortNotesAccordingTimeAndDateInAscendingOrder(notes: MutableList<NoteData>): MutableList<NoteData> {
        val n = notes.size
        var temp: NoteData
        for (i in 0 until n) {
            var indexOfMin = i
            for (j in n - 1 downTo i) {
                if (isTimeAndDate1BiggerOrEqual(notes[j], notes[indexOfMin])) {
                    indexOfMin = j
                }
            }
            if (i != indexOfMin) {
                temp = notes[i]
                notes[i] = notes[indexOfMin]
                notes[indexOfMin] = temp
            }
        }
        return notes
    }

    private fun isTimeAndDate1BiggerOrEqual(note1: NoteData, note2: NoteData): Boolean =
        when {
            isDate1Bigger(note1.date, note2.date) -> true
            isTime1BiggerOrEqualTime2(note1.time, note2.time) -> true
            else -> false
        }

    private fun isTime1BiggerOrEqualTime2(time1: Time, time2: Time): Boolean {
        if (time1.h > time2.h) return true
        else if (time1.h == time2.h) {
            if (time1.m > time2.m) return true
            else if (time1.m == time2.m) {
                return time1.s >= time2.s
            }
        }
        return false
    }

    private fun isDate1Bigger(date1: Date, date2: Date): Boolean {
        if (date1.y > date2.y) {
            return true
        } else if (date1.y == date2.y) {
            if (date1.m > date2.m) {
                return true
            } else if (date1.m == date2.m) {
                if (date1.d > date2.d) {
                    return true
                }
            }
        }
        return false
    }

}