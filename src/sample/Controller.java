package sample;


import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.clothes.Clothes;
import sample.components.ComponentsMaker;
import sample.loader.ClassReader;
import sample.model.ClothesMaker;
import sample.model.ControlsCreator;
import sample.model.ItemsSearcher;
import sample.storage.ObjectStorage;

import java.util.ArrayList;


public class Controller {
    public Button btDelete;

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
    private ListView<String> lvClothesElements;

    @FXML
    private TextField tfName;

    @FXML
    public void initialize(){
        ClassReader classReader = new ClassReader();
        classReader.loadClasses();
        objectStorage.setClassList(classReader.getClasses());

        ControlsCreator controlsCreator = new ControlsCreator();
        cbxClothes.getSelectionModel().selectedIndexProperty().addListener((ChangeListener) (observable, oldValue, newValue) -> {
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

        });

        lvClothesElements.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() >= 0) {
                ComponentsMaker componentsMaker = new ComponentsMaker();
                Clothes clothes = objectStorage.getClothesArrayList().get(newValue.intValue());
                objectStorage.setCurrentObject(clothes.getClass());
                try {
                    ArrayList<Control> controls = componentsMaker.fillControls(componentsMaker.makeComponents(clothes.getClass()), clothes, 0);
                    displayComponents(controls);
                    objectStorage.setControls(controls);
                    tfName.setText(lvClothesElements.getSelectionModel().getSelectedItem());

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        controlsCreator.makeControlsList(objectStorage.getClassList());
        cbxClothes.setItems(controlsCreator.getItemsList());

    }

    private void displayComponents(ArrayList<Control> controls){
        apFieldsView.getChildren().removeAll(objectStorage.getControls());
        if (controls != null) {
            apFieldsView.getChildren().addAll(controls);
        }
    }

    @FXML
    void onMouseClick(MouseEvent event) {
        ClothesMaker clothesMaker = new ClothesMaker();
        String name = tfName.getText();
        try {
            Clothes clothes = objectStorage.findByName(name);
            if (clothes != null) {
                clothesMaker.parseResult(objectStorage.getControls(), objectStorage.getCurrentObject(), name, clothes);
            }else{
                clothes = clothesMaker.parseResult(objectStorage.getControls(), objectStorage.getCurrentObject(), name, null);
                lvClothesElements.getItems().add(name);
                objectStorage.getClothesArrayList().add(clothes);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void onSelect(){
//        ItemsSearcher itemsSearcher = new ItemsSearcher();
//        ComponentsMaker componentsMaker = new ComponentsMaker();
//        componentsMaker.makeComponents(itemsSearcher.searchByAnnotation(cbxClothes.getValue(), objectStorage));
//        displayComponents(componentsMaker.getControls());
//        objectStorage.setControls(componentsMaker.getControls());
    }

    public void btnDeleteonMouseClick(MouseEvent event) {
        ArrayList<Clothes> tempArr = objectStorage.getClothesArrayList();
        tempArr.remove(lvClothesElements.getSelectionModel().getSelectedIndex());
        objectStorage.setClothesArrayList(tempArr);
        lvClothesElements.getItems().remove(lvClothesElements.getSelectionModel().getSelectedIndex());
        displayComponents(null);
    }
}
