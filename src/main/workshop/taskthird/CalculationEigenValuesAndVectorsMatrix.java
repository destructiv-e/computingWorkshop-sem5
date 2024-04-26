package taskthird;

import static taskfirst.Matrix.makeUnitMatrix;

import java.util.List;
import taskfirst.Vector;
import taskfirst.Matrix;

/**
 * Класс для поиска собственных чисел и векторов матрицы
 */
public class CalculationEigenValuesAndVectorsMatrix {

  private final Matrix matrixA;

  /**
   * Конструктор класса
   *
   * @param matrixA - матрица
   */
  public CalculationEigenValuesAndVectorsMatrix(Matrix matrixA) {
    this.matrixA = matrixA;
  }

  public MethodJacobiResult methodJacobi(double epsilon) {
    Matrix matrixAcopy = new Matrix(matrixA.getMatrix());
    int k = 0;
    List<Integer> listIndex = matrixAcopy.findIkJk(matrixAcopy.getMatrix());
    int ik = listIndex.get(0);
    int jk = listIndex.get(1);
    double d, cos, sin;
    Matrix eigenVectorMatrix = makeUnitMatrix(matrixAcopy.getRows());
    Vector eigenVector;
    while (Math.abs(matrixAcopy.getMatrix()[ik][jk]) >= epsilon) {
      d = calcCoefD(ik, jk, matrixAcopy.getMatrix());
      cos = calcCoefC(ik, jk, matrixAcopy.getMatrix(), d);
      sin = calcCoefS(ik, jk, matrixAcopy.getMatrix(), d);

      matrixAcopy = getNextIterationMatrix(matrixAcopy.getMatrix(), ik, jk, cos, sin);
      Matrix rotationMatrix = getTransitionMatrix(ik, jk, cos, sin);
      eigenVectorMatrix = eigenVectorMatrix.multiply(rotationMatrix);
      k = k + 1;

      listIndex = matrixAcopy.findIkJk(matrixAcopy.getMatrix());
      ik = listIndex.get(0);
      jk = listIndex.get(1);
    }
    for (int i = 0; i < matrixA.getColumns(); i++) {
      eigenVector = eigenVectorMatrix.getColumn(i);
      eigenVector.normalize();
      eigenVectorMatrix.setColumn(i, eigenVector);
    }
    return new MethodJacobiResult(matrixAcopy, eigenVectorMatrix, k);
  }

  /**
   * Методы для подсчёта коэффициентов
   * @param ik - индекс
   * @param jk - индекс
   * @param a - матрица
   * @return - коэффициенты
   */
  private double calcCoefD(int ik, int jk, double[][] a) {
    return Math.sqrt(Math.pow(a[ik][ik] - a[jk][jk], 2) + 4 * Math.pow(a[ik][jk], 2));
  }

  private double calcCoefC(int ik, int jk, double[][] a, double d) {
    return Math.sqrt(0.5 * (1 + Math.abs(a[ik][ik] - a[jk][jk]) / d));
  }

  private double calcCoefS(int ik, int jk, double[][] a, double d) {
    return Math.signum(a[ik][jk] * (a[ik][ik] - a[jk][jk])) * calcCoefC(ik, jk, a, -d);
  }

  /**
   * Метод, для подсчёта матрицы A^(k+1) в методе Якоби
   *
   * @param a   - матрица A^k
   * @param ik  - индекс
   * @param jk  - индекс
   * @param cos - косинус
   * @param sin - синус
   * @return - матрица A^(k+1)
   */
  private Matrix getNextIterationMatrix(double[][] a, int ik, int jk, double cos, double sin) {
    double[][] result = new double[matrixA.getRows()][matrixA.getRows()];

    for (int i = 0; i < matrixA.getRows(); i++) {
      for (int j = 0; j < matrixA.getRows(); j++) {
        if (i != ik && i != jk && j != ik && j != jk) {
          result[i][j] = a[i][j];
        } else if (i != ik && i != jk) {
          result[i][ik] = cos * a[i][ik] + sin * a[i][jk];
          result[ik][i] = result[i][ik];
          result[i][jk] = -sin * a[i][ik] + cos * a[i][jk];
          result[jk][i] = result[i][jk];
        } else {
          result[ik][ik] =
              cos * cos * a[ik][ik] + 2 * cos * sin * a[ik][jk] + sin * sin * a[jk][jk];
          result[jk][jk] =
              sin * sin * a[ik][ik] - 2 * cos * sin * a[ik][jk] + cos * cos * a[jk][jk];
          result[ik][jk] = 0;
          result[jk][ik] = 0;
        }
      }
    }
    return new Matrix(result);
  }

