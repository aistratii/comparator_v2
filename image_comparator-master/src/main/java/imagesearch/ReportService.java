package imagesearch;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.util.List;

@Component
public class ReportService {
    private final String reportDirectory;
    private int counter = 0;
    private ObjectMapper objectMapper;

    public ReportService(String reportDirectory){
        this.reportDirectory = reportDirectory;
        this.objectMapper = new ObjectMapper();
    }

    synchronized public void persistReport(Report report) {
        try {
            objectMapper.writeValue(new File(reportDirectory + File.separator + "report_" + counter++ + ".txt"), report);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
