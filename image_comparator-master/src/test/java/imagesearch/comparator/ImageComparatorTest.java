package imagesearch.comparator;

import imagesearch.image.CustomImageType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageComparatorTest {

    @Spy
    private ComparatorCache cache = spy(new ComparatorCache());

    @Spy
    private ImageComparator imageComparator = new ImageComparator(cache, 1, 1, 1);

    @Test
    public void compareTestSuccess() throws IOException {
        final String imagePath = ImageComparatorTest.class.getClassLoader().getResource("img1.jpg").getFile().toString();

        final BufferedImage image1 = ImageIO.read(new File(imagePath));
        final CustomImageType sourceImage = new CustomImageType(image1.getWidth(), image1.getHeight(), 1);
        final CustomImageType targetImage = new CustomImageType(image1.getWidth(), image1.getHeight(), 1);

        sourceImage.createGraphics().drawImage(image1, 0, 0, null);
        targetImage.createGraphics().drawImage(image1, 0, 0, null);

        doReturn(Pair.of(sourceImage, targetImage)).when(imageComparator).matchSize(sourceImage, targetImage);

        assertTrue(imageComparator.compare(sourceImage, targetImage));

        verify(cache).setBuffer(sourceImage, targetImage);
    }

    @Test
    public void compareTestFail() throws IOException {
        final String imagePath1 = ImageComparatorTest.class.getClassLoader().getResource("img1.jpg").getFile().toString();
        final String imagePath2 = ImageComparatorTest.class.getClassLoader().getResource("img2.jpg").getFile().toString();

        final BufferedImage image1 = ImageIO.read(new File(imagePath1));
        final BufferedImage image2 = ImageIO.read(new File(imagePath2));
        final CustomImageType sourceImage = new CustomImageType(image1.getWidth(), image1.getHeight(), 1);
        final CustomImageType targetImage = new CustomImageType(image2.getWidth(), image2.getHeight(), 1);

        sourceImage.createGraphics().drawImage(image1, 0, 0, null);
        targetImage.createGraphics().drawImage(image2, 0, 0, null);

        doReturn(Pair.of(sourceImage, targetImage)).when(imageComparator).matchSize(sourceImage, targetImage);

        assertFalse(imageComparator.compare(sourceImage, targetImage));

        verify(cache).setBuffer(sourceImage, targetImage);
    }

    @Test
    public void matchSizeTest() throws IOException {
        final String imagePath1 = ImageComparatorTest.class.getClassLoader().getResource("img1.jpg").getFile().toString();
        final String imagePath2 = ImageComparatorTest.class.getClassLoader().getResource("img2.jpg").getFile().toString();

        final BufferedImage image1 = ImageIO.read(new File(imagePath1));
        final BufferedImage image2 = ImageIO.read(new File(imagePath2));
        final CustomImageType sourceImage = new CustomImageType(image1.getWidth(), image1.getHeight(), 1);
        final CustomImageType targetImage = new CustomImageType(image2.getWidth(), image2.getHeight(), 1);

        sourceImage.createGraphics().drawImage(image1, 0, 0, null);
        targetImage.createGraphics().drawImage(image2, 0, 0, null);

        assertNotEquals(sourceImage.getWidth(), targetImage.getWidth());
        assertEquals(480, targetImage.getHeight());

        final Pair<CustomImageType, CustomImageType> pair = imageComparator.matchSize(sourceImage, targetImage);

        assertEquals(pair.getLeft().getWidth(), pair.getRight().getWidth());
        assertEquals(600, pair.getRight().getHeight());
    }

}
