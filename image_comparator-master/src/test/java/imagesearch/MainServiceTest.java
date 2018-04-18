package imagesearch;

import imagesearch.comparator.AnalysisService;
import imagesearch.comparator.ComparatorService;
import imagesearch.persistance.DataBaseService;
import imagesearch.source.SourceImageFactory;
import imagesearch.source.SourceImageService;
import imagesearch.source.TargetImageSourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MainServiceTest {


    @Mock
    private DataBaseService dbService;

    @Mock
    private AnalysisService analysisService;

    @InjectMocks
    @Spy
    private MainService mainService;


    @Test
    public void workTest(){
        Map<String, List<String>> expectedParams = new HashMap<>();
        String[] args = {"--load", "load url 1,load url 2", "--analyze", "analyze 1,analyze 2,analyze 3"};
        expectedParams.put("--load", asList("load url 1", "load url 2"));
        expectedParams.put("--analyze", asList("analyze 1", "analyze 2", "analyze 3"));

        doReturn(expectedParams).when(mainService).reduceParams(args);

        mainService.work(args);

        verify(dbService).updateDb("load url 1");
        verify(dbService).updateDb("load url 2");
        verify(analysisService).analyze(asList("analyze 1", "analyze 2", "analyze 3"));
    }


    @Test
    public void testParse(){
        Map<String, List<String>> expectedParams = new HashMap<>();
        expectedParams.put("--load", asList("d://users//load_dir1", "d://users//load_dir2"));
        expectedParams.put("--analyze", asList("d://users//analyze_dir"));

        String[] args = {"--load", "d://users//load_dir1,d://users//load_dir2", "--analyze", "d://users//analyze_dir"};

        Map<String, List<String>> actualParams = mainService.reduceParams(args);

        assertEquals(expectedParams, actualParams);
    }

}
