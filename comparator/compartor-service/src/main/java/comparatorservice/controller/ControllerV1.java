package comparatorservice.controller;


import comparatorapi.constants.OperationsUrlV1;
import comparatorapi.dto.ImageDtoV1;
import comparatorservice.service.ServiceV1;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControllerV1 {

    private ServiceV1 serviceV1;

    @PostMapping(value = OperationsUrlV1.isContainedUrl)
    public boolean isContained(ImageDtoV1 imageDtoV1){
        return serviceV1.isContained(imageDtoV1);
    }

}
