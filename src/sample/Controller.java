package sample;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
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
import sample.serialize.SerializeController;
import sample.storage.ObjectStorage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    private static final String ERROR_TITLE = "Invalid values";
    private static final String NO_SELECTED_TITLE = "No selected items";
    private static final String NO_SELECTED_HEADER = "Select item you want to delete";
    private static final String ERROR_INF_SYMBOLS = "Please, correct your input. Some fields contain invalid symbols. \n " +
            "Make sure, that all text fields, except name field, have integer or float values";
    private static final String ERROR_INF_EMPTY_FIELD = "Please, correct your input. Some fields are empty.";
    private static final String ERROR_INF_INTERNAL = "Sorry, there are some internal exception.\n Please, keep calm and " +
            "send message to developer";
    private static final String ERROR_FILE_OPEN = "File is incorrect.\n Please, choose another one.";

    private static final int MAX_LENGTH = 15;

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
    private MenuItem miOpen;

    @FXML
    private MenuItem miSave;

    @FXML
    public void initialize(){
        ClassReader classReader = new ClassReader();
        classReader.loadClasses("sample/clothes");
        addTextLimiter(tfName, MAX_LENGTH);
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

    public static void addTextLimiter(final TextField tf, final int maxLength) {
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (tf.getText().length() > maxLength) {
                    String s = tf.getText().substring(0, maxLength);
                    tf.setText(s);
                }
            }
        });
    }

    private void onCbExistMaterialSelect(boolean value){
        if (!objectStorage.isMaterial() && objectStorage.getCurrentObject() != null){
            ComponentsMaker componentsMaker = new ComponentsMaker();
            ArrayList<Control> controls = componentsMaker.makeComponents(objectStorage.getCurrentObject(), value,
                    objectStorage.makeMaterialList());
            displayComponents(controls);
            objectStorage.setControls(controls);
        }
    }

    private void onSelectLvClothesItem(Number newValue){
        if (newValue.intValue() >= 0) {
            btDelete.disableProperty().setValue(false);
            btEnter.disableProperty().setValue(false);
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
            btDelete.disableProperty().setValue(false);
            btEnter.disableProperty().setValue(false);
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
        btDelete.disableProperty().setValue(false);
        btEnter.disableProperty().setValue(false);
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
                            objectStorage.findMaterialByName(name));
                    if (material != null) {
                        lvMaterialElements.getItems().add(name);
                        objectStorage.getMaterials().add(material);
                    }
                } catch (IllegalAccessException ex) {
                    showAlert(ERROR_TITLE, ERROR_INF_INTERNAL, ex.getMessage(), Alert.AlertType.ERROR);
                }catch (NumberFormatException ex){
                    showAlert(ERROR_TITLE, ERROR_INF_SYMBOLS, ex.getMessage(), Alert.AlertType.ERROR);
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
                    showAlert(ERROR_TITLE, ERROR_INF_INTERNAL, ex.getMessage(), Alert.AlertType.ERROR);
                } catch (NumberFormatException ex){
                    showAlert(ERROR_TITLE, ERROR_INF_SYMBOLS, ex.getMessage(), Alert.AlertType.ERROR);
                }

            }
        }else {
            showAlert(ERROR_TITLE, ERROR_INF_EMPTY_FIELD, "", Alert.AlertType.ERROR);
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

    private void showAlert(String title, String header, String info, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(info);
        alert.showAndWait();
    }

    public void btnDeleteonMouseClick(MouseEvent event) {

        tfName.clear();
        btDelete.disableProperty().setValue(true);
        btEnter.disableProperty().setValue(true);
        cbExistMaterial.disableProperty().set(true);
        if (objectStorage.isMaterial()) {
            if (lvMaterialElements.getSelectionModel().getSelectedIndex() != -1){

            List<Material> materials = objectStorage.getMaterials();
            materials.remove(lvMaterialElements.getSelectionModel().getSelectedIndex());
            objectStorage.setMaterials(materials);
            lvMaterialElements.getItems().remove(lvMaterialElements.getSelectionModel().getSelectedIndex());
            lvMaterialElements.getSelectionModel().select(-1);
            }else{
                showAlert(NO_SELECTED_TITLE, NO_SELECTED_HEADER, "", Alert.AlertType.WARNING);

            }
        }else{
            if (lvClothesElements.getSelectionModel().getSelectedIndex() != -1) {
                List<Clothes> tempArr = objectStorage.getClothesArrayList();
                tempArr.remove(lvClothesElements.getSelectionModel().getSelectedIndex());
                objectStorage.setClothesArrayList(tempArr);
                lvClothesElements.getItems().remove(lvClothesElements.getSelectionModel().getSelectedIndex());
                lvClothesElements.getSelectionModel().select(-1);
            }else{
                showAlert(NO_SELECTED_TITLE, NO_SELECTED_HEADER, "", Alert.AlertType.WARNING);

            }
        }
        displayComponents(null);
    }

    private void fillLvMaterialList(List<Material> materials){
        for (Material material : materials){
            lvMaterialElements.getItems().add(material.getName());
        }
    }

    private void fillLvClothesList(List<Clothes> clothes){
        for (Clothes temp : clothes){
            lvClothesElements.getItems().add(temp.getName());
        }
    }

    @FXML
    void miOpenClick(ActionEvent event) {
        SerializeController serializeController = new SerializeController();
        try {
            serializeController.loadFromFile();
        } catch (IOException | ClassNotFoundException | InstantiationException | InvocationTargetException |
                NoSuchMethodException | IllegalAccessException e) {
            showAlert(ERROR_TITLE, ERROR_INF_INTERNAL, e.getMessage(), Alert.AlertType.ERROR);
        }
        List<Material> materials = serializeController.getMaterials();
        List<Clothes> clothes = serializeController.getClothes();
        if (materials != null){
            objectStorage.setMaterials(materials);
            lvMaterialElements.getItems().removeAll(lvMaterialElements.getItems());
            fillLvMaterialList(objectStorage.getMaterials());
        }
        if (clothes != null) {
            objectStorage.setClothesArrayList(clothes);
            lvClothesElements.getItems().removeAll(lvClothesElements.getItems());
            fillLvClothesList(objectStorage.getClothesArrayList());
        }

        displayComponents(null);
    }

    @FXML
    void miSaveClick(ActionEvent event) {
        SerializeController serializeController = new SerializeController();
        try {
            serializeController.saveToFile(objectStorage.getClothesArrayList(), objectStorage.getMaterials());
        } catch (IOException | InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            showAlert(ERROR_TITLE, ERROR_INF_INTERNAL, e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
