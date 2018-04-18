package imagesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import imagesearch.persistance.model.AllDbModel;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MainTest {

    @Mock
    private MainService mainService;

    @Test
    @Ignore
    public void testMain(){
        final String sources[] = {"source1", "source2"};
        Main.main(sources);
        verify(mainService).work(sources);
    }

}
