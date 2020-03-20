package sample.loader;

import sample.clothes.Clothes;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FieldsReader {

    public Field[] getFields(Clothes clothes) {
        Class tempClass = clothes.getClass();
        return tempClass.getFields();
    }
}
