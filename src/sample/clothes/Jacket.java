package sample.clothes;

import sample.Reflectable;
import sample.types.JacketType;

@Reflectable(name = "Куртка")
public class Jacket extends Outwear {
    JacketType jacketType;

    @Override public String getName(){
        return "Жакет";
    }
}

