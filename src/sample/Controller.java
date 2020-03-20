package sample;


import com.sun.xml.internal.bind.v2.TODO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import sample.components.ComponentsMaker;
import sample.loader.ClassReader;
import sample.model.CbCreator;
import sample.model.ItemsSearcher;
import sample.storage.ObjectStorage;

import java.util.ArrayList;


public class Controller {
    public ObjectStorage getObjectStorage() {
        return objectStorage;
    }

    public void setObjectStorage(ObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }

    private ObjectStorage objectStorage = new ObjectStorage();

    @FXML
    private AnchorPane apForm;

    @FXML
    private ComboBox<String> cbClothes;

    @FXML
    private Button btEnter;

    @FXML
    public void initialize(){
        ClassReader classReader = new ClassReader();
        classReader.loadClasses();
        objectStorage.setClassList(classReader.getClasses());

        CbCreator cbCreator = new CbCreator();

        cbCreator.makeCbItemsList(objectStorage.getClassList());
        cbClothes.setItems(cbCreator.getItemsList());

    }

    public void displayComponents(ArrayList<TextField> textFields){
        apForm.getChildren().addAll(textFields);
    }

    @FXML
    public void onSelect(ActionEvent actionEvent){
        //ToDo: Refactor this part
        ItemsSearcher itemsSearcher = new ItemsSearcher();
        ComponentsMaker componentsMaker = new ComponentsMaker();
        componentsMaker.makeComponents(itemsSearcher.searchByAnnotation(cbClothes.getValue(), objectStorage));
        displayComponents(componentsMaker.getTextFields());
        objectStorage.setTextFields(componentsMaker.getTextFields());
    }

}
