package sample.components;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import sample.material.Material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComponentsMaker {
    private static final int Y_START = 40;
    private static final int X = 20;
    private static final int Y_MARGIN = 40;
    private static final int WIDTH = 226;
    private static final Matcher mtch = Pattern.compile("[A-Z]").matcher("");


    private void setMaterials(ObservableList<String> materials) {
        this.materials = materials;
    }

    private ObservableList<String> getMaterials() {
        return materials;
    }

    private ObservableList<String> materials;

    private Boolean getExistMaterial() {
        return isExistMaterial;
    }

    private void setExistMaterial(Boolean existMaterial) {
        isExistMaterial = existMaterial;
    }

    private Boolean isExistMaterial = false;
    private ArrayList<Control> controls = new ArrayList<>();

    private ObservableList<String> makeContentArray(Class tempClass){
        ObservableList<String> observableList = FXCollections.observableArrayList();
        for (Field field : tempClass.getFields()){
            observableList.add(field.getName());
        }
        return observableList;
    }

    public ArrayList<Control> fillControls(ArrayList<Control> controlArrayList, Object object, int i, boolean isMaterialExists, Material material)
            throws IllegalAccessException {
        for (Field field : object.getClass().getFields()) {
            if (field.getType() != Material.class) {
                if (controlArrayList.get(i) instanceof TextField) {
                    ((TextField) controlArrayList.get(i)).setText(String.valueOf(field.get(object)));
                } else if (controlArrayList.get(i) instanceof ComboBox) {
                        ((ComboBox<String>) controlArrayList.get(i)).setValue(String.valueOf(field.get(object)));
                    }

            }else if (isMaterialExists) {
                    ((ComboBox<String>) controlArrayList.get(i)).setValue(material.getName());
                } else {
                    fillControls(controlArrayList, field.get(object), i, false,null);
                }
            i++;
        }
        return controlArrayList;
    }


    private ComboBox makeCombobox(int positionX, int positionY, Class tempClass, String name){
        ComboBox comboBox = new ComboBox<String>(makeContentArray(tempClass));
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

    private void makeControl(int positionX, int positionY, Field[] fields){
        for (Field temp: fields){
            if (temp.getType() == Material.class) {
                if (getExistMaterial())
                    controls.add(makeExistMaterialCb(positionX + X, positionY));
                else{
                    makeControl(positionX + X, positionY, temp.getType().getFields());
                }
            }
            else {

                if ((temp.getType() == int.class) || (temp.getType() == double.class)) {
                    controls.add(makeTextField(positionX, positionY, temp.getName()));
                } else {
                    controls.add(makeCombobox(positionX, positionY, temp.getType(), temp.getName()));
                }
            }
            positionY += Y_MARGIN;
        }
    }

    private ComboBox<String> makeExistMaterialCb(int X, int Y) {
        ComboBox<String> cbMaterial = new ComboBox<>(getMaterials());
        cbMaterial.setLayoutY(Y);
        cbMaterial.setLayoutX(X);
        cbMaterial.setPrefWidth(WIDTH);
        cbMaterial.setPromptText("Select material");
        return cbMaterial;
    }

    public ArrayList<Control> makeComponents(Class tempClass, Boolean isExistMaterial, ObservableList<String> materials){
        setExistMaterial(isExistMaterial);
        setMaterials(materials);
        makeControl(X, Y_START, tempClass.getFields());
        return controls;
    }
}
