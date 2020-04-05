package sample.clothes;

import sample.Reflectable;
import sample.material.Material;
import sample.types.ClothesSize;
import sample.types.Sex;
import sample.types.Style;

@Reflectable(name = "Clothes")
public class Clothes {
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

