package ru.conv.currencyconverter.utils;

public class CalculatorCurrency {

    //    в качестве базовой валюты выбран рубль
    public static double calculate(double number, int from, int to) {
        double numberAfterChange;

        switch (from) {
            case 0:
                numberAfterChange = number;
                break;
            case 1:
                numberAfterChange = number * UtilsCurrency.getUsd();
                break;
            case 2:
                numberAfterChange = number * UtilsCurrency.getEur();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + from);
        }
        switch (to) {
            case 0:
                return numberAfterChange;
            case 1:
                return numberAfterChange / UtilsCurrency.getUsd();
            case 2:
                return numberAfterChange / UtilsCurrency.getEur();
            default:
                throw new IllegalStateException("Unexpected value: " + from);
        }

    }
}
