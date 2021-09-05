package knn;

import java.util.List;

public class Record {
    int type;
    List<Double> features;

    public Record(int type, List<Double> features) {
        this.type = type;
        this.features = features;
    }
}
