package com.stehno.spellbooks

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.TypeConverters
import android.os.Parcel
import android.os.Parcelable
import java.util.*

@Entity(tableName = "spells", primaryKeys = ["book", "name"])
@TypeConverters(value = [CasterTypeConverters::class])
data class Spell(@ColumnInfo(name = "name") var name: String,
                 @ColumnInfo(name = "level") var level: Int,
                 @ColumnInfo(name = "school") var school: String,
                 @ColumnInfo(name = "ritual") var ritual: Boolean,
                 @ColumnInfo(name = "casting_time") var castingTime: String,
                 @ColumnInfo(name = "range") var range: String,
                 @ColumnInfo(name = "components") var components: String,
                 @ColumnInfo(name = "duration") var duration: String,
                 @ColumnInfo(name = "casters", typeAffinity = ColumnInfo.TEXT) var casters: Array<String>,
                 @ColumnInfo(name = "book") var book: String,
                 @ColumnInfo(name = "description") var description: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte().toInt() == 1,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString().split(",").toTypedArray(),
        parcel.readString(),
        parcel.readString()
    )

    override fun toString() = name

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(level)
        parcel.writeString(school)
        parcel.writeByte(when (ritual) {
            true -> 1.toByte()
            else -> 0.toByte()
        })
        parcel.writeString(castingTime)
        parcel.writeString(range)
        parcel.writeString(components)
        parcel.writeString(duration)
        parcel.writeString(casters.joinToString(","))
        parcel.writeString(book)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0 // TODO: what is this for?
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Spell

        if (name != other.name) return false
        if (level != other.level) return false
        if (school != other.school) return false
        if (ritual != other.ritual) return false
        if (castingTime != other.castingTime) return false
        if (range != other.range) return false
        if (components != other.components) return false
        if (duration != other.duration) return false
        if (!Arrays.equals(casters, other.casters)) return false
        if (book != other.book) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + level
        result = 31 * result + school.hashCode()
        result = 31 * result + ritual.hashCode()
        result = 31 * result + castingTime.hashCode()
        result = 31 * result + range.hashCode()
        result = 31 * result + components.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + Arrays.hashCode(casters)
        result = 31 * result + book.hashCode()
        result = 31 * result + description.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<Spell> {
        override fun createFromParcel(parcel: Parcel): Spell {
            return Spell(parcel)
        }

        override fun newArray(size: Int): Array<Spell?> {
            return arrayOfNulls(size)
        }
    }
}
