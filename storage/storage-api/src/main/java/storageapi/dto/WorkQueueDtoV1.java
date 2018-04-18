package storageapi.dto;

import java.util.ArrayList;
import java.util.List;

public class WorkQueueDtoV1 {
    List<String> fileList;

    public WorkQueueDtoV1(){
        fileList = new ArrayList<String>();
    }

    public List<String> getFileList(){
        return fileList;
    }
}
