package taskthird;

import taskfirst.Matrix;
import taskfirst.Vector;

public class MainThirdTask {

  static final String STRING = "====================================================================================================";
  static final double EPSILON_BIG = 0.001;
  static final double EPSILON_SMALL = 0.000001;

  public static void main(String[] args) {
    System.out.println("ЗАДАЧА 3-4");
    System.out.println("Проблема собственных значений матрицы");
    System.out.println("1. Найти методом Якоби собственные числа и вектора с точностью " + EPSILON_SMALL);
    System.out.println("2. Найти степенным методом максимальное по модулю собственное число и соответствующий ему вектор с точностью " + EPSILON_BIG);
    System.out.println("3. Найти методом скалярных произведений максимальное по модулю собственное число и соответствующий ему вектор с точностью " + EPSILON_SMALL);
    System.out.println("4. Используя метод скалярных произведений с точностью " + EPSILON_SMALL + " найти противоположнук границу спектра собственных чисел и соответствующий вектор");
    System.out.println("5. Методом Виланта найти собственное число с точностью " + EPSILON_BIG);
    System.out.println("Применить уточнение по Эйткену");
    System.out.println(STRING);
    Matrix matrixA = new Matrix(new double[][]{
        {-0.82005, -0.13542, 0.26948},
        {-0.13542, 0.51486, 0.02706},
        {0.26948, 0.02706, -0.83365}});
    System.out.println("Матрица A ");
    Matrix.matrixPrint(matrixA);
    System.out.println();
    System.out.println(STRING);
    CalculationEigenValuesAndVectorsMatrix cevavm = new CalculationEigenValuesAndVectorsMatrix(matrixA);
    MethodJacobiResult res = cevavm.methodJacobi(1.E-6);
    System.out.println("Матрица собственных чисел найденная МЕТОДОМ ЯКОБИ ");
    Matrix res1 = res.eigenvalueMatrix();
    Matrix.matrixPrint(res1);
    System.out.println();
    System.out.println("Матрица собственных векторов найденная МЕТОДОМ ЯКОБИ ");
    Matrix res2 = res.eigenvectorMatrix();
    Matrix.matrixPrint(res2);
    System.out.println();
    System.out.println("Количество итераций ");
    int k = res.iterations();
    System.out.println(k);
    System.out.println(STRING);
    CalculationEigenValuesAndVectorsMatrix cevavm2 = new CalculationEigenValuesAndVectorsMatrix(matrixA);
    PowerAndScalarMethodResult resPow = cevavm2.powerMethod(1.E-3);
    System.out.println("Максимальное по модулю собственное число найденное СТЕПЕННЫМ МЕТОДОМ");
    double val = resPow.maxEigenval();
    System.out.println(val);
    System.out.println();
    System.out.println("Cобcтвенный вектор найденный СТЕПЕННЫМ МЕТОДОМ");
    Vector vec = resPow.eigenVec();
    Vector.vectorPrint(vec.getVectorArr());
    System.out.println();
    System.out.println("Количество итераций");
    int k1 = resPow.iteration();
    System.out.println(k1);
    System.out.println(STRING);
    CalculationEigenValuesAndVectorsMatrix cevavm3 = new CalculationEigenValuesAndVectorsMatrix(matrixA);
    PowerAndScalarMethodResult resPow3 = cevavm3.scalarProductMethod(1.E-3);
    System.out.println("Максимальное по модулю собственное число найденное МЕТОДОМ СКАЛЯРНЫХ ПРОИЗВЕДЕНИЙ");
    double val1 = resPow3.maxEigenval();
    System.out.println(val1);
    System.out.println();
    System.out.println("Cобcтвенный вектор");
    Vector vec1 = resPow3.eigenVec();
    Vector.vectorPrint(vec1.getVectorArr());
    System.out.println();
    System.out.println("Количество итераций");
    int k11 = resPow3.iteration();
    System.out.println(k11);
    System.out.println(STRING);
    CalculationEigenValuesAndVectorsMatrix spectr = new CalculationEigenValuesAndVectorsMatrix(matrixA);
    FindingOppositeEndSpectrumResult result = spectr.FindingOppositeEndSpectrum(1.E-3);
    System.out.println("Противоположная граница спектра методом скалярных произведений");
    double resss = result.eigenVal();
    System.out.println(resss);
    System.out.println();
    System.out.println("Вектор противоположной границы спектра: ");
    Vector.vectorPrint(result.eigenVec().getVectorArr());
    System.out.println(STRING);
    CalculationEigenValuesAndVectorsMatrix vilant = new CalculationEigenValuesAndVectorsMatrix(matrixA);
    FindingOppositeEndSpectrumResult resVilant = vilant.findEigenValueWielandt(1.E-3, 1, false);
    System.out.println("Уточнённое собственное число МЕТОДОМ ВИЛАНДТА");
    double resVil = resVilant.eigenVal();
    System.out.println(resVil);
    System.out.println();
    System.out.println("Вектор уточнённого собственного числа");
    Vector.vectorPrint(resVilant.eigenVec().getVectorArr());
    System.out.println(STRING);
    FindingOppositeEndSpectrumResult resVilant2 = vilant.findEigenValueWielandt(1.E-3, 1, true);
    System.out.println("Уточнённое по Эйткену собственное число");
    double resVil1 = resVilant2.eigenVal();
    System.out.println(resVil1);
  }
}
