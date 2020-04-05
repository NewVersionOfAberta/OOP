package sample.storage;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import sample.clothes.Clothes;

import java.util.ArrayList;
import java.util.HashSet;

public class ObjectStorage {

    public Class getCurrentObject() {
        return currentObject;
    }

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    private boolean isExist = false;

    public void setCurrentObject(Class currentObject) {
        this.currentObject = currentObject;
    }

    private Class currentObject;

    public HashSet<Class> getClassList() {
        return classList;
    }

    public void setClassList(HashSet<Class> classList) {
        this.classList = classList;
    }

    private HashSet<Class> classList = new HashSet<>();

    public ArrayList<Clothes> getClothesArrayList() {
        return clothesArrayList;
    }

    public void setClothesArrayList(ArrayList<Clothes> clothesArrayList) {
        this.clothesArrayList = clothesArrayList;
    }

    private ArrayList<Clothes> clothesArrayList = new ArrayList<>();


    public ArrayList<Control> getControls() {
        return controls;
    }

    public void setControls(ArrayList<Control> controls) {
        this.controls = controls;
    }

    private ArrayList<Control> controls = new ArrayList<>();

    public Clothes findByName(String name){
        for (Clothes clothes : clothesArrayList){
            if (clothes.getName().equals(name)){
                return clothes;
            }
        }
        return null;
    }

}
