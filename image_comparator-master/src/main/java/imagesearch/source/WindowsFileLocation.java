package imagesearch.source;

import imagesearch.image.CustomImageType;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static java.util.Arrays.stream;

public class WindowsFileLocation implements SourceImageService, ImageStream {
    private Stack<String> fileList;

    public WindowsFileLocation(String source) {
        fileList = new Stack<>();

        File file = new File(source);
        if (file.isDirectory())
            stream(file.list())
                    .forEach(fileName -> fileList.push(source + File.separator + fileName));
        else
            fileList.push(source);
    }

    public WindowsFileLocation(List<String> fileLocations){
        fileList = new Stack<>();

        fileLocations.
                stream()
                .forEach(fileList::push);
    }

    @Override
    public CustomImageType getNext() {
        try {
            String fileName = fileList.pop();
            BufferedImage bufferedImage = ImageIO.read(new File(fileName));
            CustomImageType customImageType = new CustomImageType(bufferedImage.getWidth(), bufferedImage.getHeight(), 1);
            customImageType.getGraphics().drawImage(customImageType, 0, 0, null);
            customImageType.setImageLocation(fileName);

            return customImageType;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean hasNext() {
        return !fileList.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WindowsFileLocation that = (WindowsFileLocation) o;

        return fileList != null ? fileList.equals(that.fileList) : that.fileList == null;

    }

    @Override
    public int hashCode() {
        return fileList != null ? fileList.hashCode() : 0;
    }
}
