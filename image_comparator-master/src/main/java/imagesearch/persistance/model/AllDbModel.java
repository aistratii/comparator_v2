package imagesearch.persistance.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllDbModel implements Serializable{
    private static final long serialVersionUID = 1l;

    private Map<Integer, List<String>> rgbModel;

    public AllDbModel(){
        rgbModel = new HashMap<>();
    }

    public Map<Integer, List<String>> getRgbDbModel() {
        return rgbModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AllDbModel that = (AllDbModel) o;

        return rgbModel != null ? rgbModel.equals(that.rgbModel) : that.rgbModel == null;

    }

    @Override
    public int hashCode() {
        return rgbModel != null ? rgbModel.hashCode() : 0;
    }
}
