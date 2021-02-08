package ru.conv.currencyconverter.utils;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.conv.currencyconverter.model.CurrencyValute;
import ru.conv.currencyconverter.model.MonthValute;
import ru.conv.currencyconverter.model.Record;
import ru.conv.currencyconverter.retrofit.NetworkService;

public class UtilsCurrency {
    private static final HashMap<Integer, ArrayList<Double>> monthCurrencies = new HashMap<>();
    private static double usd;
    private static double eur;
    private static boolean isReadyToday = false;
    private static boolean isReadyMonthUsd = false;
    private static boolean isReadyMonthEur = false;

    private static String[] daysOfMonth;

    private final String usdCode = "R01235";
    private final String eurCode = "R01239";


    public UtilsCurrency() {
        String[] dates = getDates();


        NetworkService.getApiJson().getCurrency().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                assert response.body() != null;
                JsonObject valute = response.body().getAsJsonObject("Valute");
                Gson gson = new Gson();

                usd = gson.fromJson(valute.getAsJsonObject("USD"), CurrencyValute.class).getValue();
                eur = gson.fromJson(valute.getAsJsonObject("EUR"), CurrencyValute.class).getValue();
                isReadyToday = true;

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });

        NetworkService.getApiXml().getMonthCurrency(dates[0], dates[1], usdCode).enqueue(new Callback<MonthValute>() {
            @Override
            public void onResponse(Call<MonthValute> call, Response<MonthValute> response) {
                assert response.body() != null;

                List<Record> usdRecords = response.body().getRecords();
                daysOfMonth = new String[usdRecords.size()];

                ArrayList<Double> rubMonth = new ArrayList<>();
                ArrayList<Double> usdMonth = new ArrayList<>();

                int index = 0;
                for (Record record : usdRecords) {
                    daysOfMonth[index] = record.getDate();
                    usdMonth.add(record.getValue());
                    rubMonth.add(1.);
                    index++;
                }
                monthCurrencies.put(0, rubMonth);
                monthCurrencies.put(1, usdMonth);

                isReadyMonthUsd = true;


            }

            @Override
            public void onFailure(Call<MonthValute> call, Throwable t) {
            }
        });

        NetworkService.getApiXml().getMonthCurrency(dates[0], dates[1], eurCode).enqueue(new Callback<MonthValute>() {
            @Override
            public void onResponse(Call<MonthValute> call, Response<MonthValute> response) {
                assert response.body() != null;

                List<Record> eurRecords = response.body().getRecords();

                ArrayList<Double> eurMonth = new ArrayList<>();

                for (Record record : eurRecords) {
                    eurMonth.add(record.getValue());
                }
                monthCurrencies.put(2, eurMonth);
                isReadyMonthEur = true;
            }

            @Override
            public void onFailure(Call<MonthValute> call, Throwable t) {
            }
        });


    }

    //    01 : rub - usd
    //    02 : rub - eur
    //    03 : usd - rub
    //    04 : eur - rub
    //    05 : usd - eur
    //    06 : eur - usd
    public static ArrayList<Entry> getCurrenciesFromTo(int from, int to) {

        return getEntrails(
                Objects.requireNonNull(monthCurrencies.get(from)),
                Objects.requireNonNull(monthCurrencies.get(to))
        );


    }

    private static ArrayList<Entry> getEntrails(ArrayList<Double> from, ArrayList<Double> to) {
        ArrayList<Entry> arrayListResult = new ArrayList<>();
        for (int i = 0; i < from.size(); i++) {
            float a = from.get(i).floatValue();
            float b = to.get(i).floatValue();
            float t = a / b;
            arrayListResult.add(
                    new Entry(i, t)
            );

        }
        return arrayListResult;
    }

    public static double getUsd() {
        return usd;
    }

    public static double getEur() {
        return eur;
    }

    public static boolean isReady() {
        return isReadyToday && isReadyMonthUsd && isReadyMonthEur;
    }

    public static String[] getDaysOfMonth() {
        return daysOfMonth;
    }

    @SuppressLint("SimpleDateFormat")
    private String[] getDates() {
        String[] dates = new String[2];

        DateFormat dataForm = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        dates[1] = dataForm.format(today);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        Date previousDate = cal.getTime();
        dates[0] = dataForm.format(previousDate);

        return dates;
    }


}
