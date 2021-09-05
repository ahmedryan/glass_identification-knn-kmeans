package knn;

import java.io.*;
import java.util.*;

public class Classification {
    private int k;
    private int fold;

    private List<Record> data = new ArrayList<Record>();
    private List<Record> trainData = new ArrayList<Record>();
    private List<Record> testData = new ArrayList<Record>();

    private List<Double> accuracyList;
    private List<Double> fScoreList;
    private List<Double> precisionList;
    private List<Double> recallList;

    private double accuracy;
    private double fScore;
    private double precision;
    private double recall;

    private int successInOneFold = 0;
    private int failureInOneFold = 0;
    List<Integer> predictedGlassTypeOfTestData = new ArrayList<Integer>();
    List<Integer> actualGlassTypeOfTestData = new ArrayList<Integer>();

    private void readFile() {
        File _file = new File("src/glass.csv");

        try {
            BufferedReader _bufferedReader = new BufferedReader(new FileReader(_file));
            String _storageString;
            while ((_storageString = _bufferedReader.readLine()) != null) {
                String[] _temporaryString = _storageString.split(",", 0);

                if (_temporaryString[0].equals("RI")) continue;

                Double _ri = Double.parseDouble(_temporaryString[0]);
                Double _na = Double.parseDouble(_temporaryString[1]);
                Double _mg = Double.parseDouble(_temporaryString[2]);
                Double _al = Double.parseDouble(_temporaryString[3]);
                Double _si = Double.parseDouble(_temporaryString[4]);
                Double _k = Double.parseDouble(_temporaryString[5]);
                Double _ca = Double.parseDouble(_temporaryString[6]);
                Double _ba = Double.parseDouble(_temporaryString[7]);
                Double _fe = Double.parseDouble(_temporaryString[8]);
                int _type = Integer.parseInt(_temporaryString[9]);

                List<Double> _features = new ArrayList<>(Arrays.asList(_ri, _na, _mg, _al, _si, _k, _ca, _ba, _fe));

                Record _data = new Record(_type, _features);
                data.add(_data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inputAlgorithmConstraints() {
        System.out.println("Cross Validation Folds -");
        System.out.println("1# 5-fold");
        System.out.println("2# 10-fold");
        System.out.print("Enter Choice: ");
        Scanner myScanner = new Scanner(System.in);
        int _foldChoice = myScanner.nextInt();

        if (_foldChoice == 1) fold = 5;
        else if (_foldChoice == 2) fold = 10;
        else {
            System.out.println("..........INVALID FOLD..........");
            System.exit(0);
        }

        System.out.print("Enter value of K: ");
        int _kChoice = myScanner.nextInt();

        if (_kChoice >= 1 && _kChoice <= 20) k = _kChoice;
        else {
            System.out.println("..........INVALID K..........");
            System.exit(0);
        }
    }

    private void shuffleDataset() {
        Collections.shuffle(data);
    }

    private void divideDataset() {
        for (int i = 0; i < data.size(); i++) {
            if (i < ((data.size() * (fold - 1)) / fold)) trainData.add(data.get(i));
            else testData.add(data.get(i));
        }
    }

    private double distance(Record argsRecord1, Record argsRecord2) {
        double _distance = 0.0;
        _distance += Math.pow(argsRecord1.features.get(0) - argsRecord2.features.get(0), 2);
        _distance += Math.pow(argsRecord1.features.get(1) - argsRecord2.features.get(1), 2);
        _distance += Math.pow(argsRecord1.features.get(2) - argsRecord2.features.get(2), 2);
        _distance += Math.pow(argsRecord1.features.get(3) - argsRecord2.features.get(3), 2);
        _distance += Math.pow(argsRecord1.features.get(4) - argsRecord2.features.get(4), 2);
        _distance += Math.pow(argsRecord1.features.get(5) - argsRecord2.features.get(5), 2);
        _distance += Math.pow(argsRecord1.features.get(6) - argsRecord2.features.get(6), 2);
        _distance += Math.pow(argsRecord1.features.get(7) - argsRecord2.features.get(7), 2);
        _distance += Math.pow(argsRecord1.features.get(8) - argsRecord2.features.get(8), 2);

        _distance = Math.sqrt(_distance);

        return _distance;
    }

    private int[] sortData(double[] argsArray1, int[] argsArray2) {
        // sort
        for (int i = 1; i < argsArray1.length; i++) {
            double _current = argsArray1[i];
            int _currentType = argsArray2[i];

            int j = i - 1;
            while (j >= 0 && _current < argsArray1[j]) {
                argsArray1[j + 1] = argsArray1[j];
                argsArray2[j + 1] = argsArray2[j];
                j--;
            }

            argsArray1[j + 1] = _current;
            argsArray2[j + 1] = _currentType;
        }
        return argsArray2.clone();
    }

    private void classification() {
        int _correct = 0;
        int _wrong = 0;

        for (int _testDataCount = 0; _testDataCount < testData.size(); _testDataCount++) {
            double[] _distanceBetweenFeaturesOfTestAndTrainData = new double[trainData.size()];
            int[] _trainTypeOfGlass = new int[trainData.size()];

            for (int _trainDataCount = 0; _trainDataCount < trainData.size(); _trainDataCount++) {
                _trainTypeOfGlass[_trainDataCount] = trainData.get(_trainDataCount).type;
                _distanceBetweenFeaturesOfTestAndTrainData[_trainDataCount] = distance(
                        trainData.get(_trainDataCount), testData.get(_testDataCount));
            }

            int[] nearestTypesSortedByDistance = sortData(
                    _distanceBetweenFeaturesOfTestAndTrainData, _trainTypeOfGlass);
            int predictedTypeOfGlass = findNearestNeighbour(nearestTypesSortedByDistance);

            predictedGlassTypeOfTestData.add(predictedTypeOfGlass);
            actualGlassTypeOfTestData.add(testData.get(_testDataCount).type);

            if (predictedTypeOfGlass == testData.get(_testDataCount).type) _correct++;
            else _wrong++;
        }
        successInOneFold = _correct;
        failureInOneFold = _wrong;
    }

    private int findNearestNeighbour(int[] argsTypeArray) {
        int _type1 = 0, _type2 = 0, _type3 = 0, _type4 = 0, _type5 = 0, _type6 = 0, _type7 = 0;

        List<Integer> _typeCount = Arrays.asList(0, 0, 0, 0, 0, 0, 0);

        for (int i = 0; i < k; i++) {
            if (argsTypeArray[i] == 1) _typeCount.set(0, _typeCount.get(0) + 1);
            if (argsTypeArray[i] == 2) _typeCount.set(1, _typeCount.get(1) + 1);
            if (argsTypeArray[i] == 3) _typeCount.set(2, _typeCount.get(2) + 1);
            if (argsTypeArray[i] == 4) _typeCount.set(3, _typeCount.get(3) + 1);
            if (argsTypeArray[i] == 5) _typeCount.set(4, _typeCount.get(4) + 1);
            if (argsTypeArray[i] == 6) _typeCount.set(5, _typeCount.get(5) + 1);
            if (argsTypeArray[i] == 7) _typeCount.set(6, _typeCount.get(6) + 1);
        }

        int _max = Collections.max(_typeCount);
        return _typeCount.indexOf(_max) + 1;
    }

    private void performance() {
        int[][] _confusionMatrix = new int[7][7];
        Arrays.stream(_confusionMatrix).forEach(a -> Arrays.fill(a, 0));

        // row: actual
        // column: prediction
        for (int i = 0; i < testData.size(); i++) {
            _confusionMatrix[actualGlassTypeOfTestData.get(i) - 1][predictedGlassTypeOfTestData.get(i) - 1]++;
        }

        System.out.println("..........Confusion Matrix..........");
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                System.out.print(_confusionMatrix[row][col] + ",");
            }
            System.out.println();
        }

        // tp=truePositive, fp=falsePositive, fn=falseNegative, tn=trueNegative
        List<Integer> _tpList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        List<Integer> _fpList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        List<Integer> _fnList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
        List<Integer> _tnList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));

        for (int _type = 0; _type < 7; _type++) {
            for (int _row = 0; _row < 7; _row++) {
                for (int _col = 0; _col < 7; _col++) {
                    if (_type == _row && _type == _col)
                        _tpList.set(_type, _tpList.get(_type) + _confusionMatrix[_row][_col]); // tpList.get(type) = 0
                    else if (_type == _row) _tnList.set(_type, _tnList.get(_type) + _confusionMatrix[_row][_col]);
                    else if (_type == _col) _fnList.set(_type, _fnList.get(_type) + _confusionMatrix[_row][_col]);
                    else _fpList.set(_type, _fpList.get(_type) + _confusionMatrix[_row][_col]);
                }
            }
        }

        List<Double> _precisionList = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        List<Double> _recallList = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        List<Double> _fScoreList = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        List<Double> _accuracyList = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));

        for (int _type = 0; _type < 7; _type++) {
            _precisionList.set(_type, (_tpList.get(_type) * 1.0) / ((_tpList.get(_type) + _fpList.get(_type)) == 0 ? 1 : (_tpList.get(_type) + _fpList.get(_type))));
            _recallList.set(_type, (_tpList.get(_type) * 1.0) / ((_tpList.get(_type) + _fnList.get(_type)) == 0 ? 1 : (_tpList.get(_type) + _fnList.get(_type))));
            _fScoreList.set(_type, (2.0 * _precisionList.get(_type) * _recallList.get(_type)) / ((_precisionList.get(_type) + _recallList.get(_type)) == 0 ? 1 : (_precisionList.get(_type) + _recallList.get(_type))));
            _accuracyList.set(_type, (_tpList.get(_type) + _fpList.get(_type)) * 1.0 / ((_tpList.get(_type) + _tnList.get(_type) + _fpList.get(_type) + _fnList.get(_type)) == 0 ? 1 : (_tpList.get(_type) + _tnList.get(_type) + _fpList.get(_type) + _fnList.get(_type))));
        }

        double _accuracyOfAllTypes = _accuracyList.stream().mapToDouble(i -> i).sum() / 7 / 0.01;
        double _precisionOfAllTypes = _precisionList.stream().mapToDouble(i -> i).sum() / 7 / 0.01;
        double _fScoreOfAllTypes = _fScoreList.stream().mapToDouble(i -> i).sum() / 7 / 0.01;
        double _recallOfAllTypes = _recallList.stream().mapToDouble(i -> i).sum() / 7 / 0.01;

        accuracy += _accuracyOfAllTypes;
        precision += _precisionOfAllTypes;
        recall += _recallOfAllTypes;
        fScore += _fScoreOfAllTypes;

        // print
        System.out.print("True Positive: ");
        for (int i = 0; i < 7; i++) {
            System.out.print((i + 1) + "->" + _tpList.get(i) + ",");
        }
        System.out.print("\nFalse Negative: ");
        for (int i = 0; i < 7; i++) {
            System.out.print((i + 1) + "->" + _fnList.get(i) + ",");
        }
        System.out.print("\nTrue Negative: ");
        for (int i = 0; i < 7; i++) {
            System.out.print((i + 1) + "->" + _tnList.get(i) + ",");
        }
        System.out.print("\nFalse Positive: ");
        for (int i = 0; i < 7; i++) {
            System.out.print((i + 1) + "->" + _fpList.get(i) + ",");
        }
        System.out.println();
    }

    private void clearTestAndTrainSet() {
        trainData.clear();
        testData.clear();
        predictedGlassTypeOfTestData.clear();
        actualGlassTypeOfTestData.clear();
    }

    private void rotateDataset() {
        Collections.rotate(data, (data.size() / fold));
    }

    private void finalPerformanceMeasure() {
        System.out.println("..........K-NN...........");
        System.out.println("Precision: " + precision / fold);
        System.out.println("Recall: " + recall / fold);
        System.out.println("F-Score: " + fScore / fold);
        System.out.println("Accuracy: " + accuracy / fold);
    }

    public void crossValidation() {
        readFile();
        inputAlgorithmConstraints();
        shuffleDataset();
        for (int _crossValidationCount = 0; _crossValidationCount < fold; _crossValidationCount++) {
            divideDataset();
            classification();
            performance();
            clearTestAndTrainSet();
            rotateDataset();
        }
        finalPerformanceMeasure();
    }
}
