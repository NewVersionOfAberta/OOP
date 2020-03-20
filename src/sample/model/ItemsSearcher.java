package sample.model;

import sample.Reflectable;
import sample.storage.ObjectStorage;

import java.lang.annotation.Annotation;

public class ItemsSearcher {
    public Class searchByAnnotation(String name, ObjectStorage objectStorage){
        for (Class tempClass : objectStorage.getClassList()){
            Annotation annotation = tempClass.getAnnotation(Reflectable.class);
            if (annotation instanceof Reflectable){
                if (((Reflectable) annotation).name() == name){
                    return tempClass;
                }
            }
        }
        return null;
    }
}
