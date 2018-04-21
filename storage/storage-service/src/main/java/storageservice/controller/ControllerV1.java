package storageservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import storageapi.constants.OperationsUrlV1;
import storageapi.dto.DbDataDtoV1;
import storageapi.dto.WorkQueueDtoV1;
import storageservice.service.ServiceV1;

@Controller
public class ControllerV1 {

    private final ServiceV1 serviceV1;

    public ControllerV1(ServiceV1 serviceV1){
        this.serviceV1 = serviceV1;
    }

    @PutMapping(value = OperationsUrlV1.workQueue)
    public void addWorkQueueV1(WorkQueueDtoV1 dto){
        serviceV1.addWork(dto);
    }

    @PostMapping(value = OperationsUrlV1.workQueue)
    public void saveWorkQueueV1(WorkQueueDtoV1 dto){
        serviceV1.saveWork(dto);
    }

    @GetMapping(value = OperationsUrlV1.workQueue)
    public WorkQueueDtoV1 getWorkQueue(){
        return serviceV1.getWorkQueue();
    }

    @PutMapping(value = OperationsUrlV1.db)
    public void addDbDataV1(DbDataDtoV1 dto){
        serviceV1.addDb(dto);
    }

    @PostMapping(value = OperationsUrlV1.db)
    public void saveDbDataV1(DbDataDtoV1 dto){
        serviceV1.saveDb(dto);
    }

    @GetMapping(value = OperationsUrlV1.db)
    public DbDataDtoV1 getDbData(){
        return serviceV1.getDb();
    }

}
