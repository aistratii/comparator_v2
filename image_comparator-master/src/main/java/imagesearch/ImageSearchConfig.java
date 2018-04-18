package imagesearch;

import imagesearch.comparator.AnalysisService;
import imagesearch.comparator.ComparatorCache;
import imagesearch.comparator.ComparatorService;
import imagesearch.comparator.ImageComparator;
import imagesearch.persistance.Dao;
import imagesearch.persistance.DataBaseService;
import imagesearch.persistance.SimpleDbFileDao;
import imagesearch.source.SourceImageFactory;
import imagesearch.source.TargetImageSourceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySources({
        @PropertySource("classpath:image-comparator.properties"),
        @PropertySource("classpath:simple-db-file-dao.properties")
})
public class ImageSearchConfig {

    @Bean
    public MainService mainService(){
        return new MainService();
    }

    @Bean
    public ReportService reportService(){
        return new ReportService("D:\\tmp\\reports");
    }

    @Bean
    public SourceImageFactory sourceImageFactory(){
        return new SourceImageFactory();
    }

    @Bean
    public TargetImageSourceService targetImageSourceService (){
        return new TargetImageSourceService();
    }

    @Bean
    public ComparatorService comparatorService(){
        return new ComparatorService();
    }

    @Scope("prototype")
    @Bean
    public ImageComparator imageComparator(ComparatorCache comparatorCache,
                                           @Value("${threadpool_size}") int threadpoolSize,
                                           @Value("${tolerance_threshold_rgb}") int toleranceThresholdRgb,
                                           @Value("${tolerance_threshold_percent}") int toleranceThresholdPercent
                                           ){
        return new ImageComparator(comparatorCache, threadpoolSize, toleranceThresholdRgb, toleranceThresholdPercent);
    }

    @Scope("prototype")
    @Bean
    public ComparatorCache comparatorCache(){
        return new ComparatorCache();
    }

    @Bean
    public Dao dao(@Value("${db-model-file-name}") String dbModelFileName){
        return new SimpleDbFileDao(dbModelFileName);
    }

    @Bean
    public DataBaseService dataBaseService(){
        return new DataBaseService();
    }

    @Bean
    public AnalysisService analysisService(){
        return new AnalysisService();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
