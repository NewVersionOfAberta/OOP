package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import sample.loader.ClassReader;

import java.lang.annotation.Annotation;
import java.util.HashSet;

public class Controller {
    @FXML
    private ComboBox<String> cbClothes;

    @FXML
    public void initialize(){
        ClassReader classReader = new ClassReader();
        classReader.loadClasses();
        HashSet<Class> classes = classReader.getClasses();
        ObservableList<String> classNames = FXCollections.observableArrayList();
        for (Class tempClass : classes){
            Annotation annotation = tempClass.getAnnotation(Reflectable.class);
            if (annotation instanceof Reflectable) {
                Reflectable reflectable = (Reflectable)annotation;
                classNames.add(reflectable.name());
            }
        }
        cbClothes.setItems(classNames);

    }

}
