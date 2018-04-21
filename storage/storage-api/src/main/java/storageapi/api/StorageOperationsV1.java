package storageapi.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import storageapi.constants.OperationsUrlV1;
import storageapi.dto.DbDataDtoV1;
import storageapi.dto.WorkQueueDtoV1;

@Component
public class StorageOperationsV1 {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * save-work-queue
     */
    public void addWorkQueueV1(WorkQueueDtoV1 dto){
        restTemplate.put(OperationsUrlV1.workQueue, dto);
    }

    public void saveWorkQueueV1(WorkQueueDtoV1 dto){
        restTemplate.postForEntity(OperationsUrlV1.workQueue, dto, Void.class);
    }

    //get-work-queue
    public WorkQueueDtoV1 getWorkQueueV1(){
        return restTemplate.getForEntity(OperationsUrlV1.workQueue, WorkQueueDtoV1.class).getBody();
    }

    //save-db-data
    public void addDbDataV1(DbDataDtoV1 dto){
        restTemplate.put(OperationsUrlV1.db, dto);
    }

    public void saveDbDataV1(DbDataDtoV1 dto){
        restTemplate.postForEntity(OperationsUrlV1.db, dto, Void.class);
    }

    //get-db-data
    public DbDataDtoV1 getDbDataV1(){
        return restTemplate.getForEntity(OperationsUrlV1.db, DbDataDtoV1.class).getBody();
    }

}
