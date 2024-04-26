package taskfirst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import taskthird.CalculationEigenValuesAndVectorsMatrix;
import taskthird.MethodJacobiResult;
import taskthird.PowerAndScalarMethodResult;

/**
 * Класс матриц
 */
public class Matrix {

  private int rows;
  private int columns;
  private double[][] matrix;

  /**
   * Конструктор класса матриц
   *
   * @param array2d - двумерный массив, который принимает программа
   */
  public Matrix(double[][] array2d) {
    this.columns = array2d[0].length;
    this.rows = array2d.length;
    this.matrix = Arrays.copyOf(array2d, array2d.length);
  }

  /**
   * Создает расширенную матрицу
   *
   * @param matrix - исходная матрицы
   * @param vector - вектор
   * @return - расширенная матрица
   */
  public Matrix getExtendedMatrix(Matrix matrix, Vector vector) {
    double[][] extendedMatrix = new double[matrix.rows][matrix.rows + 1];
    for (int i = 0; i < matrix.rows; i++) {
      System.arraycopy(matrix.getMatrix()[i], 0, extendedMatrix[i], 0,
          matrix.getMatrix()[i].length);
      extendedMatrix[i][matrix.rows] = vector.getVectorArr()[i];
    }
    return new Matrix(extendedMatrix);
  }

  /**
   * Метод, который находит обратную матрицу
   *
   * @param matrix - матрица
   * @return - обратная матрица
   */
  public Matrix getInverseMatrix(Matrix matrix) {
    if (matrix.rows != matrix.columns) {
      System.out.println("Матрица не квадратная!");
      return null;
    }
    double[][] inverseMatrixArr = new double[matrix.rows][matrix.columns];
    Matrix inverseMatrix = new Matrix(inverseMatrixArr);
    Vector vectorRes;
    List<Vector> unitVectors = Vector.getListUnitVector(matrix.rows);
    for (int i = 0; i < matrix.rows; i++) {
      Vector vector_i = unitVectors.get(i);
      vectorRes = new SolvingLinearSystems(matrix, vector_i, 1.E-3).solutionGaussElemSelec(matrix,
          vector_i);
      for (int j = 0; j < rows; j++) {
        inverseMatrix.set(vectorRes.getVectorArr()[j], i, j);
      }
    }
    return inverseMatrix;
  }

  public Matrix getInverseMatrix1() {
    double[][] inverseMatrixArr = new double[matrix.length][matrix.length];
    Matrix inverseMatrix = new Matrix(inverseMatrixArr);
    Vector vectorRes;
    List<Vector> unitVectors = Vector.getListUnitVector(matrix.length);
    for (int i = 0; i < matrix.length; i++) {
      Vector vector_i = unitVectors.get(i);
      vectorRes = new SolvingLinearSystems(new Matrix(matrix), vector_i,
          1.E-3).solutionGaussElemSelec(new Matrix(matrix),
          vector_i);
      for (int j = 0; j < rows; j++) {
        inverseMatrix.set(vectorRes.getVectorArr()[j], i, j);
      }
    }
    return inverseMatrix.transpose();
  }

