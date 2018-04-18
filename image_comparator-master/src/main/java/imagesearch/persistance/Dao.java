package imagesearch.persistance;


import imagesearch.image.CustomImageType;

import java.util.List;

public interface Dao {
    List<String> getForRgb(int averageRgb);
    void update(CustomImageType customImageType);
    void commit();
}
