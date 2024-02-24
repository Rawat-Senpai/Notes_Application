package com.example.optimizingcode.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.optimizingcode.R
import com.example.optimizingcode.data.entity.Note
import com.example.optimizingcode.databinding.DeletePopupBinding
import com.example.optimizingcode.databinding.FragmentAddEditNotesBinding
import com.example.optimizingcode.viewModel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@AndroidEntryPoint
class AddEditNoteFragment:Fragment(R.layout.fragment_add_edit_notes) {

    val viewModel by viewModels<NoteViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentAddEditNotesBinding.bind(requireView())
        val args:AddEditNoteFragmentArgs by navArgs()

        val note = args.note

        if(note!=null){
            binding.apply {
                noteText.setText(note.title)
                noteDescription.setText(note.content)
                val formatter = SimpleDateFormat("MMM dd yyyy")
                currentDate.text = formatter.format(note.date)
                saveNote.setOnClickListener(){
                    val title = noteText.text.toString()
                    val content = noteDescription.text.toString()
                    val updatedNote = note.copy(title = title, content = content, date = System.currentTimeMillis())
                    viewModel.updateNote(updatedNote)

                }

                backBtn.setOnClickListener(){
                    val action = AddEditNoteFragmentDirections.actionAddEditNoteFragment2ToNoteFragment2()
                    findNavController().navigate(action)
                }

                menuBtn.visibility=View.VISIBLE

                menuBtn.setOnClickListener(){
//                    viewModel.deleteNote(note)

                    deleteNotePopup(note)
                }

            }
        }else{
            binding.apply {
                val formatter = SimpleDateFormat("MMM dd yyyy")
                currentDate.text = formatter.format(System.currentTimeMillis())
                saveNote.setOnClickListener(){
                    val title = noteText.text.toString()
                    val content = noteDescription.text.toString()

                    val newNote = Note(title= title, content = content, date = System.currentTimeMillis())
                    viewModel.insertNote(newNote)
                }

                backBtn.setOnClickListener(){
                    val action = AddEditNoteFragmentDirections.actionAddEditNoteFragment2ToNoteFragment2()
                    findNavController().navigate(action)
                }
                menuBtn.visibility=View.GONE









            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notesEvent.collect{event->
                if(event is NoteViewModel.NotesEvents.NavigateToNotesFragments){
                    val action = AddEditNoteFragmentDirections.actionAddEditNoteFragment2ToNoteFragment2()
                    findNavController().navigate(action)
                }
            }
        }

    }

    private fun deleteNotePopup(note: Note) {

        val view =
            LayoutInflater.from(context).inflate(R.layout.delete_popup, null)
        val builder =
            AlertDialog.Builder(requireContext(), R.style.dialog_transparent_style)
                .setView(view)

        val dialogBinding = DeletePopupBinding.bind(view)
        val mAlertDialog = builder.show()

        val window = mAlertDialog.window
        val params = window?.attributes
        params?.width = WindowManager.LayoutParams.WRAP_CONTENT
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        window?.attributes = params

        window!!.setGravity(Gravity.CENTER)
        window.setBackgroundDrawableResource(R.color.transparent)


        dialogBinding.title.text =
            "Are you sure you want to delete this note?"

        dialogBinding.deleteBtn.setOnClickListener {
            viewModel.deleteNote(note)
            mAlertDialog.dismiss()
        }
        dialogBinding.cancleBtn.setOnClickListener {
            mAlertDialog.dismiss()
        }

        builder.setCancelable(true)



    }
}