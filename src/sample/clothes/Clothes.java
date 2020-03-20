package sample.clothes;

import sample.Reflectable;
import sample.material.Material;
import sample.types.ClothesSize;
import sample.types.Sex;
import sample.types.Style;

@Reflectable(name = "Одежда")
public class Clothes {
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void setSize(ClothesSize size) {
        this.size = size;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    Sex sex;
    Style style;
    ClothesSize size;
    Double price;
    Material material;

    public Sex getSex() {
        return sex;
    }

    public Style getStyle() {
        return style;
    }

    public ClothesSize getSize() {
        return size;
    }

    public Double getPrice() {
        return price;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName(){
        return "Одежда";
    }
}

