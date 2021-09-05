package kmeans;

public class Distance {
    public static double measure(Record argGlass1, Record argGlass2) {
        double _distanceBetweenTwoGlass = 0;

        _distanceBetweenTwoGlass += Math.pow(argGlass1.ri - argGlass2.ri, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.na - argGlass2.na, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.mg - argGlass2.mg, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.al - argGlass2.al, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.si - argGlass2.si, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.k - argGlass2.k, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.ca - argGlass2.ca, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.ba - argGlass2.ba, 2);
        _distanceBetweenTwoGlass += Math.pow(argGlass1.fe - argGlass2.fe, 2);

        _distanceBetweenTwoGlass = Math.sqrt(_distanceBetweenTwoGlass);

        return _distanceBetweenTwoGlass;
    }
}
