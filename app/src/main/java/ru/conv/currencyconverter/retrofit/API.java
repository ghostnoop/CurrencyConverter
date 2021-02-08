package ru.conv.currencyconverter.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.conv.currencyconverter.model.MonthValute;

public interface API {

    @GET("daily_json.js")
    Call<JsonObject> getCurrency();

//    @GET("http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=02/03/2001&date_req2=14/03/2002&VAL_NM_RQ=R01235")/
//
//    @GET("XML_dynamic.asp")
//    Call<MonthValute> getMonthCurrency(
//            @Path("date_req1") String date1,
//            @Path("date_req2") String date2,
//            @Path("VAL_NM_RQ") String codeValute
//    );

    @GET("XML_dynamic.asp")
    Call<MonthValute> getMonthCurrency(
            @Query(value = "date_req1", encoded = true) String date1,
            @Query(value = "date_req2", encoded = true) String date2,
            @Query("VAL_NM_RQ") String codeValute
    );

    @GET("XML_dynamic.asp?date_req1=02/03/2001&date_req2=14/03/2002&VAL_NM_RQ=R01235")
    Call<MonthValute> getTest();
}
