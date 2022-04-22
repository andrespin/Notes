package android.andrespin.notes

import android.andrespin.notes.model.Date
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.model.Time
import android.andrespin.notes.model.database.NoteEntity
import android.andrespin.notes.utils.sorter.ISorter
import android.andrespin.notes.utils.sorter.Sorter
import android.andrespin.notes.utils.sorter.SorterNotes
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SorterUnitTest {

    private val sorter: ISorter = Sorter()

    private val time1 = Time("12", "04", "12")

    private val time2 = Time("12", "44", "12")

    private val time3 = Time("13", "14", "12")

    private val date = Date("13", "04", "2022")

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
        time1,
        date,
        0,
    )

    private val notesDataAscending = listOf(
        NoteData(
            "A",
            "Body",
            time1,
            date,
            0,
        ),
        NoteData(
            "AB",
            "Body",
            time2,
            date,
            0,
        ),
        NoteData(
            "ABC",
            "Body",
            time3,
            date,
            0,
        )
    )

    private val notesDataDescending = listOf(
        NoteData(
            "ABC",
            "Body",
            time3,
            date,
            0,
        ),
        NoteData(
            "AB",
            "Body",
            time2,
            date,
            0,
        ),
        NoteData(
            "A",
            "Body",
            time1,
            date,
            0,
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSortByNoteSizeInDescendingOrder_isCorrect() = runTest {
        Assert.assertEquals(
            notesDataDescending,
            sorter.setSortByNoteSizeInDescendingOrder(notesDataAscending as MutableList<NoteData>)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSortByNoteSizeInAscendingOrder_isCorrect() = runTest {
        Assert.assertEquals(
            notesDataAscending,
            sorter.setSortByNoteSizeInDescendingOrder(notesDataDescending as MutableList<NoteData>)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSortByDateInAscendingOrder_isCorrect() = runTest {
        Assert.assertEquals(
            notesDataAscending,
            sorter.setSortByDateInAscendingOrder(notesDataDescending as MutableList<NoteData>)
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun setSortByDateInDescendingOrder_isCorrect() = runTest {
        Assert.assertEquals(
            notesDataAscending,
            sorter.setSortByDateInDescendingOrder(notesDataDescending as MutableList<NoteData>)
        )

        //  sorter.sortNotesForSyncing()
    }

    val db = listOf(
        NoteEntity(
            1,
            "Zagolovok",
            body = "Sinhronizaciya",
            "17",
            "15",
            "21",
            "20",
            "04",
            "2022"
        )
    )

    val server = listOf(
        NoteEntity(
            1,
            "Zagolovok",
            body = "Sinhronizaciya",
            "17",
            "15",
            "21",
            "20",
            "04",
            "2022"
        ),
        NoteEntity(
            2,
            "Zagolovok",
            body = "Sinhronizaciya",
            "17",
            "15",
            "21",
            "20",
            "04",
            "2022"
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun sortNotesForSyncing_isCorrect() = runTest {
        Assert.assertEquals(
            SorterNotes(
                mutableListOf(
                    NoteEntity(
                        2,
                        "Zagolovok",
                        body = "Sinhronizaciya",
                        "17",
                        "15",
                        "21",
                        "20",
                        "04",
                        "2022"
                    )
                ), mutableListOf()
            ),
            sorter.sortNotesForSyncing(db, server)
        )
    }

}
