package android.andrespin.notes.utils.converter

import android.andrespin.notes.model.Date
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.Time
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.model.mapRussianMonths

class Converter : TimeAndDate, DataTypes {

    override fun convertToTime(timeAndDate: String): Time {

        val char = timeAndDate.toCharArray()

        val h1 = char[13]
        val h2 = char[14]

        val m1 = char[16]
        val m2 = char[17]

        val s1 = char[19]
        val s2 = char[20]

        val h = h1.toString() + h2.toString()
        val m = m1.toString() + m2.toString()
        val s = s1.toString() + s2.toString()

        return Time(
            h,
            m,
            s
        )
    }

    override fun convertToDate(timeAndDate: String): Date {

        val char = timeAndDate.toCharArray()

        val y1 = char[0]
        val y2 = char[1]
        val y3 = char[2]
        val y4 = char[3]

        val m1 = char[5]
        val m2 = char[6]

        val d1 = char[8]
        val d2 = char[9]

        val y = y1.toString() + y2.toString() + y3.toString() + y4.toString()
        val m = m1.toString() + m2.toString()
        val d = if (d1.code == 0) {
            d2.toString()
        } else {
            (d1.toString() + d2.toString())
        }
        return Date(
            d,
            m,
            y
        )
    }

    override fun addNameOfMonth(date: Date) =
        Date(
            date.d,
            date.m,
            date.y,
            mapRussianMonths[date.m.toInt()]
        )

    override fun convertToNoteEntityList(list: List<NoteData>): List<NoteEntity> {
        val l = mutableListOf<NoteEntity>()
        for (i in list.indices) {
            val id = if (list[i].id == 0) {
                0
            } else {
                list[i].id
            }
            l.add(
                NoteEntity(
                    id,
                    list[i].header,
                    list[i].body,
                    list[i].time.h,
                    list[i].time.m,
                    list[i].time.s,
                    list[i].date.d,
                    list[i].date.m,
                    list[i].date.y,
                )
            )
        }
        return l
    }

    override fun convertToNoteDataList(list: List<NoteEntity>): List<NoteData> {
        val l = mutableListOf<NoteData>()
        for (i in list.indices) {
            val dateWithNameOfMonth =
                addNameOfMonth(getDate(list[i].dateDay, list[i].dateMonth, list[i].dateYear))
            l.add(
                NoteData(
                    list[i].header,
                    list[i].body,
                    getTime(list[i].timeHours, list[i].timeMinutes, list[i].timeSeconds),
                    dateWithNameOfMonth,
                    list[i].id
                )
            )
        }
        return l
    }

    override fun convertToNoteData(noteEntity: NoteEntity): NoteData {
        val time = Time(
            noteEntity.timeHours,
            noteEntity.timeMinutes,
            noteEntity.timeSeconds
        )
        val date = Date(
            noteEntity.dateDay,
            noteEntity.dateMonth,
            noteEntity.dateYear
        )
        return NoteData(
            noteEntity.header,
            noteEntity.body,
            time,
            date,
            noteEntity.id
        )
    }

    override fun convertToNoteEntity(noteData: NoteData): NoteEntity =
        NoteEntity(
            noteData.id,
            noteData.header,
            noteData.body,
            noteData.time.h,
            noteData.time.m,
            noteData.time.s,
            noteData.date.d,
            noteData.date.m,
            noteData.date.y
        )

    private fun getTime(
        timeHours: String,
        timeMinutes: String,
        timeSeconds: String
    ): Time =
        Time(
            timeHours,
            timeMinutes,
            timeSeconds
        )

    private fun getDate(
        dateDay: String,
        dateMonth: String,
        dateYear: String
    ): Date =
        Date(
            dateDay,
            dateMonth,
            dateYear
        )

}