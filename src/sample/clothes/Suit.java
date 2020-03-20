package sample.clothes;

import sample.Reflectable;
import sample.types.ShirtType;
import sample.types.TopType;
import sample.types.TrousersType;

@Reflectable(name = "Костюм")
public class Suit extends Clothes {
    TopType toptype;
    ShirtType shirtType;
    TrousersType trousersType;

    @Override public String getName(){
        return "Костюм";
    }
}


