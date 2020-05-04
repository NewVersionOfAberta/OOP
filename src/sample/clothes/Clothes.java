package sample.clothes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sample.Reflectable;
import sample.material.Material;
import sample.types.ClothesSize;
import sample.types.Sex;
import sample.types.Style;

import java.io.Serializable;


@Reflectable(name = "Clothes")
public class Clothes implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public Sex sex;
    public Style style;
    public ClothesSize size;
    public double price;
    public Material material;

}
