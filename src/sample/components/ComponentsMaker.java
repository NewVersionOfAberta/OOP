package sample.components;

import javafx.scene.control.TextField;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ComponentsMaker {
    final int X_MARGIN = 10;
    final int Y = 10;


    public ArrayList<TextField> getTextFields() {
        return textFields;
    }

    public void setTextFields(ArrayList<TextField> textFields) {
        this.textFields = textFields;
    }

    private ArrayList<TextField> textFields = new ArrayList<>();


    public void makeComponents(Class tempClass){
        ArrayList<TextField> tempArr = new ArrayList<>();
        int xCoord = 10;
        Field fields[] = tempClass.getFields();
        for(Field temp: fields){
            TextField t = new TextField();
            t.setLayoutX(xCoord);
            xCoord += X_MARGIN;
            t.setLayoutY(Y);
            tempArr.add(new TextField());
        }
        setTextFields(tempArr);
    }
}
