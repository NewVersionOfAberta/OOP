package sample.clothes;

import sample.Reflectable;
import sample.material.Material;
import sample.types.*;

@Reflectable(name = "Suit")
public class Suit extends Clothes {
    public TopType topType;
    public ShirtType shirtType;
    public TrousersType trousersType;

}


