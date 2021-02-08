package ru.conv.currencyconverter.model;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

@Xml
public class Record {
    //    Эти атритубы не нужны, но без них xml не мапится
    @Attribute
    String Date;

    @Attribute
    String Id;

    @PropertyElement
    String Nominal;

    @PropertyElement
    String Value;

    private double value;

    public double getValue() {
        return Double.parseDouble(Value.replace(",", "."));
    }

    public String getDate() {
        return Date.substring(0, 5);
    }
}
