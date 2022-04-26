package android.andrespin.notes.welcome_slides.adapter

import android.andrespin.notes.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WelcomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val layouts = intArrayOf(
        R.layout.slide_one,
        R.layout.slide_two,
        R.layout.slide_three,
        R.layout.slide_four
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.getContext())
            .inflate(viewType, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        println("Implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return layouts[position]
    }

    override fun getItemCount(): Int {
        return layouts.size
    }

    inner class SliderViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)

}

