package com.example.optimizingcode.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "notes")
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true) val id:Int =0,
    val title:String?= null,
    val content:String?= null,
    val date:Long
):  Parcelable