package android.andrespin.notes

import android.andrespin.notes.model.Date
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.Time
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.utils.converter.Converter
import android.andrespin.notes.utils.converter.DataTypes
import android.andrespin.notes.utils.converter.TimeAndDate
import org.junit.Assert
import org.junit.Test

class ConverterUnitTest {

    private val converter: TimeAndDate = Converter()
    private val time = Time("12", "04", "12")
    private val date = Date("13", "04", "2022")

    private val timeAndDate = "2022.04.13 - 12:04:12"

    private val noteEntity = NoteEntity(
        0,
        "Header",
        "Body",
        "12",
        "04",
        "12",
        "13",
        "04",
        "2022"
    )

    private val noteData = NoteData(
        "Header",
        "Body",
        time,
        date,
        0,
    )

    @Test
    fun timeConverter_isCorrect() {
        Assert.assertEquals(
            time,
            converter.convertToTime(timeAndDate)
        )
    }

    @Test
    fun dateConverter_isCorrect() {
        Assert.assertEquals(
            date,
            converter.convertToDate(timeAndDate)
        )
    }

    @Test
    fun convertToNoteEntity_isCorrect() {
        val converter: DataTypes = Converter()
        Assert.assertEquals(
            noteEntity,
            converter.convertToNoteEntity(noteData)
        )
    }

    @Test
    fun convertToNoteData_isCorrect() {
        val converter: DataTypes = Converter()
        Assert.assertEquals(
            noteData,
            converter.convertToNoteData(noteEntity)
        )
    }
}
