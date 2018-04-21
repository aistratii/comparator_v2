package comparatorapi.api;

import comparatorapi.constants.OperationsUrlV1;
import comparatorapi.dto.ImageDtoV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ComparatorOperationsV1 {

    @Autowired
    private RestTemplate restTemplate;

    boolean isContained(ImageDtoV1 imageDtoV1){
        return restTemplate.postForEntity(OperationsUrlV1.isContainedUrl, imageDtoV1, Boolean.class).getBody();
    }

}
