package imagesearch.persistance;

import com.fasterxml.jackson.databind.ObjectMapper;
import imagesearch.image.CustomImageType;
import imagesearch.persistance.model.AllDbModel;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SimpleDbFileDao implements Dao {
    private AllDbModel allDbModel;
    private final String DB_MODEL_FILE_NAME;

    public SimpleDbFileDao(String dbModelFileName){
        this.DB_MODEL_FILE_NAME = dbModelFileName;
        readFile();
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    private void readFile() {
        try {
            File file = new File(DB_MODEL_FILE_NAME);
            if(file.exists()){
                allDbModel = objectMapper.readValue(file, AllDbModel.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if (allDbModel == null)
                allDbModel = new AllDbModel();
        }
    }

    @Override
    public void update(CustomImageType customImageType){
        if (allDbModel.getRgbDbModel().get(customImageType.getRgb()) == null)
            allDbModel.getRgbDbModel().put(customImageType.getRgb(), new ArrayList<>());

        allDbModel.getRgbDbModel().get(customImageType.getRgb()).add(customImageType.getImageLocation());
    }

    @Override
    public void commit() {
        try {
            objectMapper.writeValue(new File(DB_MODEL_FILE_NAME), allDbModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected AllDbModel getAllDbModel(){
        return allDbModel;
    }

    @Override
    public List<String> getForRgb(int averageRgb) {
        List<String> resultFileNames = allDbModel.getRgbDbModel().get(averageRgb);

        return resultFileNames == null ? new ArrayList<>() : resultFileNames;
    }
}
