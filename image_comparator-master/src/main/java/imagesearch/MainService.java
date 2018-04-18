package imagesearch;

import imagesearch.comparator.AnalysisService;
import imagesearch.comparator.ComparatorService;
import imagesearch.persistance.DataBaseService;
import imagesearch.source.SourceImageFactory;
import imagesearch.source.SourceImageService;
import imagesearch.source.TargetImageSourceService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Optional.of;

@Component
public class MainService{

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private AnalysisService analysisService;

    public void work(final String args[]){
        Map<String, List<String>> reducedParams = reduceParams(args);

        for (Map.Entry<String, List<String>> entry: reducedParams.entrySet()){
            if (entry.getKey().equals("--load"))
                entry.getValue().forEach(path -> dataBaseService.updateDb(path));
            if(entry.getKey().equals("--analyze"))
                analysisService.analyze(entry.getValue());
        }
    }

    protected Map<String, List<String>> reduceParams(String[] args) {
        Map<String, List<String>> paramMetaData = new HashMap<>();

        for (int i = 0; i < args.length; i++){
            if (args[i].startsWith("--")) {
                String paramKeyWord = args[i];

                i++;
                String values[] = args[i].split(",");

                paramMetaData.put(paramKeyWord, new ArrayList<>());

                stream(values)
                        .forEach(value -> paramMetaData.get(paramKeyWord).add(value));
            }
        }

        return paramMetaData;
    }
}
