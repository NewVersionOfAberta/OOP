package sample.clothes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sample.Reflectable;
import sample.material.Material;
import sample.types.ClothesSize;
import sample.types.Cut;
import sample.types.Sex;
import sample.types.Style;

@Reflectable(name = "Dress")
public class Dress extends Clothes{
    public int length;
    public Cut cut;

}

