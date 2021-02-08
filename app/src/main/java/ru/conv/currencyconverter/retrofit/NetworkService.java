package ru.conv.currencyconverter.retrofit;

import android.app.Application;

import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.util.Arrays;
import java.util.List;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService extends Application {
    private static NetworkService mInstance;
    private Retrofit mRetrofitJson;
    private Retrofit mRetrofitXml;
    private static API apiJson;
    private static API apiXml;

    //http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=02/03/2001&date_req2=14/03/2002&VAL_NM_RQ=R01235
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;


        List<ConnectionSpec> lists = Arrays.asList(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(lists)
                .build();

        mRetrofitJson = new Retrofit.Builder()
                .baseUrl("https://www.cbr-xml-daily.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        TikXml xml = new TikXml.Builder().exceptionOnUnreadXml(false).build();
        mRetrofitXml = new Retrofit.Builder()
                .baseUrl("http://www.cbr.ru/scripts/")
                .addConverterFactory(TikXmlConverterFactory.create(xml))
                .client(client)
                .build();

        apiJson = mRetrofitJson.create(API.class);
        apiXml = mRetrofitXml.create(API.class);
    }


    public static API getApiJson() {
        return apiJson;
    }

    public static API getApiXml() {
        return apiXml;
    }
}
