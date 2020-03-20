package sample.storage;

import sample.clothes.Clothes;

public class ObjectStorage {

    public Clothes getCurrentObject() {
        return currentObject;
    }

    public void setCurrentObject(Clothes currentObject) {
        this.currentObject = currentObject;
    }

    private Clothes currentObject;

}
