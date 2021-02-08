package ru.conv.currencyconverter.model;


public class CurrencyValute {
    private String Name;
    private double Value;

    public String getName() {
        return Name;
    }

    public double getValue() {
        return Value;
    }

    public void setValue(double value) {
        Value = value;
    }

    public void setName(String name) {
        Name = name;
    }
}

//<Valute ID="R01235">
//<NumCode>840</NumCode>
//<CharCode>USD</CharCode>
//<Nominal>1</Nominal>
//<Name>Доллар США</Name>
//<Value>73,4453</Value>
//</Valute>

//<Valute ID="R01239">
//<NumCode>978</NumCode>
//<CharCode>EUR</CharCode>
//<Nominal>1</Nominal>
//<Name>Евро</Name>
//<Value>89,1846</Value>
//</Valute>