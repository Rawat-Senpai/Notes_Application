package com.example.optimizingcode.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.optimizingcode.ColorObjects.getRandomColor
import com.example.optimizingcode.data.entity.Note
import com.example.optimizingcode.databinding.LayoutNotesBinding
import java.text.SimpleDateFormat



class NoteAdapter(private  val notes:List<Note>, private val listener:OnNoteClickListener) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {


    interface OnNoteClickListener {
        fun onNoteClick(note:Note)

        fun onNoteLongClick(note:Note)
    }



    inner  class ViewHolder(private val binding: LayoutNotesBinding ,val context:Context ): RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener(){




                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION)
                    {
                        val note = notes[position]
                        listener.onNoteClick(note)
                    }

                }


                root.setOnLongClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION) {
                        val note = notes[position]
                        listener.onNoteLongClick(note)
                    }

                    true
                }



            }
        }


        fun bind(note:Note){
            binding.apply {
                titleNote.text = note.title
                val formatter = SimpleDateFormat("dd.mm.yyyy")
                date.text = formatter.format(note.date)
                noteDescription.text = note.content

                val randomColor = getRandomColor(context)
//                Log.d("NoteAdapter", "Random color: $randomColor")
//                cardView.setBackgroundColor(randomColor)
                bookmark.setColorFilter(randomColor)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutNotesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding,parent.context)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(notes[position]){
            holder.bind(this)



        }



    }
}