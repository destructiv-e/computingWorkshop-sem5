package taskfirst;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс Вектор
 */
public class Vector {

  private int size;
  private double[] vectorArr;

  /**
   * Конструктор класса
   */
  public Vector() {
    this.vectorArr = new double[0];
    this.size = 0;
  }

  public Vector(int size) {
    this.size = size;
    this.vectorArr = new double[size];
  }

  public Vector(double[] vectorArr) {
    this.vectorArr = vectorArr;
    this.size = vectorArr.length;
  }

  /**
   * Создаёт нулевой вектор заданной размерности
   *
   * @param size - размерность вектора
   * @return - нулевой вектор
   */
  public static Vector getZeroVector(int size) {
    double[] zeroVector = new double[size];
    for (int i = 0; i < zeroVector.length; i++) {
      zeroVector[i] = 0;
    }
    return new Vector(zeroVector);
  }

  /**
   * Создаёт List векторов, такой что у i-го вектора на i-том месте стоит единица, а все остальные
   * элеменнты нули
   *
   * @param size - размерность векторов
   * @return - List векторов
   */
  public static List<Vector> getListUnitVector(int size) {
    List<Vector> listUnitVector = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Vector vectorZero = getZeroVector(size);
      vectorZero.setVector(i, 1);
      listUnitVector.add(vectorZero);
    }
    return listUnitVector;
  }

  /**
   * Метод для вывода вектора в консоль
   */
  public static void vectorPrint(double[] array) {
    for (double v : array) {
      String str1 = String.format("[%.8e", v);
      System.out.print(str1 + "]\n");
    }
  }

  /**
   * Вычисляет разность между этим вектором и вектором-аргументом.
   *
   * @param other - вектор, который вычитается из этого вектора
   * @return Вектор, представляющий разность между двумя векторами
   */
  public Vector subtract(Vector other) {
    if (this.size != other.getSize()) {
      throw new IllegalArgumentException("Размеры векторов должны быть одинаковыми для вычитания");
    }

    double[] result = new double[this.size];

    for (int i = 0; i < this.size; i++) {
      result[i] = this.vectorArr[i] - other.get(i);
    }

    return new Vector(result);
  }

  /**
   * Вычисляет бесконечную норму
   *
   * @return - значение нормы
   */
  public double calcVectorInfNorm() {
    double max = 0;
    for (int i = 0; i < vectorArr.length; i++) {
      if (Math.abs(vectorArr[i]) > Math.abs(max)) {
        max = Math.abs(vectorArr[i]);
      }
    }
    return max;
  }

  /**
   * Вычисляет вторую норму вектора
   *
   * @return - значение нормы
   */
  public double calcVectorTwoNorm() {
    double sum = 0;
    for (int i = 0; i < vectorArr.length; i++) {
      sum = sum + Math.pow(vectorArr[i], 2);
    }
    return Math.pow(sum, 0.5);
  }


  /**
   * Суммирует два вектора
   *
   * @param vector - вектор, который хотим прибавить
   * @return - вектор
   */
  public Vector vecSum(Vector vector) {
    Vector res = new Vector(this.size);
    for (int i = 0; i < this.vectorArr.length; i++) {
      res.getVectorArr()[i] = this.vectorArr[i] + vector.getVectorArr()[i];
    }
    return res;
  }

  /**
   * Умножает вектор на число
   *
   * @param a - число на которое хотим умножить
   * @return - вектор
   */
  public Vector vecMultiplicationByNumber(double a) {
    Vector res = new Vector(this.size);
    for (int i = 0; i < this.vectorArr.length; i++) {
      res.getVectorArr()[i] = this.vectorArr[i] * a;
    }
    return res;
  }

  /**
   * Умножение двух векторов
   *
   * @param vector -
   * @return - вектор
   */
  public Vector vecMultiplicationByVector(Vector vector) {
    Vector res = new Vector(this.size);
    for (int i = 0; i < this.vectorArr.length; i++) {
      res.getVectorArr()[i] = this.vectorArr[i] * vector.getVectorArr()[i];
    }
    return res;
  }

  /**
   * Деление вектора на число
   *
   * @param a -
   * @return -
   */
  public Vector vecDivisionByNumber(double a) {
    Vector res = new Vector(this.size);
    for (int i = 0; i < this.vectorArr.length; i++) {
      res.getVectorArr()[i] = this.vectorArr[i] / a;
    }
    return res;
  }

  public double multiplyScalar(Vector prev) {
    double sum = 0;
    for (int i = 0; i < size; i++) {
      sum += vectorArr[i] * prev.get(i);
    }
    return sum;
  }

  public void normalize(){
    double norm = this.calcVectorTwoNorm();
    for (int i = 0; i < size; i++) {
      vectorArr[i] = vectorArr[i] / norm;
    }
  }

  public void setVector(int i, double elem) {
    this.vectorArr[i] = elem;
  }

  public double[] getVectorArr() {
    return vectorArr;
  }

  public void setVectorArr(double[] vectorArr) {
    this.vectorArr = vectorArr;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public double get(int index) {
    return vectorArr[index];
  }
}
