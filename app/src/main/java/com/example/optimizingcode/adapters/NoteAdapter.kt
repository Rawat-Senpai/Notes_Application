package com.example.optimizingcode.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.optimizingcode.R
import com.example.optimizingcode.Utils.ColorObjects.getRandomColor
import com.example.optimizingcode.data.entity.Note
import com.example.optimizingcode.databinding.LayoutNotesBinding
import java.text.SimpleDateFormat


class NoteAdapter( private val listener: OnNoteClickListener) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var notes =emptyList<Note>()

    interface OnNoteClickListener {
        fun onNoteClick(note: Note)
        fun onNoteLongClick(note: Note)
    }

    inner class ViewHolder(private val binding: LayoutNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            Log.d("NoteSize",notes.size.toString())

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = notes[position]
                    listener.onNoteClick(note)
                }
            }

            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = notes[position]
                    listener.onNoteLongClick(note)
                    return@setOnLongClickListener true
                }
                return@setOnLongClickListener false
            }
        }

        fun bind(note: Note) {
            binding.apply {
                titleNote.text = note.title
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                date.text = formatter.format(note.date)
                noteDescription.text = note.content

                val randomColor = getRandomColor(itemView.context)
                bookmark.setColorFilter(randomColor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
//        setAnimation(holder.itemView, position)
//        setAnimationNew(holder.itemView, position)

        setCustomAnimation(holder.itemView)

    }


    // custom animation
    private fun setCustomAnimation(viewToAnimate: View) {
        val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.custom_animation)
        viewToAnimate.startAnimation(animation)
    }

    private fun setAnimationNew(viewToAnimate: View, position: Int) {
        val animation = if (position % 2 == 0) {
            AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_left)
        } else {
            AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_right)
        }
        viewToAnimate.startAnimation(animation)
    }

    // animation when the data in recycler View comees from bottom
    private fun setAnimation(viewToAnimate: View, position: Int) {
        val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_bottom)
        viewToAnimate.startAnimation(animation)
    }


    fun setData(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }
}