package ru.conv.currencyconverter.view.fragments;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

import ru.conv.currencyconverter.R;
import ru.conv.currencyconverter.utils.CalculatorCurrency;
import ru.conv.currencyconverter.utils.UtilsCurrency;

public class CurrencyFragment extends Fragment {
    private final int chartLinesColor = Color.parseColor("#52c7b8");
    private LineChart chart;
    private Spinner currency1;
    private Spinner currency2;

    private EditText inputCurrency;
    private TextView outputCurrency;

    private int activeInputCurrencyType = 0;
    private int activeOutputCurrencyType = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        chart = view.findViewById(R.id.chart);

        currency1 = view.findViewById(R.id.currency1);
        currency2 = view.findViewById(R.id.currency2);

        inputCurrency = view.findViewById(R.id.inputCurrency);
        outputCurrency = view.findViewById(R.id.outputCurrency);

        initChart();
        setAdapterSpinner();
        initListeners();

        return view;
    }

    private void initChart() {
        String[] chartDays = UtilsCurrency.getDaysOfMonth();

        chart.setBackgroundColor(Color.WHITE);
        XAxis xAxis;
        xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // vertical grid lines
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return chartDays[(int) value % chartDays.length];
            }
        });


        YAxis yAxis;
        yAxis = chart.getAxisLeft();
        // disable dual axis (only use LEFT axis)
        chart.getAxisRight().setEnabled(false);
        // horizontal grid lines
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        LineDataSet set1;


        chart.setTouchEnabled(true);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);


        // create a dataset and give it a type
        set1 = new LineDataSet(UtilsCurrency.getCurrenciesFromTo(0, 1), "");

        set1.setDrawIcons(false);

        // draw dashed line
        set1.enableDashedLine(10f, 5f, 0f);

        // color lines and points
        set1.setColor(chartLinesColor);
        set1.setCircleColor(chartLinesColor);

        // line thickness and point size
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);

        // draw points as solid circles
        set1.setDrawCircleHole(false);

        // customize legend entry
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);

        // text size of values
        set1.setValueTextSize(9f);

        // draw selection line as dashed
        set1.enableDashedHighlightLine(10f, 5f, 0f);

        // set the filled area
        set1.setDrawFilled(true);
        set1.setFillFormatter((dataSet, dataProvider) -> chart.getAxisLeft().getAxisMinimum());

        // set color of filled area

        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_green);
        set1.setFillDrawable(drawable);
        LineData data = new LineData(set1);

        chart.setData(data);

    }


    private void setChart() {

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            LineDataSet set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);

            set1.setValues(UtilsCurrency.getCurrenciesFromTo(
                    activeInputCurrencyType,
                    activeOutputCurrencyType)
            );

            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        }
    }

    private void setAdapterSpinner() {
        String[] dataAdapter = getResources().getStringArray(R.array.valuteName);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                dataAdapter
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                dataAdapter
        );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        currency1.setAdapter(adapter1);
        currency2.setAdapter(adapter2);
        currency2.setSelection(1);

//        adapter.notifyDataSetChanged();


    }

    private void initListeners() {
        currency1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (activeInputCurrencyType != i) {
                    activeInputCurrencyType = i;
                    changeChart();
                    changeOutCurrency();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        currency2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (activeOutputCurrencyType != i) {
                    activeOutputCurrencyType = i;
                    changeChart();
                    changeOutCurrency();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        inputCurrency.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                outCurrencyText();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void outCurrencyText() {
        try {
            double number = Double.parseDouble(inputCurrency.getText().toString());
            double outputNumber = CalculatorCurrency.calculate(
                    number,
                    activeInputCurrencyType,
                    activeOutputCurrencyType
            );
            DecimalFormat df = new DecimalFormat("#.###");
            outputCurrency.setText(df.format(outputNumber));

        } catch (NumberFormatException e) {
        }

    }

    private void changeOutCurrency() {
        outCurrencyText();
    }

    private void changeChart() {
        if (activeInputCurrencyType != activeOutputCurrencyType) {
            setChart();
            chart.invalidate();
        }
    }
}