  /**
   * Строит матрицу вращения
   *
   * @param ik  - индекс
   * @param jk  - индекс
   * @param cos - косинус
   * @param sin - синус
   * @return - матрица вращения
   */
  public Matrix getTransitionMatrix(int ik, int jk, double cos, double sin) {
    double[][] result = new double[matrixA.getRows()][matrixA.getRows()];
    for (int i = 0; i < matrixA.getRows(); i++) {
      for (int j = 0; j < matrixA.getRows(); j++) {
        if (i == j) {
          if (i != ik && i != jk) {
            result[i][i] = 1;
          }
        } else {
          if (i != ik && i != jk && j != ik && j != jk) {
            result[i][j] = 0;
          }
        }
      }
    }
    result[ik][ik] = cos;
    result[jk][jk] = cos;
    result[ik][jk] = -sin;
    result[jk][ik] = sin;
    return new Matrix(result);
  }

  /**
   * Степенной метод, для поиска максимального по модулю собственного числа матрицы
   *
   * @param epsilon - точность
   * @return - собственное число, вектор, кол-во итераций
   */
  public PowerAndScalarMethodResult powerMethod(double epsilon){
    int k = 0;
    double maxEigenVal = 0;
    double prev;
    MethodJacobiResult resJacobi = methodJacobi(1.E-6);
    Matrix matrixEigenVec = resJacobi.eigenvectorMatrix();
    Vector vectorMaxEigenVal = Vector.getZeroVector(matrixA.getRows());
    for (int i = 0; i < matrixA.getRows(); i++) {
      vectorMaxEigenVal = vectorMaxEigenVal.vecSum(matrixEigenVec.getColumn(i));
    }
    do{
      k++;
      prev = vectorMaxEigenVal.get(0);
      vectorMaxEigenVal = matrixA.multiplyByVector(vectorMaxEigenVal);
      maxEigenVal = vectorMaxEigenVal.get(0) / prev;
    } while (posterioriForEigen(maxEigenVal, vectorMaxEigenVal) >= epsilon);
    vectorMaxEigenVal.normalize();
    return  new PowerAndScalarMethodResult(maxEigenVal, vectorMaxEigenVal, k);
  }

  /**
   * Находит апостериорную оценку для степенного метода.
   *
   * @param eigenValue собственное число
   * @param vectorEigenValue собственный вектор
   * @return апостериорная оценка
   */
  public double posterioriForEigen(double eigenValue, Vector vectorEigenValue) {
    double numerator = matrixA.multiplyByVector(vectorEigenValue).subtract(vectorEigenValue.vecMultiplicationByNumber(eigenValue)).calcVectorTwoNorm();
    double denominator = vectorEigenValue.calcVectorTwoNorm();
    return numerator / denominator;
  }

