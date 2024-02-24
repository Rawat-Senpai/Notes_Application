package com.example.optimizingcode.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.optimizingcode.data.dao.NoteDao
import com.example.optimizingcode.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteDao:NoteDao):ViewModel() {

    val notes = noteDao.getAllNotes()
    val notesChannel= Channel<NotesEvents>()
    val notesEvent = notesChannel.receiveAsFlow()

    fun insertNote(note:Note) = viewModelScope.launch{
        noteDao.insertNote(note)
        notesChannel.send(NotesEvents.NavigateToNotesFragments)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        noteDao.updateNote(note)
        notesChannel.send(NotesEvents.NavigateToNotesFragments)
    }

    fun deleteNote(note:Note) = viewModelScope.launch {
        noteDao.deleteNote(note)
        notesChannel.send(NotesEvents.ShowUndoSnackBar("Note Deleted Successfully",note))
    }

    sealed class NotesEvents{
        data class ShowUndoSnackBar(val msg:String,val note:Note):NotesEvents()

        object NavigateToNotesFragments:NotesEvents(

        )
    }


}