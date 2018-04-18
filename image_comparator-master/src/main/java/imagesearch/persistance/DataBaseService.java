package imagesearch.persistance;

import imagesearch.source.SourceImageFactory;
import imagesearch.source.SourceImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.transform.Source;
import java.io.File;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class DataBaseService {

    @Autowired
    private Dao dao;

    @Autowired
    private SourceImageFactory sourceImageFactory;

    public List<String> getImagesWithRgb(int averageRgb) {
        return dao.getForRgb(averageRgb);
    }

    public void updateDb(String URL){
        SourceImageService sourceService= sourceImageFactory.getSource(URL);

        while(sourceService.hasNext()){
            dao.update(sourceService.getNext());
        }

        dao.commit();
    }
}
