package imagesearch;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import imagesearch.decisionqueue.DecisionQueueWindow;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main4ImageCloneDecisionQueue {
    public static void main(String args[]){
        List<Pair<String, String>> possibleClones = loadCloneFile("C:\\Users\\Alex\\Desktop\\image_comparator-master\\result\\clones_serialized_17894(eager).db");

        new DecisionQueueWindow(possibleClones, "result/decision_queue.db"); //stopped at 308
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
}

class ListOfPairDeserializer extends StdDeserializer<List<Pair<String, String>>>{

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
