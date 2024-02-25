package com.example.optimizingcode.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.optimizingcode.data.entity.Note
import kotlinx.coroutines.flow.Flow



@Dao
interface NoteDao {

    @Query("Select * from notes ORDER BY date Desc")
    fun getAllNotes() : Flow<List<Note>>

    @Query("Select * from notes WHERE title LIKE :searchQuery OR content LIKE :searchQuery ")
    fun searchInDatabase(searchQuery:String) : Flow<List<Note>>

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun  deleteNote(note:Note)


}