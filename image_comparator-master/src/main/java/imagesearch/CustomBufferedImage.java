package imagesearch;

import imagesearch.cache.LRU;
import org.apache.commons.lang3.tuple.Pair;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CustomBufferedImage extends BufferedImage {
    private int averageRgb = 0;
    private boolean isRgbInited = false;

    public CustomBufferedImage(int width, int height, int imageType) {
        super(width, height, imageType);
    }

//    public void setAverageRgb(int averagRgb){
//        this.isRgbInited = true;
//        this.averageRgb = averagRgb;
//    }

    public int averageRgb() {
        if (isRgbInited)
            return averageRgb;
        else {
            int rgbCache = getRGB(0, 0);
            int redCounter = (rgbCache >> 16) & 0xFF,
                greenCounter = (rgbCache >> 8) & 0xFF,
                blueCounter = (rgbCache >> 0) & 0xFF;

            for (int i = 1; i < getWidth(); i++)
                for (int j = 0; j < getHeight(); j++) {
                    rgbCache = this.getRGB(i, j);

                    redCounter = (redCounter + (rgbCache >> 16) & 0xFF) / 2;
                    greenCounter = (greenCounter + (rgbCache >> 8) & 0xFF) / 2;
                    blueCounter = (blueCounter + (rgbCache >> 0) & 0xFF) / 2;
                }

            isRgbInited = true;
            averageRgb = ((redCounter & 0xFF) << 16) |
                        ((greenCounter & 0xFF) << 8)  |
                        ((blueCounter & 0xFF) << 0);
            return averageRgb;
        }
    }

    public static final class CustomBufferedImageReader {

        private static LRU<String, CustomBufferedImage> cache = new LRU<>();

        public static CustomBufferedImage readImage(String fileName){
//            try {
//                if(cache.exists(fileName))
//                    return cache.get(fileName);
//                else {
//                    BufferedImage bufferedImage = ImageIO.read(new File(fileName));
//                    CustomBufferedImage customBufferedImage = new CustomBufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
//                    customBufferedImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
//                    cache.push(fileName, customBufferedImage);
//                    return customBufferedImage;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
            return readImagePrivate(fileName);
        }

        private static CustomBufferedImage readImagePrivate(String fileName){
            try {
                BufferedImage bufferedImage = ImageIO.read(new File(fileName));
                CustomBufferedImage customBufferedImage = new CustomBufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
                customBufferedImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
                return customBufferedImage;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

//    public static CustomBufferedImage readImage(String fileName){
//        try {
//            BufferedImage bufferedImage = ImageIO.read(new File(fileName));
//            CustomBufferedImage customBufferedImage = new CustomBufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
//            customBufferedImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
//            return customBufferedImage;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    /**
     *
     * @param that
     * @return true means it is the same, false means it's not the same
     */
    public boolean compareTo(CustomBufferedImage that) {
        Pair<CustomBufferedImage,CustomBufferedImage> sizedImages = matchSize(this, that);

        CustomBufferedImage thisSized = sizedImages.getLeft();
        CustomBufferedImage thatSized = sizedImages.getRight();

        int minWidth = min(thisSized.getWidth(), thatSized.getWidth());
        int minHeight = min(thisSized.getHeight(), thatSized.getHeight());

        int numberOfFails = 0;
        int numberOfSuccesses = 0;
        boolean isDiffAcceptable;
        int thisRgbCache;
        int thatRgbCache;
        for (int i = 0; i < minWidth; i++)
            for (int j = 0; j < minHeight; j++){
                thisRgbCache = thisSized.getRGB(i, j);
                thatRgbCache = thatSized.getRGB(i, j);

                isDiffAcceptable =
                        ((thisRgbCache >> 16) & 0xFF) - ((thatRgbCache >> 16) & 0xFF) <= Main2ImageSearchSimple.ACCEPTABLE_RGB_CHANNEL_DIF &&
                        ((thisRgbCache >> 8) & 0xFF) - ((thatRgbCache >> 8) & 0xFF) <= Main2ImageSearchSimple.ACCEPTABLE_RGB_CHANNEL_DIF &&
                        ((thisRgbCache >> 0) & 0xFF) - ((thatRgbCache >> 0) & 0xFF) <= Main2ImageSearchSimple.ACCEPTABLE_RGB_CHANNEL_DIF;

                if (isDiffAcceptable)
                    numberOfSuccesses++;
                else
                    numberOfFails++;
        }

        numberOfFails += max(thisSized.getWidth() * thisSized.getHeight(), thatSized.getWidth()*thatSized.getHeight()) -
                min(thisSized.getWidth() * thisSized.getHeight(), thatSized.getWidth()*thatSized.getHeight());

        return numberOfFails * 100 / (numberOfFails + numberOfSuccesses) <= Main2ImageSearchSimple.ACCEPTABLE_PERCENTAGE_OF_MATCHING;

    }

    private static Pair<CustomBufferedImage, CustomBufferedImage> matchSize(CustomBufferedImage sourceImage, CustomBufferedImage targetImage) {
        //priroty by width:
        int width = max(sourceImage.getWidth(), targetImage.getWidth());

        CustomBufferedImage sizedSourceImage, sizedTargetImage;

        if (sourceImage.getWidth() < width){
            int newHeight = (int)(((float)targetImage.getHeight() / (float)targetImage.getWidth()) * width);
            sizedSourceImage = new CustomBufferedImage(width, newHeight, 1);

            sizedSourceImage
                    .getGraphics()
                    .drawImage(sourceImage, 0, 0, sizedSourceImage.getWidth(), sizedSourceImage.getHeight(), null);
        } else
            sizedSourceImage = sourceImage;


        if (targetImage.getWidth() < width){
            int newHeight = (int)(((float)targetImage.getHeight() / (float)targetImage.getWidth()) * width);
            sizedTargetImage = new CustomBufferedImage(width, newHeight, 1);

            sizedTargetImage
                    .getGraphics()
                    .drawImage(sourceImage, 0, 0, sizedTargetImage.getWidth(), sizedTargetImage.getHeight(), null);
        } else
            sizedTargetImage = sourceImage;

        //sizedSourceImage.setAverageRgb(sourceImage.averageRgb());
        //sizedTargetImage.setAverageRgb(targetImage.averageRgb());

        return Pair.of(sizedSourceImage, sizedTargetImage);
    }
}