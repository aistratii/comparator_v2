package imagesearch.comparator;

import imagesearch.Report;
import imagesearch.ReportService;
import imagesearch.source.ImageStream;
import imagesearch.source.TargetImageSourceService;
import imagesearch.image.CustomImageType;
import imagesearch.source.SourceImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComparatorService {

    @Autowired
    private ImageComparator imageComparator;

    @Autowired
    private ReportService reportService;

    public void compare(SourceImageService source, TargetImageSourceService target) {
        while(source.hasNext()) {
            CustomImageType sourceImage = source.getNext();

            ImageStream localImageStream = target.getStreamForRgb(sourceImage.getRgb());

            while (localImageStream.hasNext()) {
                CustomImageType targetImage = localImageStream.getNext();

                if (imageComparator.compare(sourceImage, targetImage)) {
                    reportService.persistReport(new Report(sourceImage, targetImage));
                    break;
                }
            }
        }
    }

}
