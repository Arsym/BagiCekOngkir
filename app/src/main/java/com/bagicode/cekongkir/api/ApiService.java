package com.bagicode.cekongkir.api;

/**
 * Created by Robby Dianputra on 10/31/2017.
 */

import com.bagicode.cekongkir.model.city.ItemCity;
import com.bagicode.cekongkir.model.cost.ItemCost;
import com.bagicode.cekongkir.model.province.ItemProvince;
import com.bagicode.cekongkir.model.subdistrict.ItemSubdistrict;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Province
    @GET("province")
    @Headers("key:3f3bce6f9e0d62d356f48cb8040b5653")
    Call<ItemProvince> getProvince ();

    // City
    @GET("city")
    @Headers("key:3f3bce6f9e0d62d356f48cb8040b5653")
    Call<ItemCity> getCity (@Query("province") String province);

    // Subdistrict
    @GET("subdistrict")
    @Headers("key:3f3bce6f9e0d62d356f48cb8040b5653")
    Call<ItemSubdistrict> getSubdistrict (@Query("city") String city);

    // Cost
    @FormUrlEncoded
    @POST("cost")
    Call<ItemCost> getCost(@Field("key") String Token,
                           @Field("origin") String origin,
                           @Field("originType") String originType,
                           @Field("destination") String destination,
                           @Field("destinationType") String destinationType,
                           @Field("weight") String weight,
                           @Field("courier") String courier);
}