  /**
   * Метод для транспонирования матрицы
   *
   * @return транспонированная матрица
   */
  public Matrix transpose() {
    double[][] transposedMatrix = new double[columns][rows];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        transposedMatrix[j][i] = matrix[i][j];
      }
    }
    return new Matrix(transposedMatrix);
  }

  /**
   * Метод, который удаляет i cтроку и j столбец.
   *
   * @param matrix        - матрица из которой хотим удалить строку и столбец
   * @param deleteRows    - номер строки для удаления
   * @param deleteColumns - номер столбца для удаления
   * @return - матрицу размерности на 1 меньше
   */
  public static Matrix matrixRowsAndColumnsMinus1(Matrix matrix, int deleteRows,
      int deleteColumns) {
    double[][] arrayNewMatrix = new double[matrix.getRows() - 1][matrix.getColumns() - 1];
    int offSetRow = 0;
    int offSetCol;
    for (int i = 0; i < matrix.getMatrix().length - 1; i++) {
      if (i == deleteRows) {
        offSetRow = 1;
      }
      offSetCol = 0;
      for (int j = 0; j < matrix.getMatrix().length - 1; j++) {
        if (j == deleteColumns) {
          offSetCol = 1;
        }
        arrayNewMatrix[i][j] = matrix.getMatrix()[i + offSetRow][j + offSetCol];
      }
    }
    return new Matrix(arrayNewMatrix);
  }

  /**
   * Метод, который вычисляет определитель матрицы.
   *
   * @param matrix - матрица, чей определитель хотим вычислить
   * @return - определитель матрицы
   */
  public static double matrixDet(@NotNull Matrix matrix) {
    double det = 0;
    int degree = 1;

    if (matrix.getMatrix().length != matrix.getMatrix()[0].length) {
      System.out.println("Матрица не квадратная!");
      return 0;
    }

    if (matrix.getMatrix().length == 1) {
      return matrix.getMatrix()[0][0];
    } else if (matrix.getMatrix().length == 2) {
      return matrix.getMatrix()[0][0] * matrix.getMatrix()[1][1]
          - matrix.getMatrix()[0][1] * matrix.getMatrix()[1][0];
    } else {
      Matrix newMatrix = new Matrix(matrix.getMatrix());

      for (int j = 0; j < matrix.getMatrix().length; j++) {
        newMatrix = matrixRowsAndColumnsMinus1(matrix, 0, j);

        det = det + (degree * matrix.getMatrix()[0][j] * matrixDet(newMatrix));
        degree = -degree;
      }
    }
    return det;
  }

  /**
   * Метод, который приводит матрицу к единично - диагональной (не нужен)
   *
   * @param matrix - матрица
   * @return - матрица
   */
  public static Matrix matrixTriangular(Matrix matrix) {

    double temp = 0;

    Matrix newMatrix = new Matrix(matrix.getMatrix());
    for (int i = 0; i < matrix.getMatrix().length; i++) {
      for (int j = 0; j < matrix.getMatrix().length; j++) {
        newMatrix.getMatrix()[i][j] = 0;
        if (i == j) {
          newMatrix.getMatrix()[i][j] = 1;
        }
      }
    }
    for (int k = 0; k < matrix.getMatrix().length; k++) {
      temp = matrix.getMatrix()[k][k];
      for (int j = 0; j < matrix.getMatrix().length; j++) {
        matrix.getMatrix()[k][j] /= temp;
        newMatrix.getMatrix()[k][j] /= temp;
      }
      for (int i = k + 1; i < matrix.getMatrix().length; i++) {
        temp = matrix.getMatrix()[i][k];
        for (int j = 0; j < matrix.getMatrix().length; j++) {
          matrix.getMatrix()[i][j] -= matrix.getMatrix()[k][j] * temp;
          newMatrix.getMatrix()[i][j] -= newMatrix.getMatrix()[k][j] * temp;
        }
      }
    }
    for (int k = matrix.getMatrix().length - 1; k > 0; k--) {
      for (int i = k - 1; i >= 0; i--) {
        temp = matrix.getMatrix()[i][k];
        for (int j = 0; j < matrix.getMatrix().length; j++) {
          matrix.getMatrix()[i][j] -= matrix.getMatrix()[k][j] * temp;
          newMatrix.getMatrix()[i][j] -= newMatrix.getMatrix()[k][j] * temp;
        }
      }
    }
    for (int i = 0; i < matrix.getMatrix().length; i++) {
      for (int j = 0; j < matrix.getMatrix().length; j++) {
        matrix.getMatrix()[i][j] = newMatrix.getMatrix()[i][j];
      }
    }
    return newMatrix;
  }


  /**
   * Метод, который вычисляет обратную матрицу c помощью определителя
   *
   * @param matrix - матрица, которую хотим инвертировать
   * @return - обратная матрица
   */
  public static Matrix matrixInverseDeterminate(Matrix matrix) {
    int length = matrix.getMatrix().length;
    double[][] inverseArr = new double[length][length];
    Matrix inverse = new Matrix(inverseArr);
    for (int i = 0; i < matrix.getMatrix().length; i++) {
      for (int j = 0; j < matrix.getMatrix()[i].length; j++) {
        inverse.getMatrix()[i][j] =
            Math.pow(-1, i + j) * matrixDet(matrixRowsAndColumnsMinus1(matrix, i, j));
      }
    }
    double det = 1.0 / matrixDet(matrix);
    for (int i = 0; i < inverse.getMatrix().length; i++) {
      for (int j = 0; j <= i; j++) {
        double temp = inverse.getMatrix()[i][j];
        inverse.getMatrix()[i][j] = inverse.getMatrix()[j][i] * det;
        inverse.getMatrix()[j][i] = temp * det;
      }
    }
    return inverse;
  }

  /**
   * Метод для вывода матрицы в консоль.
   *
   * @param matrix - матрица, которую хотим вывести
   */
  public static void matrixPrint(Matrix matrix) {
    for (int i = 0; i < matrix.getMatrix().length; i++) {
      System.out.print("[");
      for (int j = 0; j < matrix.getMatrix()[i].length; j++) {
        String str1 = String.format("%.8e", matrix.getMatrix()[i][j]);
        System.out.print(str1);
        if (j != matrix.getMatrix()[i].length - 1) {
          System.out.print("     ");
        }
      }
      System.out.println("]");
    }
  }

  /**
   * Метод, который вычисляет бесконечную норму матрицы
   *
   * @param matrix - матрица
   * @return - норма матрицы
   */
  public static double matrixNorma(Matrix matrix) {
    double norma = 0;
    for (int i = 0; i < matrix.getMatrix().length; i++) {
      double sum = 0;
      for (int j = 0; j < matrix.getMatrix()[0].length; j++) {
        sum = sum + Math.abs(matrix.getMatrix()[i][j]);
      }
      if (sum > norma) {
        norma = sum;
      }
    }
    return norma;
  }

  /**
   * Вычисляет число обусловленности матрицы
   *
   * @param matrix - матрица
   * @return - число обусловленности
   */
  public static double matrixCond(Matrix matrix) {
    double normaInverseMatrix = matrixNorma(matrix.getInverseMatrix(matrix));
    double normaMatrix = matrixNorma(matrix);
    return normaInverseMatrix * normaMatrix;
  }

  /**
   * Разбивает систему Ах=B, на матрицу А и матрицу В
   *
   * @param matrix - расширенную матрциу
   * @return - матриц А и матрица В
   */
  public List<Matrix> splitMatrix(Matrix matrix) {
    double[][] firstMatrixArr = new double[matrix.rows][matrix.columns - 1];
    double[][] secondMatrixArr = new double[matrix.rows][1];
    for (int i = 0; i < matrix.rows; i++) {
      for (int j = 0; j < matrix.columns - 1; j++) {
        firstMatrixArr[i][j] = matrix.getMatrix()[i][j];
      }
      secondMatrixArr[i][0] = matrix.getMatrix()[i][matrix.columns - 1];
    }
    Matrix first = new Matrix(firstMatrixArr);
    Matrix second = new Matrix(secondMatrixArr);
    List<Matrix> listMatrix = new ArrayList<>();
    listMatrix.add(first);
    listMatrix.add(second);
    return listMatrix;
  }

  /**
   * Метод, который берет стобец матрицы и делает из него объект класса Vector
   *
   * @param columnIndex - номер столбца
   * @return - вектор
   */
  public Vector getColumnAsVector(int columnIndex) {
    if (columnIndex < 0 || columnIndex >= columns) {
      throw new IllegalArgumentException("Column index out of range");
    }
    double[] columnData = new double[rows];
    for (int i = 0; i < rows; i++) {
      columnData[i] = matrix[i][columnIndex];
    }
    return new Vector(columnData);
  }

  /**
   * Умножает матрицу справа на вектор.
   *
   * @param vector - вектор, на который умножается матрица
   * @return Результат умножения в виде вектора
   */
  public Vector multiplyByVector(Vector vector) {
    if (columns != vector.getSize()) {
      throw new IllegalArgumentException(
          "Количество столбцов матрицы должно быть равно размеру вектора");
    }

    double[] result = new double[rows];

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        result[i] += this.matrix[i][j] * vector.get(j);
      }
    }

    return new Vector(result);
  }

  /**
   * Умножает эту матрицу на другую матрицу.
   *
   * @param other - другая матрица
   * @return Результат умножения в виде новой матрицы
   */
  public Matrix multiply(Matrix other) {
    if (this.columns != other.rows) {
      throw new IllegalArgumentException(
          "Количество столбцов первой матрицы должно быть равно количеству строк второй матрицы");
    }

    double[][] result = new double[this.rows][other.columns];

    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < other.columns; j++) {
        for (int k = 0; k < this.columns; k++) {
          result[i][j] += this.matrix[i][k] * other.matrix[k][j];
        }
      }
    }

    return new Matrix(result);
  }

  /**
   * Метод, который вычисляет спектральный радиус квадратной матрицы
   *
   * @return - максимальное абсолютное значение собственных значений матрицы.
   */
  public double spectralRadius() {
    RealMatrix realMatrix = MatrixUtils.createRealMatrix(this.matrix);
    EigenDecomposition eigenDecomposition = new EigenDecomposition(realMatrix);
    double[] realEigenvalues = eigenDecomposition.getRealEigenvalues();
    double maxEigenvalue = Arrays.stream(realEigenvalues).map(Math::abs).max().orElse(0.0);
    long count = Arrays.stream(realEigenvalues).map(Math::abs).filter(val -> val == maxEigenvalue)
        .count();
    if (count > 1) {
      System.out.println("Максимальное по модулю собственное число матрицы не единственно.");
    } else {
      System.out.println(
          "Максимально по модулю собственное число матрицы единственно. Значит, можно применить метод Люстерника.");
    }
    return maxEigenvalue;
  }

  /**
   * Метод, который вычисляет спектральный радиус квадратной матрицы
   *
   * @return - true or false.
   */
  public double spectralRadiusSeid() {
    RealMatrix realMatrix = MatrixUtils.createRealMatrix(this.matrix);
    EigenDecomposition eigenDecomposition = new EigenDecomposition(realMatrix);
    double[] realEigenvalues = eigenDecomposition.getRealEigenvalues();

    return Arrays.stream(realEigenvalues).map(Math::abs).max().orElse(0.0);
  }

  /**
   * Делит матрицу на верхне-треугольную и нижне-треугольную по главной диагонали. Главная диагональ
   * достаётся верхней части
   *
   * @return верхне и нижне диагональные матрицы
   */
  public List<Matrix> divideToLeftRight() {
    double[][] hLeft = new double[rows][rows];
    double[][] hRight = new double[rows][rows];
    List<Matrix> listTwoMatrix = new ArrayList<>(2);

    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < rows; j++) {
        if (i < j) {
          hLeft[i][j] = this.matrix[i][j];
          hRight[i][j] = 0;
        } else {
          hLeft[i][j] = 0;
          hRight[i][j] = this.matrix[i][j];
        }
      }
    }
    listTwoMatrix.add(new Matrix(hLeft));
    listTwoMatrix.add(new Matrix(hRight));
    return listTwoMatrix;
  }

  /**
   * Вычисляет разность между данной матрицей и матрицей matrix.
   *
   * @param matrix матрица для вычитания
   * @return разность матриц
   */
  public Matrix diff(Matrix matrix) {
    double[][] sumMatrix = new double[rows][columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        sumMatrix[i][j] = this.matrix[i][j] - matrix.getElemMatrix(i, j);
      }
    }
    return new Matrix(sumMatrix);
  }

  /**
   * Создаёт единичную матрицу.
   *
   * @param size размер
   * @return единичная матрица
   */
  public static Matrix makeUnitMatrix(int size) {
    double[][] result = new double[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        if (i == j) {
          result[i][j] = 1;
        } else {
          result[i][j] = 0;
        }
      }
    }
    return new Matrix(result);
  }

  /**
   * Поиск индексов элемента матрицы, который имеет максимальное по модулю значение среди всех
   * элементов матрицы, находящихся выше главной диагонали.
   *
   * @param matrix - матрица
   * @return - индексы
   */
  public List<Integer> findIkJk(double[][] matrix) {
    if (matrix == null || matrix.length == 0) {
      throw new IllegalArgumentException("Matrix cannot be null or empty");
    }
    int iMax = -1;
    int jMax = -1;
    double maxElem = -1;
    double tmp;
    int size = matrix.length;
    List<Integer> res = Arrays.asList(-1, -1);
    for (int i = 0; i < size; i++) {
      for (int j = i + 1; j < size; j++) {
        tmp = Math.abs(matrix[i][j]);
        if (tmp > maxElem) {
          maxElem = tmp;
          iMax = i;
          jMax = j;
          res.set(0, iMax);
          res.set(1, jMax);
        }
      }
    }
    return res;
  }

  /**
   * Получить столбец матрицы.
   *
   * @param column индекс столбца
   * @return столбец матрицы
   */
  public Vector getColumn(int column) {
    double[] vector = new double[rows];
    for (int i = 0; i < rows; i++) {
      vector[i] = matrix[i][column];
    }
    return new Vector(vector);
  }

  public void setColumn(int column, Vector eigenVector) {
    for (int i = 0; i < rows; i++) {
      matrix[i][column] = eigenVector.get(i);
    }
  }

  /**
   * Умножает двумерный массив на скаляр.
   *
   * @param scalar скаляр
   * @return произведение двумерного массива на скаляр
   */
  public Matrix mul(double scalar) {
    int rows = matrix.length;
    int columns = matrix[0].length;
    double[][] result = new double[rows][columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        result[i][j] = scalar * matrix[i][j];
      }
    }
    return new Matrix(result);
  }

  /**
   * Метод скалярный произведений
   *
   * @param epsilon - точность
   * @return - собственное число и вектор
   */
  public PowerAndScalarMethodResult scalarProductMethod1(double epsilon){
    Matrix matrixA = new Matrix(matrix);
    int k = 0;
    double maxEigenValue = 0;
    CalculationEigenValuesAndVectorsMatrix new1 = new CalculationEigenValuesAndVectorsMatrix(matrixA);
    MethodJacobiResult valVec = new1.methodJacobi(1.e-6);
    Matrix eigenVectors = valVec.eigenvectorMatrix();
    Vector prev;
    Vector vectorMaxEigenValue = Vector.getZeroVector(matrix.length);
    for (int i = 0; i < matrix.length; i++) {
      vectorMaxEigenValue = vectorMaxEigenValue.vecSum(eigenVectors.getColumn(i));
    }

    do{
      k++;
      prev = vectorMaxEigenValue;
      vectorMaxEigenValue = matrixA.multiplyByVector(vectorMaxEigenValue);
      maxEigenValue = vectorMaxEigenValue.multiplyScalar(prev) / prev.multiplyScalar(prev);
    } while (new1.posterioriForEigen(maxEigenValue, vectorMaxEigenValue) >= epsilon);
    vectorMaxEigenValue.normalize();

    return new PowerAndScalarMethodResult(maxEigenValue, vectorMaxEigenValue, k);
  }

  public void set(double elem, int i, int j) {
    matrix[i][j] = elem;
  }

  public double getElemMatrix(int i, int j) {
    return matrix[i][j];
  }

  public int getRows() {
    return rows;
  }

  public void setRows(int rows) {
    this.rows = rows;
  }

  public int getColumns() {
    return columns;
  }

  public void setColumns(int columns) {
    this.columns = columns;
  }

  public double[][] getMatrix() {
    return matrix;
  }

  public void setMatrix(double[][] matrix) {
    this.matrix = matrix;
  }
}
