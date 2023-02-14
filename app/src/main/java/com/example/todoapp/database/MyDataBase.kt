package com.example.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.database.dao.TodoDao
import com.example.todoapp.database.model.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class MyDataBase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        const val DATABASE_NAME = "Todo_Database"
        private var myDataBase: MyDataBase? = null
        fun getInstance(context: Context): MyDataBase {
            if (myDataBase == null) {
                myDataBase = Room.databaseBuilder(
                    context, MyDataBase::class.java, DATABASE_NAME
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return myDataBase!!
        }
    }
}