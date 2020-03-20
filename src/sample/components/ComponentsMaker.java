package sample.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import sample.material.Material;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ComponentsMaker {
    final int Y_START = 40;
    final int X = 20;
    final int Y_MARGIN = 40;

    private ArrayList<Control> controls = new ArrayList<>();

    private ObservableList<String> makeContentArray(Class tempClass){
        ObservableList observableList = FXCollections.observableArrayList();
        for (Field field : tempClass.getFields()){
            observableList.add(field.getName());
        }
        return observableList;
    }


    private ComboBox makeCombobox(int PositionY, Class tempClass){
        ComboBox comboBox = new ComboBox(makeContentArray(tempClass));
        comboBox.setLayoutY(PositionY);
        comboBox.setLayoutX(X);
        return comboBox;
    }

    private TextField makeTextField(int PositionY){
        TextField textField = new TextField();
        textField.setLayoutY(PositionY);
        textField.setLayoutX(X);
        return textField;
    }

    private ArrayList<Control> makeControl(int position, Field[] fields){
        for (Field temp: fields){
            if ((temp.getType() == int.class) || (temp.getType() == double.class)){
                controls.add(makeTextField(position));
            }else{
                if (temp.getType() == Material.class){
                    makeControl(position, temp.getType().getFields());
                }else {
                    controls.add(makeCombobox(position, temp.getType()));
                }

            }
            position += Y_MARGIN;
        }
        return controls;
    }

    public ArrayList<Control> makeComponents(Class tempClass){
        makeControl(Y_START, tempClass.getFields());
        return controls;
    }
}
