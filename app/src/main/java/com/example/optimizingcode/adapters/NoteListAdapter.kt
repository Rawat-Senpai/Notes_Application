package com.example.optimizingcode.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.optimizingcode.R
import com.example.optimizingcode.Utils.ColorObjects
import com.example.optimizingcode.data.entity.Note
import com.example.optimizingcode.databinding.LayoutNotesBinding
import java.text.SimpleDateFormat

class NoteListAdapter(private val listener: NoteAdapter.OnNoteClickListener) :
    ListAdapter<Note, NoteListAdapter.NoteViewHolder>(ComparatorDiffUtil()) {

    inner class NoteViewHolder(private val binding: LayoutNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(note: Note) {
            // notes for data
            binding.apply {
                titleNote.text = note.title
                val formatter = SimpleDateFormat("dd.MM.yyyy")
                date.text = formatter.format(note.date)
                noteDescription.text = note.content

                val randomColor = ColorObjects.getRandomColor(itemView.context)
                bookmark.setColorFilter(randomColor)


                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {

                        listener.onNoteClick(note)
                    }
                }


                root.setOnLongClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {

                        listener.onNoteLongClick(note)
                        return@setOnLongClickListener true
                    }
                    return@setOnLongClickListener false
                }

            }


        }

    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        val binding = LayoutNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)

    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = getItem(position)
        note?.let {
            holder.bind(it)
        }
        setAnimationNew(holder.itemView, position)


    }

    private fun setAnimationNew(viewToAnimate: View, position: Int) {
        val animation = if (position % 2 == 0) {
            AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_left)
        } else {
            AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_in_right)
        }
        viewToAnimate.startAnimation(animation)
    }


}