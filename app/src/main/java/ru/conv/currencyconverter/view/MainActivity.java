package ru.conv.currencyconverter.view;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ru.conv.currencyconverter.R;
import ru.conv.currencyconverter.utils.UtilsCurrency;
import ru.conv.currencyconverter.view.fragments.BankMapFragment;
import ru.conv.currencyconverter.view.fragments.ConverterFragment;
import ru.conv.currencyconverter.view.fragments.CurrencyFragment;


public class MainActivity extends FragmentActivity {
    private final Fragment converterFragment = new ConverterFragment();
    private final Fragment currencyFragment = new CurrencyFragment();
    private final Fragment bankMapFragment = new BankMapFragment();

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    private Fragment activeFragment = converterFragment;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        waitToDownload();


    }

    private void setUI() {
        fragmentManager.beginTransaction()
                .add(R.id.container, converterFragment).show(converterFragment)
                .add(R.id.container, currencyFragment).hide(currencyFragment)
                .add(R.id.container, bankMapFragment).hide(bankMapFragment)
                .commit();
        initListeners();
    }

    @SuppressLint("NonConstantResourceId")
    private void initListeners() {

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            hideKeyboard();
            switch (menuItem.getItemId()) {
                case R.id.navigation_converter:
                    fragmentManager.beginTransaction().hide(activeFragment).show(converterFragment)
                            .commit();
                    activeFragment = converterFragment;
                    return true;
                case R.id.navigation_map:
                    fragmentManager.beginTransaction().hide(activeFragment).show(bankMapFragment)
                            .commit();
                    activeFragment = bankMapFragment;
                    return true;
                case R.id.navigation_chart:
                    fragmentManager.beginTransaction().hide(activeFragment).show(currencyFragment)
                            .commit();
                    activeFragment = currencyFragment;
                    return true;
            }
            return false;
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void waitToDownload() {
        new UtilsCurrency();
        ProgressDialog dialog = ProgressDialog.show(this, "",
                "Загружается курс, пожалуйста подождите!", true);
        new Thread(() -> {
            while (true) {
                if (UtilsCurrency.isReady()) {
                    dialog.dismiss();
                    this.runOnUiThread(this::setUI);
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }


}