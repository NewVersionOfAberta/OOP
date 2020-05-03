package sample.clothes;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import sample.Reflectable;
import sample.material.Material;
import sample.types.*;

@Reflectable(name = "Coat") public class Coat extends Outwear{
    public CoatModel coatModel;

}

