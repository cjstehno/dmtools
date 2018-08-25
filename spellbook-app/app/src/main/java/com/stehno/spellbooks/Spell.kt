package com.stehno.spellbooks

import android.os.Parcel
import android.os.Parcelable

data class Spell(val name: String,
                 val level: Int,
                 val school: String,
                 val ritual: Boolean,
                 val castingTime: String,
                 val range: String,
                 val components: String,
                 val duration: String,
                 val casters: Array<String>,
                 val book: String,
                 val description: String) : Parcelable {

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

    companion object CREATOR : Parcelable.Creator<Spell> {
        override fun createFromParcel(parcel: Parcel): Spell {
            return Spell(parcel)
        }

        override fun newArray(size: Int): Array<Spell?> {
            return arrayOfNulls(size)
        }
    }
}