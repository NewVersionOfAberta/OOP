package sample.model;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import sample.clothes.Clothes;
import sample.material.Material;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemMaker {


    private int controlArrIndex = 0;

    public Boolean getExistMaterial() {
        return isExistMaterial;
    }

    public void setExistMaterial(Boolean existMaterial) {
        isExistMaterial = existMaterial;
    }

    private Boolean isExistMaterial = false;


    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    private List<Material> materials;

    private void setIntValue(Field field, Control control, Object object) throws IllegalAccessException, NumberFormatException {
        if (control instanceof TextField){
            field.setInt(object, Integer.parseInt(((TextField)control).getText()));
        }
    }

    private void setDoubleValue(Field field, Control control, Object object) throws IllegalAccessException, NumberFormatException {
        if (control instanceof TextField){
            field.setDouble(object, Double.parseDouble(((TextField) control).getText()));
        }
    }

    private void setEnumValue(Field field, Control control, Object object) throws IllegalAccessException {

        for (Field typeField : field.getType().getFields()) {
            if (Objects.equals(typeField.getName(), ((ComboBox<String>) control).getValue())) {
                field.set(object, Enum.valueOf((Class<Enum>) field.getType(),
                        ((ComboBox<String>) control).getValue()));
            }
        }
    }

    private Material findMaterialByName(String name){
        for (Material material : getMaterials()){
            if (material.getName().equals(name)){
                return material;
            }
        }
        return null;
    }

    private void parseForFields(ArrayList<Control> controls, Class parseClass, Object object) throws IllegalAccessException {
        for (Field field : parseClass.getFields()) {
            if (field.getType() != Material.class) {
                if (field.getType() == int.class) {
                    setIntValue(field, controls.get(controlArrIndex), object);
                } else if (field.getType() == double.class) {
                    if (controls.get(controlArrIndex) instanceof TextField) {
                        setDoubleValue(field, controls.get(controlArrIndex), object);
                    }
                } else if (controls.get(controlArrIndex) instanceof ComboBox) {
                    setEnumValue(field, controls.get(controlArrIndex), object);
                }
                controlArrIndex++;
            } else {
                Material material;
                if (isExistMaterial){
                    material = findMaterialByName(((ComboBox<String>)controls.get(controlArrIndex)).getValue());
                }else {
                    material = new Material();
                    parseForFields(controls, field.getType(), material);

                }
                field.set(object, material);
            }
        }
    }

    public Clothes parseResult(ArrayList<Control> controls, Class parseClass, String name, Clothes existClothes,
                               Boolean isExistMaterial, List<Material> materials)
            throws IllegalAccessException {
        setExistMaterial(isExistMaterial);
        setMaterials(materials);
        if (existClothes != null){
            parseForFields(controls, parseClass, existClothes);
            return null;
        }else {
            Clothes clothes;
            try {
                clothes = (Clothes) parseClass.getConstructor().newInstance();
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
            clothes.setName(name);
            parseForFields(controls, parseClass, clothes);
            return clothes;
        }
    }

    public Material parseForMaterial(ArrayList<Control> controls, String name, Material material) throws IllegalAccessException {
        if (material != null){
            parseForFields(controls, Material.class, material);
            return null;
        }else {
            Material newMaterial = new Material();
            newMaterial.setName(name);
            parseForFields(controls, Material.class, newMaterial);
            return newMaterial;
        }
    }
}
