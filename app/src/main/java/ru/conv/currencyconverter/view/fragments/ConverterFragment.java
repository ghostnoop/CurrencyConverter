package ru.conv.currencyconverter.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import ru.conv.currencyconverter.R;
import ru.conv.currencyconverter.utils.CalculatorCurrency;

public class ConverterFragment extends Fragment {
    private Spinner currency1;
    private Spinner currency2;
    private String[] dataAdapter;

    private EditText inputCurrency;
    private TextView outputCurrency;

    private Button calculate;

    private int activeInputCurrencyType = 0;
    private int activeOutputCurrencyType = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_converter, container, false);

        currency1 = view.findViewById(R.id.currency1);
        currency2 = view.findViewById(R.id.currency2);

        inputCurrency = view.findViewById(R.id.inputCurrency);
        outputCurrency = view.findViewById(R.id.outputCurrency);

        calculate = view.findViewById(R.id.btn_calculate);

        setAdapterSpinner();
        initListeners();

        return view;
    }


    private void setAdapterSpinner() {
        dataAdapter = getResources().getStringArray(R.array.valuteName);
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


    }

    private void initListeners() {
        currency1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activeInputCurrencyType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        currency2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activeOutputCurrencyType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calculate.setOnClickListener(view -> changeOutCurrency());
    }

    private void changeOutCurrency() {
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
}