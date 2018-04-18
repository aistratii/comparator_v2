package imagesearch.source;

import imagesearch.persistance.DataBaseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URISyntaxException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class TargetImageSourceServiceTest {

    @Mock
    private DataBaseService dbService;

    @Spy
    @InjectMocks
    private TargetImageSourceService svc = spy(new TargetImageSourceService());

    @Test
    public void getStreamForRgbTest() throws URISyntaxException {
        int rgb = 10;
        List<String> fileNames = asList(TargetImageSourceServiceTest.class.getClassLoader().getResource("img1.jpg").toURI().toString());

        ImageStream localImageStream = new WindowsFileLocation(fileNames);

        when(dbService.getImagesWithRgb(rgb)).thenReturn(fileNames);

        assertEquals(localImageStream, svc.getStreamForRgb(rgb));
    }
}
