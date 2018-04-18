package imagesearch.source;

import imagesearch.persistance.DataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
/**
 * Gateway for all knowledge of the system regarding the known images,
 * a facade for many data sources, be it local and/or remote.
 */
public class TargetImageSourceService {

    @Autowired
    private DataBaseService dbService;


    //TODO: Make this API call more universal, to also include calls from some remote DB's
    public ImageStream getStreamForRgb(int averageRgb) {
        return new WindowsFileLocation(dbService.getImagesWithRgb(averageRgb));
    }
}
