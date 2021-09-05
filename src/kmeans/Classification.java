package kmeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Classification {
    private final int k = 7;
    private List<Record> dataset;
    private List<Record> previousCentroids;
    private List<Record> nextCentroids;
    private List<ArrayList<Record>> clusters;

    int iteration = 0;

    private void loadDataset() {
        Read _read = new Read();
        dataset = _read.readFile();
    }

    private void loadCentroid() {
        List<ArrayList<Record>> _givenClusters;
        _givenClusters = new ArrayList<>();
        previousCentroids = new ArrayList<>();

        for (int _clusterCounter = 0; _clusterCounter <= k; _clusterCounter++) {
            _givenClusters.add(new ArrayList<>());
        }

        for (int _typeCounter = 0; _typeCounter <= k; _typeCounter++) {
            for (Record _data : dataset) {
                if (_typeCounter == _data.type) {
                    _givenClusters.get(_typeCounter).add(_data);
                }
            }
        }

        int _clusterCounter = 0;
        for (List<Record> _recordList : _givenClusters) {
            double _al = 0, _ri = 0, _si = 0, _ba = 0, _ca = 0, _fe = 0, _k = 0, _mg = 0, _na = 0;

            for (Record record : _recordList) {
                _al += record.al;
                _ri += record.ri;
                _si += record.si;
                _ba += record.ba;
                _ca += record.ca;
                _fe += record.fe;
                _k += record.k;
                _mg += record.mg;
                _na += record.na;
            }
            int totalRecordsInThisCluster = _givenClusters.get(_clusterCounter).size();
            _al = _al / totalRecordsInThisCluster;
            _ri = _ri / totalRecordsInThisCluster;
            _si = _si / totalRecordsInThisCluster;
            _ba = _ba / totalRecordsInThisCluster;
            _ca = _ca / totalRecordsInThisCluster;
            _fe = _fe / totalRecordsInThisCluster;
            _k = _k / totalRecordsInThisCluster;
            _mg = _mg / totalRecordsInThisCluster;
            _na = _na / totalRecordsInThisCluster;
            previousCentroids.add(new Record(-1, _ri, _na, _mg, _al, _si, _k, _ca, _ba, _fe));
            _clusterCounter++;
        }
    }

    private void assignGlassToCluster() {
        clusters = new ArrayList<>();

        for (Record _centroid : previousCentroids) {
            clusters.add(new ArrayList<>());
        }

        for (Record _record : dataset) {
            double _initialDistance = 999999;
            int _typeToWhichTheRecordBelongs = 0;

            int _centroidCounter = 0;
            for (Record _centroid : previousCentroids) {
                double _distanceBetweenRecordAndCentroid = Distance.measure(_record, _centroid);

                if (_distanceBetweenRecordAndCentroid < _initialDistance) {
                    _initialDistance = _distanceBetweenRecordAndCentroid;
                    _typeToWhichTheRecordBelongs = _centroidCounter;
                }
                _centroidCounter++;
            }
            clusters.get(_typeToWhichTheRecordBelongs).add(_record);
        }
    }

    private void relocateMeanOfCluster() {
        nextCentroids = new ArrayList<>();

        int _clusterLoopCounter = 0;
        for (ArrayList<Record> _cluster : clusters) {
            double _ri = 0, _na = 0, _mg = 0, _al = 0, _si = 0, _k = 0, _ca = 0, _ba = 0, _fe = 0;
            for (Record _record : _cluster) {
                _ri += _record.ri;
                _na += _record.na;
                _mg += _record.mg;
                _al += _record.al;
                _si += _record.si;
                _k += _record.k;
                _ca += _record.ca;
                _ba += _record.ba;
                _fe += _record.fe;
            }

            int _totalRecords = _cluster.size();
            Record _nextCentroidRecord = new Record(-1, _ri / _totalRecords, _na / _totalRecords, _mg / _totalRecords, _al / _totalRecords, _si / _totalRecords, _k / _totalRecords, _ca / _totalRecords, _ba / _totalRecords, _fe / _totalRecords);
            nextCentroids.add(_clusterLoopCounter, _nextCentroidRecord);
            _clusterLoopCounter++;
        }
    }

    private boolean isDifferenceOfCentroidsSignificant(Record argsCentroid1, Record argsCentroid2) {
        double _threshold = 0.000001;
        return Math.abs(argsCentroid1.ba) - Math.abs(argsCentroid2.ba) < _threshold &&
                Math.abs(argsCentroid1.ca - argsCentroid2.ca) < _threshold &&
                Math.abs(argsCentroid1.na - argsCentroid2.na) < _threshold &&
                Math.abs(argsCentroid1.al - argsCentroid2.al) < _threshold &&
                Math.abs(argsCentroid1.fe - argsCentroid2.fe) < _threshold &&
                Math.abs(argsCentroid1.k - argsCentroid2.k) < _threshold &&
                Math.abs(argsCentroid1.mg - argsCentroid2.mg) < _threshold &&
                Math.abs(argsCentroid1.ri - argsCentroid2.ri) < _threshold &&
                Math.abs(argsCentroid1.si - argsCentroid2.si) < _threshold;
    }

    public void run() {
        loadDataset();
        loadCentroid();

        do {
            assignGlassToCluster();
            relocateMeanOfCluster();

            for (int _centroidIndexCounter = 0; _centroidIndexCounter < previousCentroids.size(); _centroidIndexCounter++) {
                if (isDifferenceOfCentroidsSignificant(previousCentroids.get(_centroidIndexCounter), nextCentroids.get(_centroidIndexCounter))) {
                    int _clusterCounter = 0;
                    int _correct = 0, _wrong = 0;
                    for (List<Record> _cluster : clusters) {
                        for (Record _record : _cluster) {
                            if (_record.type == _clusterCounter) _correct++;
                            else _wrong++;
                        }
                        _clusterCounter++;
                    }
                    double _accuracy = ((_correct * 1.0) / (_correct + _wrong)) * 100;
                    System.out.println("..........K-Means...........");
                    System.out.println("Accuracy: " + _accuracy);
                    return;
                }
            }
            Collections.copy(previousCentroids, nextCentroids);
//            System.out.println("Iteration: " + iteration);
            iteration++;
        } while (true);
    }

}
