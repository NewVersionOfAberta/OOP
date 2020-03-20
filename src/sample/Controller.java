package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sample.components.ComponentsMaker;
import sample.loader.ClassReader;
import sample.model.ControlsCreator;
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
    private ComboBox<String> cbxClothes;

    @FXML
    private Button btEnter;

    @FXML
    private AnchorPane apFieldsView;

    @FXML
    private ListView lvObjects;

    @FXML
    private TextField tfName;

    @FXML
    public void initialize(){


        ClassReader classReader = new ClassReader();
        classReader.loadClasses();
        objectStorage.setClassList(classReader.getClasses());

        ControlsCreator controlsCreator = new ControlsCreator();
        cbxClothes.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                //ToDo: Refactor
                ItemsSearcher itemsSearcher = new ItemsSearcher();
                ComponentsMaker componentsMaker = new ComponentsMaker();
                Class currClass = itemsSearcher.searchByAnnotation(cbxClothes.getValue(),objectStorage);
                objectStorage.setCurrentObject(currClass);
                ArrayList<Control> controls =
                        componentsMaker.makeComponents(itemsSearcher.searchByAnnotation(cbxClothes.getValue(), objectStorage));
                displayComponents(controls);
                objectStorage.setControls(controls);
               // objectStorage.setClothesArrayList(objectStorage.getClothesArrayList().add());

            }
        });
        controlsCreator.makeControlsList(objectStorage.getClassList());
        cbxClothes.setItems(controlsCreator.getItemsList());

    }

    public void displayComponents(ArrayList<Control> controls){
        apFieldsView.getChildren().addAll(controls);
    }

    @FXML
    void onMouseClick(ActionEvent event) {

    }

    @FXML
    public void onSelect(){
//        ToDo: Refactor this part
//        ItemsSearcher itemsSearcher = new ItemsSearcher();
//        ComponentsMaker componentsMaker = new ComponentsMaker();
//        componentsMaker.makeComponents(itemsSearcher.searchByAnnotation(cbxClothes.getValue(), objectStorage));
//        displayComponents(componentsMaker.getControls());
//        objectStorage.setControls(componentsMaker.getControls());
    }

}
