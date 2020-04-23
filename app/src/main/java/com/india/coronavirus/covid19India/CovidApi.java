package com.india.coronavirus.covid19India;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CovidApi {

    @GET("/data.json")
    Call<String> getStateWiseList();


    @GET("/v2/state_district_wise.json")
    Call<String> getDistrictWiseList();
}
