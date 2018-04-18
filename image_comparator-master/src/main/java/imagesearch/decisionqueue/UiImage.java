package imagesearch.decisionqueue;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class UiImage extends JPanel {
    private final static BufferedImage DUMMY_BUFFERED_IMAGE;

    static {
        DUMMY_BUFFERED_IMAGE = new BufferedImage(500, 500, 1);
        DUMMY_BUFFERED_IMAGE.createGraphics().drawString("DUMMY_IMAGE", 250, 250);
    }

    //public static final UiImage DEFAULT = new UiImage(DUMMY_BUFFERED_IMAGE, "DUMMY_FILE_NAME");

    private BufferedImage image;
    private String fileName;
    private float ratio;

    private UiImage(BufferedImage image, String fileName) {
        this.image = image;
        this.fileName = fileName;
        this.ratio = (float)image.getWidth() / (float)image.getHeight();
        this.setPreferredSize(new Dimension(500, 500));
    }

    public UiImage(String fileName) {
        BufferedImage tmpImage = null;
        try {
            tmpImage = ImageIO.read(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.image = tmpImage;
        this.fileName = fileName;
        this.ratio = image == null ? 0 : (float)image.getWidth() / (float)image.getHeight();
        this.setPreferredSize(new Dimension(500, 500));
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, getScaledWidth(), getScaledHeight(), null);
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getFileName() {
        return fileName;
    }

    public int getScaledWidth() {
        //return (int)(ratio * thisMin());
        return (int) ((500-200)*ratio);
    }

    public int getScaledHeight() {
        //return (int)(thisMin() / ratio);
        return (int) ((500-200)/ratio);
    }

    private int thisMin(){
        return min(this.getWidth(), this.getHeight());
    }


    public static UiImage dummy() {
        return new UiImage(DUMMY_BUFFERED_IMAGE, "DUMMY_FILE_NAME");
    }

    public void updateImage(String fileName) {
        BufferedImage tmpImage = null;
        try {
            tmpImage = ImageIO.read(new File(fileName));
            this.image = tmpImage;
            //this.image.getGraphics().drawString(fileName, 100, 100);
            this.fileName = fileName;
            this.ratio = (float)image.getWidth() / (float)image.getHeight();
            this.setPreferredSize(new Dimension(500, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (tmpImage == null) {
            this.image = DUMMY_BUFFERED_IMAGE;
            this.fileName = "DUMMY NAME";
        }
    }
}
