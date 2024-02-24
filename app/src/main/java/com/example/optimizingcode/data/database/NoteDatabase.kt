package com.example.optimizingcode.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.optimizingcode.data.dao.NoteDao
import com.example.optimizingcode.data.entity.Note

@Database(entities = arrayOf(Note::class), version =  1)
abstract class NoteDatabase :RoomDatabase() {
    abstract fun noteDao(): NoteDao
}