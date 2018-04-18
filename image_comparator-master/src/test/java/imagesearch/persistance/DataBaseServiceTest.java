package imagesearch.persistance;

import imagesearch.ImageSearchConfig;
import imagesearch.comparator.ImageComparatorTest;
import imagesearch.image.CustomImageType;
import imagesearch.persistance.model.AllDbModel;
import imagesearch.source.SourceImageFactory;
import imagesearch.source.SourceImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Import(ImageSearchConfig.class)
public class DataBaseServiceTest {

    @Mock
    private Dao dao;

    @Mock
    private SourceImageFactory sourceImageFactory;

    @Spy
    @InjectMocks
    private DataBaseService dataBaseService;

    @Test
    public void getByRgbTest(){
        List<String> expectedFilenames = asList("file1.jpg", "file2.jpg");
        int rgb = 101;

        when(dao.getForRgb(rgb)).thenReturn(expectedFilenames);

        List<String> actualFileNames = dataBaseService.getImagesWithRgb(rgb);

        assertEquals(expectedFilenames, actualFileNames);
    }

    @Test
    public void updateDbTest() throws IOException {
        String url = DataBaseServiceTest.class.getClassLoader().getResource("").toString();
        SourceImageService sourceImageService = mock(SourceImageService.class);

        final String imagePath1 = DataBaseServiceTest.class.getClassLoader().getResource("img1.jpg").getFile().toString();
        final String imagePath2 = ImageComparatorTest.class.getClassLoader().getResource("img2.jpg").getFile().toString();
        final BufferedImage image1 = ImageIO.read(new File(imagePath1));
        final BufferedImage image2 = ImageIO.read(new File(imagePath2));
        final CustomImageType customImg1 = new CustomImageType(image1.getWidth(), image1.getHeight(), 1);
        final CustomImageType customImg2 = new CustomImageType(image2.getWidth(), image2.getHeight(), 1);
        customImg1.createGraphics().drawImage(image1, 0, 0, null);
        customImg2.createGraphics().drawImage(image2, 0, 0, null);

        when(sourceImageFactory.getSource(url)).thenReturn(sourceImageService);
        when(sourceImageService.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(sourceImageService.getNext()).thenReturn(customImg1).thenReturn(customImg2);

        dataBaseService.updateDb(url);

        verify(dao, atLeastOnce()).update(customImg1);
        verify(dao, atLeastOnce()).update(customImg2);
        verify(dao).commit();
    }

}