  /**
   * Метод скалярный произведений
   *
   * @param epsilon - точность
   * @return - собственное число и вектор
   */
  public PowerAndScalarMethodResult scalarProductMethod(double epsilon){
    int k = 0;
    double maxEigenValue = 0;
    MethodJacobiResult valVec = methodJacobi(1.e-6);
    Matrix eigenVectors = valVec.eigenvectorMatrix();
    Vector prev;
    Vector vectorMaxEigenValue = Vector.getZeroVector(matrixA.getRows());
    for (int i = 0; i < matrixA.getColumns(); i++) {
      vectorMaxEigenValue = vectorMaxEigenValue.vecSum(eigenVectors.getColumn(i));
    }

    do{
      k++;
      prev = vectorMaxEigenValue;
      vectorMaxEigenValue = matrixA.multiplyByVector(vectorMaxEigenValue);
      maxEigenValue = vectorMaxEigenValue.multiplyScalar(prev) / prev.multiplyScalar(prev);
    } while (posterioriForEigen(maxEigenValue, vectorMaxEigenValue) >= epsilon);
    vectorMaxEigenValue.normalize();

    return new PowerAndScalarMethodResult(maxEigenValue, vectorMaxEigenValue, k);
  }

  /**
   * Нахождение противоположного элемента спектра
   *
   * @param epsilon - точность
   * @return - собственное число и вектор
   */
  public FindingOppositeEndSpectrumResult FindingOppositeEndSpectrum(double epsilon){
    PowerAndScalarMethodResult absMaxEigen = scalarProductMethod(epsilon);
    double absMaxEigenVal = absMaxEigen.maxEigenval();
    Matrix matrixB = matrixA.diff(makeUnitMatrix(matrixA.getRows()).mul(absMaxEigenVal));
    absMaxEigen = matrixB.scalarProductMethod1(epsilon);
    double res = absMaxEigen.maxEigenval() + absMaxEigenVal;
    return new FindingOppositeEndSpectrumResult(res, absMaxEigen.eigenVec());
  }

  /**
   * Метод Виландта + Эйткен
   *
   * @param epsilon - точность
   * @param approximateEigenValue - приближенное собственное число
   * @param refine - Эйткен
   * @return - результат
   */
  public FindingOppositeEndSpectrumResult findEigenValueWielandt(double epsilon,
      double approximateEigenValue, boolean refine) {
    int k = 0;
    double maxEigenValueW;
    double[] values = new double[3];
    int valuesLastInd = values.length - 1;
    values[valuesLastInd] = approximateEigenValue;
    MethodJacobiResult valVec = methodJacobi(epsilon);
    Matrix eigenVectors = valVec.eigenvectorMatrix();
    Vector vectorMaxEigenValue = Vector.getZeroVector(matrixA.getRows());
    for (int i = 0; i < matrixA.getColumns(); i++) {
      vectorMaxEigenValue = vectorMaxEigenValue.vecSum(eigenVectors.getColumn(i));
    }
    Matrix matrixW = matrixA.diff(makeUnitMatrix(matrixA.getRows()).mul(approximateEigenValue));
    Vector prevVector;

    do {
      k++;
      for (int i = 0; i < valuesLastInd; i++) {
        values[i] = values[i + 1];
      }
      prevVector = vectorMaxEigenValue;
      vectorMaxEigenValue = Matrix.matrixInverseDeterminate(matrixW).multiplyByVector(prevVector);
      maxEigenValueW = vectorMaxEigenValue.multiplyScalar(prevVector) / prevVector.multiplyScalar(prevVector);
      values[valuesLastInd] = 1 / maxEigenValueW + approximateEigenValue;
    } while (Math.abs(values[valuesLastInd] - values[valuesLastInd - 1]) >= epsilon);
    vectorMaxEigenValue.normalize();

    if (refine &&
        k >= 2 &&
        Math.abs(values[valuesLastInd]) < Math.abs(values[valuesLastInd - 1]) &&
        Math.abs(values[valuesLastInd - 1]) < Math.abs(values[valuesLastInd - 2])) {
      values[valuesLastInd] = (values[valuesLastInd] * values[valuesLastInd - 2] -
          Math.pow(values[valuesLastInd - 1], 2)) /
          (values[valuesLastInd] - 2 * values[valuesLastInd - 1] + values[valuesLastInd - 2]);
    }

    return new FindingOppositeEndSpectrumResult(values[valuesLastInd], vectorMaxEigenValue);
  }
}
