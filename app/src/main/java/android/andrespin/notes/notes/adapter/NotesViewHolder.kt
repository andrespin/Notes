package android.andrespin.notes.notes.adapter

import android.andrespin.notes.databinding.ItemRvNoteDataBinding
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.notes.NotesViewModel
import android.content.Context
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView

class NotesViewHolder(
    private val vb: ItemRvNoteDataBinding,
    private val context: Context,
    private val viewModel: NotesViewModel
) : RecyclerView.ViewHolder(vb.root) {

    fun bind(note: NoteData) {
        vb.txtDate.text = "${note.date.d} ${note.date.nameOfMonth} ${note.date.y}"
        vb.txtNoteHeader.text = note.header
        vb.layoutNote.background =
            AppCompatResources.getDrawable(
                context,
                note.background
            )
        vb.backImgNoteChecked.background =
            AppCompatResources.getDrawable(
                context,
                note.checkedBackground
            )
        vb.backImgNoteChecked.visibility = note.checkedVisibility

      //  vb.backImgNoteChecked.visibility = note.checkedVisibility
    }

}