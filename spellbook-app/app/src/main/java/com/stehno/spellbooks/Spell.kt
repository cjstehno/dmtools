package com.stehno.spellbooks

import android.os.Parcel
import android.os.Parcelable

data class Spell(val name: String) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun toString() = name

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
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