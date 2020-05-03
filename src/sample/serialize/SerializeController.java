package sample.serialize;

import javafx.stage.FileChooser;
import sample.Reflectable;
import sample.clothes.Clothes;
import sample.loader.ClassReader;
import sample.material.Material;
import sample.model.cbContentMaker;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class SerializeController {


    private List<Material> materials;
    private List<Clothes> clothes;
    private HashSet<Class> classes;

    public List<Material> getMaterials() {
        return materials;
    }

    public List<Clothes> getClothes() {
        return clothes;
    }

    private void readClasses() {
        ClassReader classReader = new ClassReader();
        classReader.loadClasses("sample/serialize");
        classes = classReader.getClasses();
    }

    private ArrayList<String> getFilters() {
        cbContentMaker cbContentMaker = new cbContentMaker();
        cbContentMaker.makeControlsList(classes);
        return new ArrayList<>(cbContentMaker.getItemsList());
    }

    private ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Class cl : classes) {
            if (cl.getAnnotation(Reflectable.class) instanceof Reflectable) {
                names.add(cl.getName().substring(cl.getName().lastIndexOf(".") + 1));
            }
        }
        return names;
    }

    private String makeSaveDialog() {
        FileChooser fileChooser = new FileChooser();
        ArrayList<String> filters = getFilters();
        ArrayList<String> names = getNames();
        for (int i = 0; i < filters.size(); i++) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(names.get(i), filters.get(i)));
        }
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    private String makeOpenDialog() {
        FileChooser fileChooser = new FileChooser();
        ArrayList<String> filters = getFilters();
        ArrayList<String> names = getNames();
        for (int i = 0; i < filters.size(); i++) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(names.get(i), filters.get(i)));
        }
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            return file.getPath();
        }
        return null;
    }

    private Object getSerializator(String extension) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Annotation annotation;
        Object object = null;
        for (Class cl : classes) {
            annotation = cl.getAnnotation(Reflectable.class);
            if (annotation instanceof Reflectable) {
                if (((Reflectable) annotation).name().equals("*." + extension)) {
                    Constructor constructor = cl.getConstructor();
                    object = constructor.newInstance();
                }
            }
        }
        return object;
    }

    public void saveToFile(List<Clothes> clothes, List<Material> materials) throws
            IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        readClasses();
        String fileName = makeSaveDialog();
        if (fileName != null) {
            String extension = "";
            if (fileName.lastIndexOf('.') != -1) {
                extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            }
            Object obj = getSerializator(extension);
            if (obj instanceof ISerialization) {
                ((ISerialization) obj).Serialize(materials, clothes, fileName);
            }else{
                ByteSerialize byteSerialize = new ByteSerialize();
                byteSerialize.Serialize(materials, clothes, fileName);
            }
        }
    }


    public void loadFromFile() throws IOException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {
        readClasses();
        String fileName = makeOpenDialog();
        if (Objects.nonNull(fileName)) {
            String extension = "";
            if (fileName.lastIndexOf('.') != -1) {
                extension = fileName.substring(fileName.lastIndexOf('.') + 1);
            }
            Object obj = getSerializator(extension);
            if (obj instanceof ISerialization) {
                ((ISerialization) obj).Deserialize(fileName);
                materials = ((ISerialization) obj).getMaterials();
                clothes = ((ISerialization) obj).getClothes();
            } else {
                ByteSerialize byteSerialize = new ByteSerialize();
                byteSerialize.Deserialize(fileName);
                materials = byteSerialize.getMaterials();
                clothes = byteSerialize.getClothes();
            }
        }
    }
}
