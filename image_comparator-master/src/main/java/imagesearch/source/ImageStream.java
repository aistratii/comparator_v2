package imagesearch.source;

import imagesearch.image.CustomImageType;

public interface ImageStream {
    CustomImageType getNext();

    boolean hasNext();
}
