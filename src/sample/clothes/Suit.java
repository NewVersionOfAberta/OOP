package sample.clothes;

import sample.Reflectable;
import sample.types.ShirtType;
import sample.types.TopType;
import sample.types.TrousersType;

@Reflectable(name = "Suit")
public class Suit extends Clothes {
    public TopType toptype;
    public ShirtType shirtType;
    public TrousersType trousersType;

}


