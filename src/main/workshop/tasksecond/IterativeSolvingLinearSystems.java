package tasksecond;

import java.util.ArrayList;
import java.util.List;
import taskfirst.Matrix;
import taskfirst.Vector;

/**
 * Класс для решения системы линеных систем (x=Hx+G) итеративными методами
 */
public class IterativeSolvingLinearSystems {

  private Matrix matrixA;
  private Vector vectorB;
  private final Matrix matrixH;
  private Vector vectorG;
  private Vector zeroIteratVec;

  /**
   * Конструктор класса
   *
   * @param matrixA - матрица А
   * @param vectorB - вектор В
   */
  public IterativeSolvingLinearSystems(Matrix matrixA, Vector vectorB) {
    this.matrixA = matrixA;
    this.vectorB = vectorB;
    this.matrixH = getMatrixHlist(getMatrixHandVectorG(matrixA, vectorB));
    this.vectorG = getVectorBlist((getMatrixHandVectorG(matrixA, vectorB)));
    this.zeroIteratVec = Vector.getZeroVector(vectorB.getSize());
  }

  /**
   * Метод, который вычисляет массив H и массив G
   *
   * @param matrixA - матрица А
   * @param vectorB - вектор В
   * @return - List массивов состоящий из двух элементов
   */
  public List<double[][]> getMatrixHandVectorG(Matrix matrixA, Vector vectorB) {
    double[][] matrixHarr = new double[matrixA.getRows()][matrixA.getRows()];
    double[][] vectorGarr = new double[vectorB.getSize()][1];
    Matrix matrixAcoppy = new Matrix(matrixA.getMatrix());
    Vector vectorBcoppy = new Vector(vectorB.getVectorArr());
    for (int i = 0; i < matrixAcoppy.getRows(); i++) {
      for (int j = 0; j < matrixAcoppy.getRows(); j++) {
        if (i == j) {
          matrixHarr[i][j] = 0;
        } else {
          matrixHarr[i][j] = -matrixAcoppy.getElemMatrix(i, j) / matrixAcoppy.getElemMatrix(i, i);
        }
      }
    }
    for (int i = 0; i < vectorBcoppy.getSize(); i++) {
      vectorGarr[i][0] = vectorBcoppy.get(i) / matrixAcoppy.getElemMatrix(i, i);
    }
    List<double[][]> listVecAndMat = new ArrayList<>();
    listVecAndMat.add(matrixHarr);
    listVecAndMat.add(vectorGarr);

    return listVecAndMat;
  }

  /**
   * Априорная оценка решения на определённой итерации
   *
   * @param k - номер итерации
   * @return априорное решение
   */
  public double caclApriorAssessment(int k) {
    return Math.pow(matrixH.matrixNorma(matrixH), k) * (zeroIteratVec.calcVectorInfNorm()
        + vectorG.calcVectorInfNorm() / (1 - matrixH.matrixNorma(matrixH)));
  }

  /**
   * находит номер итерации и саму ошибку для априорной оценки, которая меньше eps
   *
   * @param eps - точность приближения
   * @return - номер итерации
   */
  public List<Double> IteratOfAprioriAssesLessEpsilon(double eps) {
    int k = 0;
    double assessment = caclApriorAssessment(k);
    List<Double> res = new ArrayList<>();
    while (assessment >= eps) {
      k += 1;
      assessment = caclApriorAssessment(k);
    }
    res.add((double) k);
    res.add(assessment);
    return res;
  }

  /**
   * Решает систему методом простой итерации, а так же проводит апостериорную оценку
   *
   * @param epsilon    - точность
   * @param withLuster - приближать или нет ответ методом Люстерника
   * @return - вектор решение, количество итераций + апостреиорная оценка
   */
  public IterationMethodsResult solveSimpleIteration(double epsilon, boolean withLuster) {
    int iterations = 0;
    Vector prevX = new Vector(zeroIteratVec.getSize());
    Vector currX = new Vector(zeroIteratVec.getVectorArr());
    double error;

    do {
      for (int i = 0; i < currX.getSize(); i++) {
        prevX.setVector(i, currX.get(i));
      }
      currX = iterate(prevX);
      error = calculateError(currX, prevX);
      iterations++;
    } while (error >= epsilon);

    if (withLuster) {
      currX = applyLusterMethod(currX, prevX);
    }

    return new IterationMethodsResult(currX, iterations, error);
  }

  /**
   * Подсчёт итерации
   *
   * @param prevX - вектор, который хранит предыдущее приближение решения системы уравнений в
   *              процессе итерации.
   * @return - ектор, который хранит текущее приближение решения системы уравнений.
   */
  private Vector iterate(Vector prevX) {
    return matrixH.multiplyByVector(prevX).vecSum(vectorG);
  }

  /**
   * вычисляет и возвращает ошибку между текущим и предыдущим приближениями решения системы
   * уравнений
   *
   * @param currX -
   * @param prevX -
   * @return - ошибку
   */
  private double calculateError(Vector currX, Vector prevX) {
    return currX.subtract(prevX).calcVectorInfNorm();
  }

