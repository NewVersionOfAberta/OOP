package sample.serialize;

import sample.Reflectable;
import sample.clothes.Clothes;
import sample.material.Material;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Reflectable(name = "*.*")
public class ByteSerialize implements ISerialization {

    private List<Material> materials = new ArrayList<>();
    private List<Clothes> clothes = new ArrayList<>();

    @Override
    public List<Material> getMaterials() {
        return materials;
    }

    @Override
    public List<Clothes> getClothes() {
        return clothes;
    }

    @Override
    public void Serialize(List<Material> materials, List<Clothes> clothes, String fileName) throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            stream.write(clothes.size());
            for (Object temp : clothes) {
                stream.writeObject(temp);
            }
            stream.write(materials.size());
            for (Material material : materials) {
                stream.writeObject(material);
            }
        }
    }

    @Override
    public void Deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            int objectCounter = objectInputStream.read();
            for (int i = 0; i < objectCounter; i++) {
                clothes.add((Clothes) objectInputStream.readObject());
            }
            objectCounter = objectInputStream.read();
            for (int i = 0; i < objectCounter; i++) {
                materials.add((Material) objectInputStream.readObject());
            }
        }
    }
}
