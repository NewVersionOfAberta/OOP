package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.Reflectable;
import sample.loader.ClassReader;

import java.lang.annotation.Annotation;
import java.util.HashSet;

public class ControlsCreator {

    public ObservableList<String> getItemsList() {
        return itemsList;
    }

    ObservableList<String> itemsList = FXCollections.observableArrayList();

    public void makeControlsList(HashSet<Class> classes){

       // ObservableList<String> classNames = FXCollections.observableArrayList();
        for (Class tempClass : classes){
            Annotation annotation = tempClass.getAnnotation(Reflectable.class);
            if (annotation instanceof Reflectable) {
                Reflectable reflectable = (Reflectable)annotation;
                itemsList.add(reflectable.name());
            }
        }
    }

}
