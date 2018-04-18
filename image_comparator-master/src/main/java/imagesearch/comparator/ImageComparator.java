package imagesearch.comparator;

import imagesearch.image.CustomImageType;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Component
public class ImageComparator {
    private final ExecutorService executorService;

    private final int THREADPOOL_SIZE;
    private final int TOLERANCE_THRESHOLD_RGB;
    private final int TOLERANCE_THRESHOLD_PERCENT;

    private final ComparatorCache comparatorCache;

    public ImageComparator(ComparatorCache comparatorCache,
                           int threadPoolSize,
                           int toleranceThresholdRgb,
                           int toleranceThresholdPercent) {
        this.THREADPOOL_SIZE = threadPoolSize;
        this.TOLERANCE_THRESHOLD_RGB = toleranceThresholdRgb;
        this.TOLERANCE_THRESHOLD_PERCENT = toleranceThresholdPercent;

        this.executorService = Executors.newFixedThreadPool(THREADPOOL_SIZE);
        this.comparatorCache = comparatorCache;
    }

    public boolean compare(CustomImageType sourceImage, CustomImageType targetImage) {
        Pair<CustomImageType, CustomImageType> pairOfResized = matchSize(sourceImage, targetImage);

        comparatorCache.setBuffer(pairOfResized.getLeft(), pairOfResized.getRight());

//        IntStream.of(THREADPOOL_SIZE).forEach(part -> {
//            executorService.execute(() ->{
//                for (int index = part * min(comparatorCache.getFirstSize(), comparatorCache.getSecondSize()) / THREADPOOL_SIZE;
//                        index < (part+1) * min(comparatorCache.getFirstSize(), comparatorCache.getSecondSize()) / THREADPOOL_SIZE;
//                        index++){
//
//                    int redDiff = ((comparatorCache.getFirstVector()[index] >> 16) & 0xFF) - ((comparatorCache.getSecondVector()[index] >> 16) & 0xFF);
//                    int greenDiff = ((comparatorCache.getFirstVector()[index] >> 8) & 0xFF) - ((comparatorCache.getSecondVector()[index] >> 8) & 0xFF);
//                    int blueDiff = ((comparatorCache.getFirstVector()[index] >> 0) & 0xFF) - ((comparatorCache.getSecondVector()[index] >> 0) & 0xFF);
//
//                    boolean diffIsAcceptable = (redDiff >= 0 && redDiff <= TOLERANCE_THRESHOLD_RGB)
//                            && (greenDiff >= 0 && greenDiff <= TOLERANCE_THRESHOLD_RGB)
//                            && (blueDiff >= 0 && blueDiff <= TOLERANCE_THRESHOLD_RGB);
//
//                    if (diffIsAcceptable)
//                        comparatorCache.incrementSuccesses();
//                    else
//                        comparatorCache.incrementFails();
//                }
//            });
//        });

        final int firstVector[]  = comparatorCache.getFirstVector();
        final int secondVector[]  = comparatorCache.getSecondVector();
        int size = min(comparatorCache.getFirstSize(), comparatorCache.getSecondSize());

        for (int index = 0; index < size; index++){
            int redDiff = ((firstVector[index] >> 16) & 0xFF) - ((secondVector[index] >> 16) & 0xFF);
            int greenDiff = ((firstVector[index] >> 8) & 0xFF) - ((secondVector[index] >> 8) & 0xFF);
            int blueDiff = ((firstVector[index] >> 0) & 0xFF) - ((secondVector[index] >> 0) & 0xFF);

            boolean diffIsAcceptable = (redDiff >= 0 && redDiff <= TOLERANCE_THRESHOLD_RGB)
                    && (greenDiff >= 0 && greenDiff <= TOLERANCE_THRESHOLD_RGB)
                    && (blueDiff >= 0 && blueDiff <= TOLERANCE_THRESHOLD_RGB);

            if (diffIsAcceptable)
                comparatorCache.incrementSuccesses();
            else
                comparatorCache.incrementFails();
        }

        try {
            executorService.awaitTermination(20l, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new ComparationException("Something went wrong while waiting for the executor to finish");
        }

        int nrOfIgnoredElements = max(comparatorCache.getFirstSize(), comparatorCache.getSecondSize())
                - min(comparatorCache.getFirstSize(), comparatorCache.getSecondSize());

        comparatorCache.incrementFails(nrOfIgnoredElements);



        try {
            return comparatorCache.getFails() * 100
                    / (comparatorCache.getFails() + comparatorCache.getSuccesses())
                    <= TOLERANCE_THRESHOLD_PERCENT;
        } catch (ArithmeticException ex){
            //should be division by 0
            return true;
        }
    }


    //perhaps will be better to reuse some array instead of new object delcaration => less strain on jvm
    //TODO: need to optimize otherwise heap overflow
    public Pair<CustomImageType, CustomImageType> matchSize(CustomImageType sourceImage, CustomImageType targetImage) {
        //priroty by width:
        int width = max(sourceImage.getWidth(), targetImage.getWidth());

        CustomImageType sizedSourceImage, sizedTargetImage;

        if (sourceImage.getWidth() < width){
            int newHeight = (int)(((float)targetImage.getHeight() / (float)targetImage.getWidth()) * width);
            sizedSourceImage = new CustomImageType(width, newHeight, 1);

            sizedSourceImage
                    .getGraphics()
                    .drawImage(sourceImage, 0, 0, sizedSourceImage.getWidth(), sizedSourceImage.getHeight(), null);
        } else
            sizedSourceImage = sourceImage;


        if (targetImage.getWidth() < width){
            int newHeight = (int)(((float)targetImage.getHeight() / (float)targetImage.getWidth()) * width);
            sizedTargetImage = new CustomImageType(width, newHeight, 1);

            sizedTargetImage
                    .getGraphics()
                    .drawImage(sourceImage, 0, 0, sizedTargetImage.getWidth(), sizedTargetImage.getHeight(), null);
        } else
            sizedTargetImage = sourceImage;

        return Pair.of(sizedSourceImage, sizedTargetImage);
    }
}
