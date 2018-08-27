package com.stehno.spellbooks

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [Spell::class], version = 1)
abstract class SpellDatabase : RoomDatabase() {

    abstract fun spellsDao(): SpellDao

    companion object {
        fun getInstance(context: Context) = Room.databaseBuilder(
            context,
            SpellDatabase::class.java,
            "spells.db"
        // FIXME: remove the main-thread part
        ).allowMainThreadQueries().build()
    }
}