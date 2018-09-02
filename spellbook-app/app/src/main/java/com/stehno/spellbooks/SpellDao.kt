package com.stehno.spellbooks

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface SpellDao {

    @Query("SELECT * from spells order by name")
    fun listAll(): List<Spell>

    @Query("""select * from spells where
        ( length(:text) == 0 or (instr(lower(name), lower(:text)) > 0 or instr(lower(description), lower(:text)) > 0) ) and
        ( :level is null or level == :level ) and
        ( :caster is null or (instr(lower(casters), lower(:caster)) > 0) ) and
        ( :school is null or lower(school) == lower(:school) )
        order by name""")
    fun filter(text: String, level: Int?, caster: String?, school: String?): List<Spell>

    @Insert(onConflict = REPLACE)
    fun add(spell: Spell)

    @Query("DELETE from spells")
    fun deleteAll()

    @Query("SELECT COUNT(*) from spells")
    fun count(): Int
}