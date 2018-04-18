package imagesearch.persistance;


import imagesearch.image.CustomImageType;

import java.util.List;

public class MongoDbDao implements Dao {
    @Override
    public List<String> getForRgb(int averageRgb) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(CustomImageType customImageType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void commit() {
        throw new UnsupportedOperationException();
    }
}
