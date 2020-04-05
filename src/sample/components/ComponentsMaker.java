package sample.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import sample.clothes.Clothes;
import sample.material.Material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentsMaker {
    private final int Y_START = 40;
    private final int X = 20;
    private final int Y_MARGIN = 40;
    private final int WIDTH = 226;
    private Matcher mtch = Pattern.compile("[A-Z]").matcher("");

    private ArrayList<Control> controls = new ArrayList<>();

    private ObservableList makeContentArray(Class tempClass){
        ObservableList observableList = FXCollections.observableArrayList();
        for (Field field : tempClass.getFields()){
            observableList.add(field.getName());
        }
        return observableList;
    }

    public ArrayList<Control> fillControls(ArrayList<Control> controlArrayList, Object object, int i) throws IllegalAccessException {
        for (Field field : object.getClass().getFields()) {
            if (field.getType() != Material.class) {
                if (controlArrayList.get(i) instanceof TextField) {
                    ((TextField) controlArrayList.get(i)).setText(String.valueOf(field.get(object)));
                } else {
                    if (controlArrayList.get(i) instanceof ComboBox) {
                        ((ComboBox<String>) controlArrayList.get(i)).setValue(String.valueOf(field.get(object)));
                    }
                }
            }else{
                fillControls(controlArrayList, field.get(object), i);
            }
            i++;
        }
        return controlArrayList;
    }


    private ComboBox makeCombobox(int positionX, int positionY, Class tempClass, String name){
        ComboBox comboBox = new ComboBox(makeContentArray(tempClass));
        comboBox.setLayoutY(positionY);
        comboBox.setLayoutX(positionX);
        if (mtch.reset(name).find())
            name =  mtch.replaceFirst(" " + mtch.group().toLowerCase());
        comboBox.setPromptText("Enter " + name);
        comboBox.setPrefWidth(WIDTH);
        return comboBox;
    }

    private TextField makeTextField(int positionX, int positionY, String name){
        TextField textField = new TextField();
        textField.setLayoutY(positionY);
        textField.setLayoutX(positionX);
        if (mtch.reset(name).find())
            name =  mtch.replaceFirst(" " + mtch.group().toLowerCase());
        textField.setPromptText("Enter " + name + "...");
        textField.setPrefWidth(WIDTH);
        return textField;
    }

    private ArrayList<Control> makeControl(int positionX, int positionY, Field[] fields){
        for (Field temp: fields){
            if ((temp.getType() == int.class) || (temp.getType() == double.class)){
                controls.add(makeTextField(positionX, positionY, temp.getName()));
            }else{
                if (temp.getType() == Material.class){
                    makeControl(positionX + X, positionY, temp.getType().getFields());
                }else {
                    controls.add(makeCombobox(positionX, positionY, temp.getType(), temp.getName()));
                }

            }
            positionY += Y_MARGIN;
        }
        return controls;
    }

    public ArrayList<Control> makeComponents(Class tempClass){
        makeControl(X, Y_START, tempClass.getFields());
        return controls;
    }
}
