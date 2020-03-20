package sample.clothes;

import sample.Reflectable;
import sample.types.CoatModel;

@Reflectable(name = "Пальто") public class Coat extends Outwear{
    CoatModel coatModel;

     @Override public String getName(){
         return "Пальто";
     }
}

