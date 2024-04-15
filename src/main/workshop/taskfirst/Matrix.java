package taskfirst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

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
   * Метод, который вычисляет норму матрицы
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
  public List<Matrix> splitMatrix(Matrix matrix){
    double[][] firstMatrixArr = new double[matrix.rows][matrix.columns-1];
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
   * Умножает матрицу на вектор.
   *
   * @param vector - вектор, на который умножается матрица
   * @return Результат умножения в виде вектора
   */
  public Vector multiplyByVector(Vector vector) {
    if (columns != vector.getSize()) {
      throw new IllegalArgumentException("Количество столбцов матрицы должно быть равно размеру вектора");
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
      throw new IllegalArgumentException("Количество столбцов первой матрицы должно быть равно количеству строк второй матрицы");
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
