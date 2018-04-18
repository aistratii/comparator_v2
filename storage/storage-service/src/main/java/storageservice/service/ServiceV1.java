package storageservice.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.stereotype.Service;
import storageapi.dto.DbDataDtoV1;
import storageapi.dto.WorkQueueDtoV1;

import java.io.*;
import java.util.*;

@Service
public class ServiceV1 {

    private final ObjectMapper objectMapper;

    private final String DB_FILE_NAME = "resources/db.db";

    private final Map<Integer, Set<String>> db;

    private final Set<Pair<String, String>> cloneList;

    private int version = 0;

    public ServiceV1(){
        objectMapper = new ObjectMapper();
        cloneList = new HashSet<Pair<String, String>>();
        db = new HashMap<Integer, Set<String>>();
    }

    public void addWork(WorkQueueDtoV1 dto) {

    }

    public void saveWork(WorkQueueDtoV1 dto) {
        writeObject(dto, "result/wq_serialized.db".replace(".", "_" + version++ +"."));
    }

    public WorkQueueDtoV1 getWorkQueue() {
        try {
            return objectMapper.reader().readValue("result/wq_serialized_" + new Integer(version-1).toString() +".db");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WorkQueueDtoV1();
    }

    public void addDb(DbDataDtoV1 dto) {
        dto.entries()
                .stream()
                .forEach(entry -> {
                    if (db.get(entry.getKey()) == null)
                        db.put(entry.getKey(), new HashSet<String>());

                    db.get(entry.getKey()).addAll(entry.getValue());
                });
    }

    public void saveDb(DbDataDtoV1 dto) {
        dto.entries()
                .stream()
                .forEach(entry -> {
                    if (db.get(entry.getKey()) == null)
                        db.put(entry.getKey(), new HashSet<String>());

                    db.get(entry.getKey()).addAll(entry.getValue());
                });

        writeObject(dto, "result/db_serialized.db".replace(".", "_" + version++ +"."));
    }

    public DbDataDtoV1 getDb() {
        try {
            return objectMapper.reader().readValue("result/db_serialized_" + new Integer(version-1).toString() +".db");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DbDataDtoV1(new HashMap<>());
    }

    private static List<Pair<String,String>> loadCloneFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(List.class, new ListOfPairDeserializer());

        objectMapper.registerModule(simpleModule);

        try {
            return objectMapper.reader().forType(new TypeReference<List<Pair>>() {
            }).readValue(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Could not read file");
    }

    private void writeObject(Object object, String fileName) {
        try {
            FileWriter fos = new FileWriter(new File(fileName));
            BufferedWriter oos = new BufferedWriter(fos);
            oos.write(objectMapper.writer().writeValueAsString(object));
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ListOfPairDeserializer extends StdDeserializer<List<Pair<String, String>>> {

    public ListOfPairDeserializer(){
        this(null);
    }

    protected ListOfPairDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Pair<String, String>> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<Pair<String, String>> myList = new ArrayList<>();
        JsonNode node = jp.getCodec().readTree(jp);

        for (int i = 0; i < node.size(); i++){
            myList.add(Pair.of(node.get(i).fields().next().getKey(), node.get(i).fields().next().getValue().textValue()));
        }

        return myList;
    }
}
