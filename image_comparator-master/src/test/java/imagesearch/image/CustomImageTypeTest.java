package imagesearch.image;

import imagesearch.testutils.TestUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CustomImageTypeTest {

    @Test
    public void testGetRgb() throws IOException {
        CustomImageType img1 = TestUtil.readCustomImageType("img1.jpg");
        CustomImageType img2 = TestUtil.readCustomImageType("img1.jpg");
        CustomImageType img3 = TestUtil.readCustomImageType("img2.jpg");

        assertEquals(img1.getRgb(), img2.getRgb());
        assertNotEquals(img1.getRgb(), img3.getRgb());
    }
}
