package taskfirst;

import java.util.List;

/**
 * Класс решатель систем линейный уравнений
 */
public class SolvingLinearSystems {

  private final Matrix squareMatrixA;
  private final Vector vectorB;
  private final double epsilon;
  private final int rows;

  /**
   * Конструктор класса
   *
   * @param squareMatrixA - матрица A
   * @param vectorB       - вектор B
   */
  public SolvingLinearSystems(Matrix squareMatrixA, Vector vectorB, double epsilon) {
    this.squareMatrixA = squareMatrixA;
    this.vectorB = vectorB;
    this.epsilon = epsilon;
    this.rows = squareMatrixA.getRows();
  }

  /**
   * Метод, который реализует прямой ход схемы Гаусса единственного деления
   *
   * @param matrixA - матрица A
   * @param vectorB - вектор B
   * @return - верхнетреугольная матрица
   */
  public Matrix reductionToUpperTriangMatrix(Matrix matrixA, Vector vectorB) {
    double leadingElement;
    int rows = matrixA.getRows();
    Matrix extendedMatrix = matrixA.getExtendedMatrix(matrixA, vectorB);

    for (int k = 1; k <= rows; k++) {
      leadingElement = extendedMatrix.getMatrix()[k - 1][k - 1];

      if (leadingElement < epsilon) {
        System.out.println(
            "( Слишком малый ведущий элемент. Возможна большая погрешность )");
      }
      // вычисление значений треугольной матрицы
      for (int j = k; j <= rows + 1; j++) {
        extendedMatrix.getMatrix()[k - 1][j - 1] =
            extendedMatrix.getMatrix()[k - 1][j - 1] / leadingElement;
      }
      // обнуление значений под треугольной матрицей
      for (int i = k + 1; i <= rows; i++) {
        leadingElement = extendedMatrix.getMatrix()[i - 1][k - 1];
        for (int j = k; j <= rows + 1; j++) {
          extendedMatrix.getMatrix()[i - 1][j - 1] = extendedMatrix.getMatrix()[i - 1][j - 1]
              - extendedMatrix.getMatrix()[k - 1][j - 1] * leadingElement;
        }
      }
    }
    return extendedMatrix;
  }

  /**
   * Метод, который реализует прямой ход схемы Гаусса с выбором главного элемента по столбцу
   *
   * @param matrixA - матрица A
   * @param vectorB - вектор B
   * @return - система с верхнетреугольной матрицей
   */
  public Matrix reductionToUpperTriangMatrixElemSelect(Matrix matrixA, Vector vectorB) {
    double leadingElement;
    int indexToSwap;
    int rows = matrixA.getRows();
    Matrix extendedMatrix = matrixA.getExtendedMatrix(matrixA, vectorB);
    for (int k = 1; k <= rows; k++) {
      leadingElement = extendedMatrix.getMatrix()[k - 1][k - 1];
      indexToSwap = k - 1;

      for (int i = k; i <= rows; i++) {
        if (Math.abs(extendedMatrix.getMatrix()[i - 1][k - 1]) >= leadingElement) {
          leadingElement = extendedMatrix.getMatrix()[i - 1][k - 1];
          indexToSwap = i;
        }
      }

      if (indexToSwap != k - 1) {
        double temp;
        for (int i = 1; i <= rows + 1; i++) {
          temp = extendedMatrix.getMatrix()[indexToSwap - 1][i - 1];
          extendedMatrix.getMatrix()[indexToSwap - 1][i - 1] = extendedMatrix.getMatrix()[k - 1][i
              - 1];
          extendedMatrix.getMatrix()[k - 1][i - 1] = temp;
        }
      }

      if (leadingElement < epsilon) {
        System.out.println(
            "( Слишком малый ведущий элемент. Возможна большая погрешность )");
      }

      for (int j = k; j <= rows + 1; j++) {
        extendedMatrix.getMatrix()[k - 1][j - 1] =
            extendedMatrix.getMatrix()[k - 1][j - 1] / leadingElement;
      }

      for (int i = k + 1; i <= rows; i++) {
        leadingElement = extendedMatrix.getMatrix()[i - 1][k - 1];
        for (int j = k; j <= rows + 1; j++) {
          extendedMatrix.getMatrix()[i - 1][j - 1] = extendedMatrix.getMatrix()[i - 1][j - 1]
              - extendedMatrix.getMatrix()[k - 1][j - 1] * leadingElement;
        }
      }
    }
    return extendedMatrix;
  }

