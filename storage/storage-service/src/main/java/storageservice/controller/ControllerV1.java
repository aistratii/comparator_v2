package storageservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import storageapi.config.OperationsUrlV1;
import storageapi.dto.DbDataDtoV1;
import storageapi.dto.WorkQueueDtoV1;
import storageservice.service.ServiceV1;

@Controller
public class ControllerV1 {

    @Autowired
    private OperationsUrlV1 urls;

    @Autowired
    private ServiceV1 serviceV1;

    @PutMapping(value = urls.workQueue)
    public void addWorkQueueV1(WorkQueueDtoV1 dto){
        serviceV1.addWork(dto);
    }

    @PostMapping(value = urls.workQueue)
    public void saveWorkQueueV1(WorkQueueDtoV1 dto){
        serviceV1.saveWork(dto);
    }

    @GetMapping(value = urls.workQueue)
    public WorkQueueDtoV1 getWorkQueue(){
        return serviceV1.getWorkQueue();
    }

    @PutMapping(value = urls.db)
    public void addDbDataV1(DbDataDtoV1 dto){
        serviceV1.addDb(dto);
    }

    @PostMapping(value = urls.db)
    public void saveDbDataV1(DbDataDtoV1 dto){
        serviceV1.saveDb(dto);
    }

    @GetMapping(value = urls.db)
    public DbDataDtoV1 getDbData(){
        return serviceV1.getDb();
    }

}
