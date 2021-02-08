package ru.conv.currencyconverter.model;

import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.List;

@Xml
public class MonthValute {
    //    Эти атритубы не нужны, но без них xml не мапится
    @Attribute
    String ID;

    @Attribute
    String DateRange1;

    @Attribute
    String DateRange2;

    @Attribute
    String name;

    @Element(name = "Record")
    List<Record> records;

    public List<Record> getRecords() {
        return records;
    }
}
