package com.bagicode.cekongkir.model.subdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemSubdistrict {
    @SerializedName("rajaongkir")
    @Expose
    private Rajaongkir rajaongkir;

    public Rajaongkir getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(Rajaongkir rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

}
