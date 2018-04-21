package comparatorservice.service;

import comparatorapi.dto.ImageDtoV1;
import org.springframework.stereotype.Service;

@Service
public class ServiceV1 {

    public boolean isContained(ImageDtoV1 imageDtoV1) {
        return true;
    }

}
