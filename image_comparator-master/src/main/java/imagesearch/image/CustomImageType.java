package imagesearch.image;

import java.awt.image.BufferedImage;

public class CustomImageType extends BufferedImage {

    private int rgb = 0;
    private String location;

    public CustomImageType(int width, int height, int imageType) {
        super(width, height, imageType);
    }

    public int getRgb() {
        int red = (getRGB(0, 0) >> 16) & 0xFF,
                green = (getRGB(0, 0) >> 8) & 0xFF,
                blue = (getRGB(0, 0) >> 0) & 0xFF;

        for (int i = 0; i < getWidth(); i++)
            for (int j = 0; j < getHeight(); j++){
                red += (getRGB(i, j) >> 16) & 0xFF;
                red /= 2;

                green += (getRGB(0, 0) >> 8) & 0xFF;
                green /= 2;

                blue = (getRGB(0, 0) >> 0) & 0xFF;
                blue /= 2;
            }

        this.rgb = (red << 16) | (green << 8) | blue;


        return rgb;
    }

    public String getImageLocation() {
        return location;
    }

    public void setImageLocation(String location){
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomImageType that = (CustomImageType) o;

        if (rgb != that.rgb) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;

        return true;
    }

}