  /**
   * Метод, для решения системы линейных уравнений с треугольной матрицей
   *
   * @param matrixA - расширенная матрица
   * @return - вектор-решение
   */
  public Vector solutionTriangularSystem(Matrix matrixA) {
    int rows = matrixA.getRows();
    double[] resultVec = new double[rows];
    for (int i = rows; i >= 1; i--) {
      double tmp = 0;
      for (int j = i + 1; j <= rows; j++) {
        tmp += matrixA.getMatrix()[i - 1][j - 1] * resultVec[j - 1];
      }
      resultVec[i - 1] = matrixA.getMatrix()[i - 1][rows] - tmp;
    }
    return new Vector(resultVec);
  }

  /**
   * Метод, для решения системы линеныйных уравнений методом Гаусса с выбором главного элеметна
   *
   * @param matrix - матрица А
   * @param vector - вектор В
   * @return - вектор Х
   */
  public Vector solutionGaussElemSelec(Matrix matrix, Vector vector) {
    return solutionTriangularSystem(reductionToUpperTriangMatrixElemSelect(matrix, vector));
  }

  /**
   * Метод, для решения системы уравнений схемой Гаусса единственного деления
   *
   * @param matrix - матрица А
   * @param vector - вектор В
   * @return - вектор Х
   */
  public Vector solutionGauss(Matrix matrix, Vector vector) {
    return solutionTriangularSystem((reductionToUpperTriangMatrix(matrix, vector)));
  }

  /**
   * Метод, который раскладывает расширенную матрицу A на матрицы L*U
   *
   * @param matrix - расширенная матрица
   * @return - матрица U и матрица Y
   */
  public List<Matrix> getMatrixUandY(Matrix matrix) {
    double[][] matrixLArr = new double[rows][rows];
    double[][] matrixUextArr = new double[rows][rows + 1];
    Matrix matrixL = new Matrix(matrixLArr);
    Matrix extendedMatrixU = new Matrix(matrixUextArr);
    double temp;

    for (int i = 0; i < rows; i++) {
      for (int j = i; j < rows; j++) {
        temp = 0;
        for (int k = 0; k < i; k++) { // Изменено здесь
          temp += matrixL.getElemMatrix(j, k) * extendedMatrixU.getElemMatrix(k, i);
        }
        matrixL.set(matrix.getElemMatrix(j, i) - temp, j, i);
      }
      for (int j = i; j <= rows; j++) {
        temp = 0;
        for (int k = 0; k < i; k++) { // Изменено здесь
          temp += matrixL.getElemMatrix(i, k) * extendedMatrixU.getElemMatrix(k, j);
        }
        extendedMatrixU.set((matrix.getElemMatrix(i, j) - temp) / matrixL.getElemMatrix(i, i), i,
            j);
      }
    }
    return extendedMatrixU.splitMatrix(extendedMatrixU);
  }

  /**
   * Метод решения системы линейных уравнений методом LU разложения
   *
   * @param matrix - матрица A
   * @param vector - вектор B
   * @return - вектор X
   */
  public Vector solutionLU(Matrix matrix, Vector vector) {
    Matrix exMatrix = matrix.getExtendedMatrix(matrix, vector);
    List<Matrix> resList = getMatrixUandY(exMatrix);
    Matrix matrixU = resList.get(0);
    Matrix matrixY = resList.get(1);
    Vector vectorY = matrixY.getColumnAsVector(0);
    Matrix extendedMatrixUandY = matrixU.getExtendedMatrix(matrixU, vectorY);
    return solutionTriangularSystem(extendedMatrixUandY);
  }
}
