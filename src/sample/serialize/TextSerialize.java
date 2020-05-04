package sample.serialize;

import sample.Reflectable;
import sample.clothes.Clothes;
import sample.material.Material;


import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Reflectable(name = "*.txt")
public class TextSerialize implements ISerialization{
    private static final String constName="*";

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

    private Material findMaterialByName(String name){
        for (Material material: materials){
            if (material.getName().equals(name)){
                return material;
            }
        }
        return null;
    }

    private void writeFields(Field[] fields, Object temp, DataOutputStream dos) throws IllegalAccessException, IOException {
        for (Field field : fields){
            if (field.getType() == int.class) {
                dos.writeInt((Integer) field.get(temp));
            }else if (field.getType() == double.class){
                dos.writeDouble((Double) field.get(temp));
            }else if (field.getType() == Material.class){
                Material material = (Material)field.get(temp);
                Material tempMaterial = findMaterialByName(material.getName());
                if (tempMaterial != null){
                    dos.writeUTF(tempMaterial.getName());
                }else{
                    dos.writeUTF(constName);
                    writeFields(material.getClass().getFields(), material, dos);
                }
            }else
                dos.writeUTF(String.valueOf(field.get(temp)));
        }
    }

    private Object makeObject(Field[] fields, Object obj, DataInputStream dis) throws IOException, IllegalAccessException {
        for (Field field: fields){
            if (field.getType() == int.class){
                field.setInt(obj, dis.readInt());
            }else if (field.getType() == double.class){
                field.setDouble(obj, dis.readDouble());
            }else if (field.getType() == Material.class) {
                String name = dis.readUTF();
                if (name.equals(constName)) {
                    Material material = new Material();
                    field.set(obj, makeObject(Material.class.getFields(), material, dis));
                }else{
                    Material material = findMaterialByName(name);
                    field.set(obj, material);
                }
            }else {
                String name = dis.readUTF();
                field.set(obj, Enum.valueOf((Class<Enum>) field.getType(), name));
            }
        }
        return obj;
    }


    @Override
    public void Serialize(List<Material> materials, List<Clothes> clothes, String fileName) throws IOException, IllegalAccessException {
        this.clothes = clothes;
        this.materials = materials;
        FileOutputStream fos = new FileOutputStream(fileName);
        try (DataOutputStream dos = new DataOutputStream(fos)) {
            dos.writeInt(materials.size());
            for (Material material: materials){
                dos.writeUTF(material.getClass().getName());
                dos.writeUTF(material.getName());
                writeFields(material.getClass().getFields(), material, dos);
            }
            dos.writeInt(clothes.size());
            for (Clothes temp: clothes){
                dos.writeUTF(temp.getClass().getName());
                dos.writeUTF(temp.getName());
                writeFields(temp.getClass().getFields(), temp, dos);
            }
        }
    }

    @Override
    public void Deserialize(String fileName) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        FileInputStream fis = new FileInputStream(fileName);
        try (DataInputStream dis = new DataInputStream(fis)) {
            int materialCount = dis.readInt();
            for (int i = 0; i < materialCount; i++){
                String className = dis.readUTF();
                Material material = ((Material)Class.forName(className).getConstructor().newInstance());
                material.setName(dis.readUTF());
                makeObject(material.getClass().getFields(), material, dis);
                materials.add(material);
            }
            int clothesCount = dis.readInt();
            for (int i = 0; i < clothesCount; i++){
                String className = dis.readUTF();
                Clothes tempClothes = ((Clothes)Class.forName(className).getConstructor().newInstance());
                tempClothes.setName(dis.readUTF());
                makeObject(tempClothes.getClass().getFields(), tempClothes, dis);
                clothes.add(tempClothes);
            }
        }
    }
}
