package sample.clothes;

import sample.Reflectable;
import sample.types.Cut;

@Reflectable(name = "Платье")
public class Dress extends Clothes{
    int Length;
    Cut cut;

    @Override public String getName(){
        return "Платье";
    }
}

