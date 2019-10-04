package com.bagicode.cekongkir.model.subdistrict;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {
    @SerializedName("city")
    @Expose
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
