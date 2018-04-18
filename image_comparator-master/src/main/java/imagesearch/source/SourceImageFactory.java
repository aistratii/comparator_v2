package imagesearch.source;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SourceImageFactory {

    public SourceImageService getSource(String source) {
        final boolean windowsFileLocation = Pattern.compile("[a-zA-Z]:\\\\.*").matcher(source).matches() ||
                Pattern.compile("[a-zA-Z]:\\\\").matcher(source).matches() ||
                Pattern.compile("[a-zA-Z]").matcher(source).matches();

        if (windowsFileLocation)
            return new WindowsFileLocation(source);
        else
            throw new SourceNotFoundException("Could not find source for: " + source);
    }

}
