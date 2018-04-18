package imagesearch.comparator;

import imagesearch.Report;
import imagesearch.ReportService;
import imagesearch.source.ImageStream;
import imagesearch.source.TargetImageSourceService;
import imagesearch.image.CustomImageType;
import imagesearch.source.SourceImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComparatorServiceTest {

    @InjectMocks
    private ComparatorService comparatorService;

    @Mock
    private SourceImageService sourceService;

    @Mock
    private TargetImageSourceService targetService;

    @Mock
    private ReportService reportService;

    @Mock
    private ImageComparator imageComparator;

    @Test
    public void compareTestMatchFound(){
        final Report report = new Report("[possibly match] source: source location, target: target location");
        final CustomImageType sourceImage = mock(CustomImageType.class);
        final CustomImageType targetImage = mock(CustomImageType.class);
        final ImageStream localImageStream = mock(ImageStream.class);

        when(sourceImage.getRgb()).thenReturn(100);

        when(sourceService.getNext()).thenReturn(sourceImage);
        when(sourceService.hasNext()).thenReturn(true).thenReturn(false);
        when(targetService.getStreamForRgb(eq(100))).thenReturn(localImageStream);

        when(localImageStream.hasNext()).thenReturn(true).thenReturn(false);
        when(localImageStream.getNext()).thenReturn(targetImage);

        when(sourceImage.getImageLocation()).thenReturn("source location");
        when(targetImage.getImageLocation()).thenReturn("target location");

        when(imageComparator.compare(sourceImage, targetImage)).thenReturn(true);

        comparatorService.compare(sourceService, targetService);

        verify(reportService).persistReport(report);
    }

    @Test
    public void compareTestMatchNotFound(){
        final CustomImageType sourceImage = mock(CustomImageType.class);
        final CustomImageType targetImage = mock(CustomImageType.class);
        final ImageStream localImageStream = mock(ImageStream.class);

        when(sourceImage.getRgb()).thenReturn(100);

        when(sourceService.getNext()).thenReturn(sourceImage);
        when(sourceService.hasNext()).thenReturn(true).thenReturn(false);
        when(targetService.getStreamForRgb(eq(100))).thenReturn(localImageStream);

        when(localImageStream.hasNext()).thenReturn(true).thenReturn(false);
        when(localImageStream.getNext()).thenReturn(targetImage);

        when(imageComparator.compare(sourceImage, targetImage)).thenReturn(false);

        comparatorService.compare(sourceService, targetService);

        verify(reportService, never()).persistReport(any(Report.class));
    }

}
