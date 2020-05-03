package sample.serialize;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import sample.Reflectable;
import sample.clothes.Clothes;
import sample.material.Material;
import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Reflectable(name = "*.json")
public class JSONSerialize implements ISerialization {

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
        ObjectMapper objectMapper = new ObjectMapper();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType("sample.")
                .allowIfBaseType(List.class)
                .allowIfBaseType(Map.class)
                .allowIfSubType("sample.")
                .build();
//        objectMapper.activateDefaultTyping(ptv); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        Map<String, Object> map = new HashMap<>();
        map.put("materials", materials);
        map.put("clothes", clothes);
        String serialized = objectMapper.writeValueAsString(map);
        System.out.println(serialized);
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(serialized);
        fileWriter.close();


    }

    private Material isMaterialInList(String name){
        for (Material material : materials){
            if (material.getName().equals(name)){
                return material;
            }
        }
        return null;
    }

    @Override
    public void Deserialize(String fileName) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ObjectMapper objectMapper = new ObjectMapper();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfBaseType("sample.")
                .allowIfBaseType(List.class)
                .allowIfBaseType(Map.class)
                .allowIfSubType("sample.")
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        FileReader fileReader = new FileReader(fileName);
        Map<String, List<Object>> clothes1 = objectMapper.readValue(fileReader, new TypeReference<Map<String, List<Object>>>() {
        });
        materials = clothes1.get("materials")
                .stream()
                .map(material -> (Material) material)
                .collect(Collectors.toList());

        clothes = clothes1.get("clothes")
                .stream()
                .map(c -> (Clothes) c)
                .collect(Collectors.toList());
        for (Clothes c : clothes){
            Material m = isMaterialInList(c.material.getName());
            if (m != null){
                c.material = m;
            }
        }
        fileReader.close();
    }
}
