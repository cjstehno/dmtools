package com.stehno.difficulty

import android.os.Parcel
import android.os.Parcelable

data class Combatant(val cr: String, val count: Int) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readInt())

    override fun toString() = "CR-$cr x $count"

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cr)
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0 // TODO: ?
    }

    companion object CREATOR : Parcelable.Creator<Combatant> {

        override fun createFromParcel(parcel: Parcel): Combatant {
            return Combatant(parcel)
        }

        override fun newArray(size: Int): Array<Combatant?> {
            return arrayOfNulls(size)
        }
    }
}