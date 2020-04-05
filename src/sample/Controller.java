package sample;


import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.clothes.Clothes;
import sample.components.ComponentsMaker;
import sample.loader.ClassReader;
import sample.material.Material;
import sample.model.ItemMaker;
import sample.model.cbContentMaker;
import sample.model.ItemsSearcher;
import sample.storage.ObjectStorage;

import java.util.ArrayList;


public class Controller {

    private static final String ERROR_TITLE = "Invalid values";
    private static final String ERROR_INF_SYMBOLS = "Please, correct your input. Some fields contain invalid symbols. \n " +
            "Make sure, that all text fields, except name field, have integer or float values";
    private static final String ERROR_INF_EMPTY_FIELD = "Please, correct your input. Some fields are empty.";
    private static final String ERROR_INF_INTERNAL = "Sorry, there are some internal exception.\n Please, keep calm and " +
            "send message to developer";

    public Button btDelete;

    private ObjectStorage objectStorage = new ObjectStorage();

    @FXML
    private AnchorPane apForm;

    @FXML
    private Button btnMaterial;
    @FXML
    private ComboBox<String> cbxClothes;


    @FXML
    private ListView<String> lvMaterialElements;

    @FXML
    private CheckBox cbExistMaterial;

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

        cbContentMaker cbContentMaker = new cbContentMaker();
        cbxClothes.getSelectionModel().selectedIndexProperty().addListener(
                (ChangeListener<? super Number>) (observable, oldValue, newValue) -> oncbClothesSelectItem());

