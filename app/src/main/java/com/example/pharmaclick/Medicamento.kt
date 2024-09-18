package com.example.pharmaclick

import android.os.Parcel
import android.os.Parcelable

// Hacemos que la clase implemente Parcelable
data class Medicamento(
    val nombre: String = "",
    val precio: Double = 0.0,
    val imagen: String = ""
) : Parcelable {
    // Constructor secundario para leer desde el Parcel
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    // Función que define cómo escribir el objeto en el Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeString(imagen)
    }

    // Necesario para describir los tipos de objetos especiales (usualmente 0)
    override fun describeContents(): Int {
        return 0
    }

    // Companion object para crear instancias de Medicamento desde un Parcel
    companion object CREATOR : Parcelable.Creator<Medicamento> {
        override fun createFromParcel(parcel: Parcel): Medicamento {
            return Medicamento(parcel)
        }

        override fun newArray(size: Int): Array<Medicamento?> {
            return arrayOfNulls(size)
        }
    }
}

