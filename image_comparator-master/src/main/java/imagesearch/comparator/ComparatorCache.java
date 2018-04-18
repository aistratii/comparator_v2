package imagesearch.comparator;

import imagesearch.image.CustomImageType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class ComparatorCache {
    private int fails;
    private int successes;
    private int[] firstVector, secondVector;
    private int firstSize, secondSize;

    public ComparatorCache(){
        firstVector = new int[200];
        secondVector = new int[200];
    }

    public int getFails() {
        return fails;
    }

    public int getSuccesses() {
        return successes;
    }

    public int getFirstSize() {
        return firstSize;
    }

    public int getSecondSize() {
        return secondSize;
    }

    public int[] getFirstVector() {
        return firstVector;
    }

    public int[] getSecondVector(){
        return secondVector;
    }

    public void setBuffer(CustomImageType first, CustomImageType second) {
        successes = 0;
        fails = 0;

        firstSize = first.getWidth() * first.getHeight();
        secondSize = second.getWidth() * second.getHeight();

        if (firstVector.length < firstSize)
            firstVector = new int[firstSize];

        if (secondVector.length < secondSize)
            secondVector = new int[secondSize];

        int index = 0;
        for (int i = 0; i < first.getWidth(); i++)
            for (int j = 0; j < first.getHeight(); j++) {
            try{
                firstVector[index++] = first.getRGB(i, j);
            } catch (Exception ex){ex.printStackTrace();
        }}


        index = 0;
        for (int i = 0; i < second.getWidth(); i++)
            for (int j = 0; j < second.getHeight(); j++)
                secondVector[index++] = second.getRGB(i, j);
    }

    public void incrementSuccesses() {
        successes++;
    }

    public void incrementFails() {
        fails++;
    }

    public void incrementFails(int failsNumber) {
        fails += failsNumber;
    }
}