        lvClothesElements.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
                onSelectLvClothesItem(newValue));

        lvMaterialElements.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
                onSelectLvMaterialItem(newValue.intValue()));

        cbExistMaterial.selectedProperty().addListener((observable, oldValue, newValue) ->
                onCbExistMaterialSelect(newValue));
        cbContentMaker.makeControlsList(objectStorage.getClassList());
        cbxClothes.setItems(cbContentMaker.getItemsList());

    }

    private void onCbExistMaterialSelect(boolean value){
        if (!objectStorage.isMaterial()){
            ComponentsMaker componentsMaker = new ComponentsMaker();
            ArrayList<Control> controls = componentsMaker.makeComponents(objectStorage.getCurrentObject(), value,
                    objectStorage.makeMaterialList());
            displayComponents(controls);
            objectStorage.setControls(controls);
        }
    }

    private void onSelectLvClothesItem(Number newValue){
        if (newValue.intValue() >= 0) {
            cbExistMaterial.disableProperty().set(false);
            objectStorage.setMaterial(false);
            ComponentsMaker componentsMaker = new ComponentsMaker();
            Clothes clothes = objectStorage.getClothesArrayList().get(newValue.intValue());
            boolean isInList = objectStorage.isMaterialInList(clothes.material);
            cbExistMaterial.setSelected(isInList);
            objectStorage.setCurrentObject(clothes.getClass());
            tfName.setText(lvClothesElements.getSelectionModel().getSelectedItem());
            try {
                ArrayList<Control> controls = componentsMaker.fillControls(componentsMaker.makeComponents(
                        clothes.getClass(), isInList, objectStorage.makeMaterialList())
                        , clothes, 0, objectStorage.isMaterialInList(clothes.material), clothes.material);
                displayComponents(controls);
                objectStorage.setControls(controls);


            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSelectLvMaterialItem(int newValue){
        if (newValue >= 0) {
            objectStorage.setMaterial(true);
            ComponentsMaker componentsMaker = new ComponentsMaker();
            Material material = objectStorage.getMaterials().get(newValue);
            tfName.setText(lvMaterialElements.getSelectionModel().getSelectedItem());
            try {
                ArrayList<Control> controls = componentsMaker.fillControls(componentsMaker.makeComponents(
                        Material.class, false,null)
                        , material, 0, false, null);
                displayComponents(controls);
                objectStorage.setControls(controls);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onMouseEntered(MouseEvent event) {
        if (event.getSource() instanceof ListView){
            ((ListView) event.getSource()).getSelectionModel().select(-1);
        }
    }



    private void oncbClothesSelectItem(){
        cbExistMaterial.disableProperty().set(false);
        tfName.clear();
        objectStorage.setMaterial(false);
        ItemsSearcher itemsSearcher = new ItemsSearcher();
        ComponentsMaker componentsMaker = new ComponentsMaker();
        Class currClass = itemsSearcher.searchByAnnotation(cbxClothes.getValue(),objectStorage);
        objectStorage.setCurrentObject(currClass);
        Boolean isExistChosenMaterial = cbExistMaterial.isSelected();
        ArrayList<Control> controls =
                componentsMaker.makeComponents(currClass,
                        isExistChosenMaterial, objectStorage.makeMaterialList());
        displayComponents(controls);
        objectStorage.setControls(controls);

    }

    private void displayComponents(ArrayList<Control> controls){
        apFieldsView.getChildren().removeAll(objectStorage.getControls());
        if (controls != null) {
            apFieldsView.getChildren().addAll(controls);
        }
    }

    private boolean isAllFieldsFilled(){
        if (tfName.getText().isEmpty()){
            return false;
        }
        for (Control control : objectStorage.getControls()){
            if (control instanceof ComboBox){
                if (((ComboBox) control).getSelectionModel().isEmpty()){
                    return false;
                }
            }else {
                if (control instanceof TextField){
                    if (((TextField) control).getText().isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @FXML
    void onMouseClick(MouseEvent event) {
        if (isAllFieldsFilled()) {
            ItemMaker itemMaker = new ItemMaker();
            String name = tfName.getText();
            if (objectStorage.isMaterial()) {
                try {
                    Material material = itemMaker.parseForMaterial(objectStorage.getControls(), name,
                            objectStorage.findMaterialByName(tfName.getText()));
                    if (material != null) {
                        lvMaterialElements.getItems().add(name);
                        objectStorage.getMaterials().add(material);
                    }
                } catch (IllegalAccessException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(ERROR_TITLE);
                    alert.setHeaderText(ERROR_INF_INTERNAL);
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }catch (NumberFormatException ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(ERROR_TITLE);
                    alert.setHeaderText(ERROR_INF_SYMBOLS);
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            } else {
                Clothes clothes = objectStorage.findByName(name);
                try {
                    clothes = itemMaker.parseResult(objectStorage.getControls(), objectStorage.getCurrentObject(), name, clothes,
                            cbExistMaterial.isSelected(), objectStorage.getMaterials());
                    if (clothes != null) {
                        lvClothesElements.getItems().add(name);
                        objectStorage.getClothesArrayList().add(clothes);
                    }
                } catch (IllegalAccessException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(ERROR_TITLE);
                    alert.setHeaderText(ERROR_INF_INTERNAL);
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                } catch (NumberFormatException ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(ERROR_TITLE);
                    alert.setHeaderText(ERROR_INF_SYMBOLS);
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(ERROR_TITLE);
            alert.setHeaderText(ERROR_INF_EMPTY_FIELD);
            alert.showAndWait();
        }
    }

    @FXML
    void onMaterialButtonClick(MouseEvent event) {
        cbExistMaterial.disableProperty().set(true);
        tfName.clear();
        objectStorage.setMaterial(true);
        objectStorage.setCurrentObject(Material.class);
        ComponentsMaker componentsMaker = new ComponentsMaker();
        ArrayList<Control> controls =
                componentsMaker.makeComponents(Material.class, false, null);
        displayComponents(controls);
        objectStorage.setControls(controls);
    }


    public void btnDeleteonMouseClick(MouseEvent event) {

        tfName.clear();
        cbExistMaterial.disableProperty().set(true);
        if (objectStorage.isMaterial()) {

            ArrayList<Material> materials = objectStorage.getMaterials();
            materials.remove(lvMaterialElements.getSelectionModel().getSelectedIndex());
            objectStorage.setMaterials(materials);
            lvMaterialElements.getItems().remove(lvMaterialElements.getSelectionModel().getSelectedIndex());
            lvMaterialElements.getSelectionModel().select(-1);
        }else{

            ArrayList<Clothes> tempArr = objectStorage.getClothesArrayList();
            tempArr.remove(lvClothesElements.getSelectionModel().getSelectedIndex());
            objectStorage.setClothesArrayList(tempArr);
            lvClothesElements.getItems().remove(lvClothesElements.getSelectionModel().getSelectedIndex());
            lvClothesElements.getSelectionModel().select(-1);
        }
        displayComponents(null);
    }
}
