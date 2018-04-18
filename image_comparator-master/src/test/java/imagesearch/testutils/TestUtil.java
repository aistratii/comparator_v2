package imagesearch.testutils;

import imagesearch.image.CustomImageType;
import imagesearch.image.CustomImageTypeTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestUtil {
    public static CustomImageType readCustomImageType(String fileName) {
        try {

            final String fullFileName = CustomImageTypeTest.class.getClassLoader().getResource(fileName).getFile();

            final BufferedImage image = ImageIO.read(new File(fullFileName));
            final CustomImageType customImage = new CustomImageType(image.getWidth(), image.getHeight(), 1);
            customImage.createGraphics().drawImage(image, 0, 0, null);

            return customImage;
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new NullPointerException();
    }
}
