package sample.serialize;

import sample.clothes.Clothes;
import sample.material.Material;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

interface ISerialization {
    List<Material> getMaterials();
    List<Clothes> getClothes();

    void Serialize(List<Material> materials, List<Clothes> clothes, String fileName) throws IOException, IllegalAccessException;
    void Deserialize(String fileName) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
