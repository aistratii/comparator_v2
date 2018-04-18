package imagesearch;

import imagesearch.image.CustomImageType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ReportServiceTest {

    @Spy
    private ReportService reportService = new ReportService("D:\\tmp\\reports");

    @Test
    public void persistReportTest(){
        CustomImageType sourceImage = new CustomImageType(10, 10, 1);
        sourceImage.setImageLocation("location1");

        CustomImageType targetImage = new CustomImageType(20, 20, 1);
        targetImage.setImageLocation("location2");

        Report report = new Report(sourceImage, targetImage);

        reportService.persistReport(report);

        File reportFile = new File( "D:\\tmp\\reports" + File.separator + "report_0.txt");
        assertTrue(reportFile.exists());
        reportFile.deleteOnExit();
    }

}
