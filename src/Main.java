import knn.Classification;

public class Main {
    public static void main(String[] args) {
        new Classification().crossValidation();
        new kmeans.Classification().run();
    }
}
