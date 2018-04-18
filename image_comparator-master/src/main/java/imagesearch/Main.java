package imagesearch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    //example to run:

    public static void main(String args[]){
        ApplicationContext appctx = new AnnotationConfigApplicationContext(ImageSearchConfig.class);
        final MainService mainService = (MainService) appctx.getBean("mainService");
        mainService.work(args);
    }

}