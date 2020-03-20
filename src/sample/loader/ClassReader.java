package sample.loader;

import javafx.scene.control.Alert;
import sample.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class ClassReader {
    public void setClasses(HashSet<Class> classes) {
        this.classes = classes;
    }

    private HashSet<Class> classes = new HashSet<>();

    public HashSet<Class> getClasses() {
        return classes;
    }

    private HashSet<Class> filterLeaves(HashSet<Class> allClasses){
        Class temp;
        HashSet<Class> parents = new HashSet<>();
        for (Class c : allClasses){
            temp = c.getSuperclass();
            parents.add(temp);
        }
        allClasses.removeAll(parents);
        return allClasses;
    }

    public void loadClasses(){
        try {
            HashSet<String> fullNames = new HashSet<>();

            ClassLoader classLoader = Main.class.getClassLoader();
            String packageName = "sample/clothes";
            URL uPackage = classLoader.getResource(packageName);
            InputStream in = (InputStream) uPackage.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.endsWith(".class"))
                    fullNames.add("sample.clothes." + line.substring(0, line.lastIndexOf('.')));
            }

            for (String s : fullNames) {
                classes.add(Class.forName(s));
            }
            setClasses(filterLeaves(classes));

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Loading classes failed");
            alert.setContentText("Error occured while load classes from file!");
        }
    }
}
