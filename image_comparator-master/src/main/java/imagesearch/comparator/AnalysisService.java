package imagesearch.comparator;

import imagesearch.source.SourceImageFactory;
import imagesearch.source.SourceImageService;
import imagesearch.source.TargetImageSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class AnalysisService {

    @Autowired
    private SourceImageFactory sourceImageFactory;

    @Autowired
    private ComparatorService comparatorService;

    @Autowired
    private TargetImageSourceService targetImageSourceService;

    public void analyze(List<String> sources) {

        List<SourceImageService> sourceServices =
                sources.stream()
                        .map(source -> sourceImageFactory.getSource(source))
                        .collect(Collectors.toList());


        sourceServices
                .parallelStream()
                .forEach(srcSvc -> comparatorService.compare(srcSvc, targetImageSourceService));
    }
}
