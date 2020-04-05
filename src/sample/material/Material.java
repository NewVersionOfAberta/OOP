package sample.material;

import sample.types.Color;
import sample.types.MaterialType;
import sample.types.Print;



public class Material {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name = "default";
    public MaterialType type = MaterialType.Cotton;
    public Color color = Color.Black;
    public Print print = Print.No;

}

