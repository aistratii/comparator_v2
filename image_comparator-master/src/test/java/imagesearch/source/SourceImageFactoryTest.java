package imagesearch.source;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SourceImageFactoryTest {

    @Spy
    private SourceImageFactory sourceImageFactory;

    @Test
    public void getSourceTestWindowsFileLocation1(){
        final String source = "D:\\someFileLocation";

        final SourceImageService returnedSource = sourceImageFactory.getSource(source);

        assertTrue(returnedSource instanceof WindowsFileLocation);
    }

    @Test
    public void getSourceTestWindowsFileLocation2(){
        final String source = "D:\\";

        final SourceImageService returnedSource = sourceImageFactory.getSource(source);

        assertTrue(returnedSource instanceof WindowsFileLocation);
    }

    @Test
    public void getSourceTestWindowsFileLocation3(){
        final String source = "D";

        final SourceImageService returnedSource = sourceImageFactory.getSource(source);

        assertTrue(returnedSource instanceof WindowsFileLocation);
    }

}
