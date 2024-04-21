package taskthird;

import java.util.ArrayList;
import java.util.List;
import taskfirst.Matrix;

public class MainThirdTask {

  public static void main(String[] args) {
    Matrix matrixA = new Matrix(new double[][]{
        {-1.48213, -0.05316, 1.08254},
        {-0.05316, 1.13958, 0.01617},
        {1.08254, 0.01617, -1.48271}});
    List<Integer> res = matrixA.findIkJk(matrixA.getMatrix());
    System.out.println(res.get(0));
    System.out.println(res.get(1));
  }
}
