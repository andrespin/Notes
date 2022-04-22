package android.andrespin.notes.notes.adapter

import android.andrespin.notes.databinding.ItemRvNoteDataBinding
import android.andrespin.notes.model.NoteData
import android.andrespin.notes.notes.NotesFragment
import android.andrespin.notes.notes.NotesViewModel
import android.andrespin.notes.notes.intent.NotesIntent
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.parse.ParseObject
import kotlinx.coroutines.launch

class NotesAdapter(
    private val context: Context,
    private val viewModel: NotesViewModel,
    private val fragment: NotesFragment
) : RecyclerView.Adapter<NotesViewHolder>() {

    private var data: List<NoteData> = mutableListOf()


//    fun addItem(t: ParseObject?) {
//        list!!.add(t!!)
//        notifyDataSetChanged()
//    }
//
//    fun removeItem(`object`: ParseObject) {
//        for (i in list!!.indices) {
//            if (list!![i].objectId == `object`.objectId) {
//                list!!.removeAt(i)
//                notifyItemRemoved(i)
//                notifyItemRangeChanged(i, list!!.size)
//                return
//            }
//        }
//    }
//
//    fun updateItem(`object`: ParseObject) {
//        for (i in list!!.indices) {
//            if (list!![i].objectId == `object`.objectId) {
//                list!![i] = `object`
//                notifyDataSetChanged()
//                return
//            }
//        }
//    }


    fun setData(data: List<NoteData>) {
        println("Data is set")
        this.data = data
        notifyDataSetChanged()
    }

    fun setData(data: List<NoteData>, vb: ViewBinding) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        NotesViewHolder(
            ItemRvNoteDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            context,
            viewModel
        ).apply {
            itemView.setOnClickListener {
                val note = data[this.layoutPosition]
                val id = note.id
                val pos = this.layoutPosition
                fragment.lifecycleScope.launch {
                    viewModel.intent.send(NotesIntent.ShortItemClickEvent(id, pos))
                }
            }

            itemView.setOnLongClickListener {
                val note = data[this.layoutPosition]
                val id = note.id
                val pos = this.layoutPosition
                fragment.lifecycleScope.launch {
                    viewModel.intent.send(NotesIntent.LongItemClickEvent(id, pos))
                }
                true
            }
        }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

}


