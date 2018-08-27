package com.stehno.spellbooks

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface SpellDao {

    @Query("SELECT * from spells")
    fun listAll(): List<Spell>

    @Insert(onConflict = REPLACE)
    fun add(spell: Spell)

    @Query("DELETE from spells")
    fun deleteAll()
}