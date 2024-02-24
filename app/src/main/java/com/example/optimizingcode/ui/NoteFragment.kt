package com.example.optimizingcode.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.GridView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.optimizingcode.R
import com.example.optimizingcode.adapters.NoteAdapter
import com.example.optimizingcode.data.entity.Note
import com.example.optimizingcode.databinding.DeletePopupBinding
import com.example.optimizingcode.databinding.FragmentNotesBinding
import com.example.optimizingcode.viewModel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteFragment: Fragment(R.layout.fragment_notes), NoteAdapter.OnNoteClickListener {

    val viewModel by viewModels<NoteViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNotesBinding.bind(requireView())

        binding.apply {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL )
            recyclerView.setHasFixedSize(false)

            floatingActionBtn.setOnClickListener(){
                    val action = NoteFragmentDirections.actionNoteFragment2ToAddEditNoteFragment2(null)
                    findNavController().navigate(action)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notes.collect{notes->
                    val adapter = NoteAdapter(notes,this@NoteFragment)
                    recyclerView.adapter=adapter
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notesEvent.collect{event->

                    if(event is NoteViewModel.NotesEvents.ShowUndoSnackBar){
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).setAction("Undo"){
                            viewModel.insertNote(event.note)
                        }.show()
                    }

                }
            }


        }

    }

    override fun onNoteClick(note: Note) {
        val action = NoteFragmentDirections.actionNoteFragment2ToAddEditNoteFragment2(note)
        findNavController().navigate(action)
    }

    override fun onNoteLongClick(note: Note) {


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