package sample.storage;

import javafx.scene.control.TextField;
import sample.clothes.Clothes;

import java.util.ArrayList;
import java.util.HashSet;

public class ObjectStorage {

    public Clothes getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(Clothes currentObject) {
        this.currentObject = currentObject;
    }

    private Clothes currentObject;

    public HashSet<Class> getClassList() {
        return classList;
    }

    public void setClassList(HashSet<Class> classList) {
        this.classList = classList;
    }

    private HashSet<Class> classList = new HashSet<>();

    public ArrayList<TextField> getTextFields() {
        return textFields;
    }

    public void setTextFields(ArrayList<TextField> textFields) {
        this.textFields = textFields;
    }

    private ArrayList<TextField> textFields= new ArrayList<>();

}