  /**
   * Применяет метод Люстерника для уточнения конечного ответа.
   *
   * @param currX -
   * @param prevX -
   * @return - уточненное приближение к решению системы уравнений
   */
  private Vector applyLusterMethod(Vector currX, Vector prevX) {
    return prevX.vecSum(currX.subtract(prevX).vecDivisionByNumber(1 - matrixH.spectralRadius()));
  }

  /**
   * Вычисляет решение системы методом Зейделя.
   *
   * @param epsilon точность приближения
   * @return объект IterationMethodsResult, содержащий решение системы, количество итераций и
   * ошибку.
   */
  public IterationMethodsResult solveSeidel(double epsilon) {
    int k = 0;
    double posteriori;
    Vector prevX = new Vector(zeroIteratVec.getSize());
    Vector currX = new Vector(zeroIteratVec.getVectorArr());
    Matrix matrixHcopy = new Matrix(matrixH.getMatrix());
    List<Matrix> pairHLHR = matrixHcopy.divideToLeftRight();
    Matrix matrixHL = pairHLHR.get(0);
    Matrix matrixHR = pairHLHR.get(1);

    do {
      for (int i = 0; i < currX.getSize(); i++) {
        prevX.setVector(i, currX.get(i));
      }
      currX = matrixHL.multiplyByVector(currX)
          .vecSum(matrixHR.multiplyByVector(prevX).vecSum(vectorG));
      posteriori = currX.subtract(prevX).calcVectorInfNorm();
      k += 1;
    } while (posteriori >= epsilon);

    return new IterationMethodsResult(currX, k, posteriori);
  }

  /**
   * Находит матрицу перехода H_seidel.
   *
   * @return матрица перехода
   */
  public Matrix calcSeidelMatrix() {
    List<Matrix> pairHLHR = matrixH.divideToLeftRight();
    Matrix matrixHL = pairHLHR.get(0);
    Matrix matrixHR = pairHLHR.get(1);
    return Matrix.makeUnitMatrix(matrixH.getRows()).diff(matrixHL).getInverseMatrix1()
        .multiply(matrixHR);
  }

  /**
   * Вычисляет решение системы методом верхней релаксации
   *
   * @param epsilon - точность
   * @return - объект IterationMethodsResult, содержащий приближенное решение системы (currX),
   * количество итераций (k) и апостериорную оценку ошибки (posteriori)
   */
  public IterationMethodsResult solveUpperRelaxation(double epsilon) {
    int n = vectorG.getSize();
    double calcElement;
    double qOptimal = 2 / (1 + Math.sqrt(1 - Math.pow(matrixH.spectralRadius(), 2)));
    Vector prev = new Vector(zeroIteratVec.getSize());
    Vector curr = new Vector(zeroIteratVec.getSize());
    int iterationCount = 0;
    for (int i = 0; i < zeroIteratVec.getSize(); i++) {
      curr.getVectorArr()[i] = zeroIteratVec.get(i);
    }
    do {
      iterationCount++;
      for (int i = 0; i < curr.getSize(); i++) {
        prev.setVector(i, curr.getVectorArr()[i]);
      }
      for (int i = 0; i < n; i++) {
        calcElement = 0;
        for (int j = 0; j < i; j++) {
          calcElement += matrixH.getElemMatrix(i, j) * curr.get(j);
        }
        for (int j = i + 1; j < n; j++) {
          calcElement += matrixH.getElemMatrix(i, j) * curr.get(j);
        }
        calcElement += -curr.get(i) + vectorG.get(i);
        calcElement = curr.get(i) + qOptimal * calcElement;
        curr.setVector(i, calcElement);
      }
    } while (curr.subtract(prev).calcVectorInfNorm() >= epsilon);

    double posteriori = curr.subtract(prev).calcVectorInfNorm();
    return new IterationMethodsResult(curr, iterationCount, posteriori);
  }

  public Matrix getMatrixHlist(List<double[][]> listMat) {
    return new Matrix(listMat.get(0));
  }

  public Vector getVectorBlist(List<double[][]> listVec) {
    Matrix mat = new Matrix(listVec.get(1));
    return mat.getColumnAsVector(0);
  }

  public Vector getZeroIteratVec() {
    return zeroIteratVec;
  }

  public void setZeroIteratVec(Vector zeroIteratVec) {
    this.zeroIteratVec = zeroIteratVec;
  }

  public Vector getVectorG() {
    return vectorG;
  }

  public void setVectorG(Vector vectorG) {
    this.vectorG = vectorG;
  }

  public Matrix getMatrixH() {
    return matrixH;
  }

  public Matrix getMatrixA() {
    return matrixA;
  }

  public void setMatrixA(Matrix matrixA) {
    this.matrixA = matrixA;
  }

  public Vector getVectorB() {
    return vectorB;
  }

  public void setVectorB(Vector vectorB) {
    this.vectorB = vectorB;
  }
}
