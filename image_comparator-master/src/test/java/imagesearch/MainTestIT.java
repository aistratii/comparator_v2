package imagesearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import imagesearch.persistance.model.AllDbModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MainTestIT {

    private String dbFileNameLocation = "db.db";

    @Test
    public void testMainSimpleDb() throws InterruptedException, IOException {
        //WHEN running ap with "--load" parameter with this property "MainTest.class.getClassLoader().getResource("").getFile().toString()"
        String args[] = {"--load", MainTest.class.getClassLoader().getResource("imgs").getFile().toString().substring(1).replace("/", "\\") +"\\"};
        Main.main(args);

        Thread.sleep(8000);
        ObjectMapper objectMapper = new ObjectMapper();
        File dbFile = new File(dbFileNameLocation);
        AllDbModel allDbModel = objectMapper.readValue(dbFile, AllDbModel.class);
        assertTrue(dbFile.delete());
        assertTrue(allDbModel.getRgbDbModel().get(0).size() == 2);
    }
}
