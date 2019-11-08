package com.example.proyecto

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
data class Muestra(var point: LatLng?, var index: Int) : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readParcelable(LatLng::class.java.classLoader),
                parcel.readInt()
        ) {
        }

        constructor() : this(point = null, index = -1)

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeParcelable(point, flags)
                parcel.writeInt(index)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<Muestra> {
                override fun createFromParcel(parcel: Parcel): Muestra {
                        return Muestra(parcel)
                }

                override fun newArray(size: Int): Array<Muestra?> {
                        return arrayOfNulls(size)
                }
        }


}


