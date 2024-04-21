package tasksecond;

import java.util.List;
import taskfirst.Matrix;
import taskfirst.SolvingLinearSystems;
import taskfirst.Vector;

public class MainSecondTask {

  static final String STRING = "====================================================================================================";
  static final double EPSILON = 0.001;

  public static void main(String[] args) {
    System.out.println("ЗАДАЧА 2");
    System.out.println("Ax = b");
    System.out.println("1. Найти решение методом Гаусса.");
    System.out.println("2. Преобразовать систему к виду x=Hx+g. Вычислить норму матрицы H.");
    System.out.println("3. Найти априорную оценку того k, при котором ||x*-x_k|| < Epsilon = " + EPSILON);
    System.out.println("4. Вычислить решение методом простой итерации с точностью " + EPSILON + "." );
    System.out.println("Вывести число итераций, фактическую погрешность, апостериорную и априорную оценку.");
    System.out.println("Уточнить последнее приближение по Люстернику и вывести его фактическую погрешность.");
    System.out.println("5. Вычислить решение системы методом Зейделя с точностью " + EPSILON + ".");
    System.out.println("6. Вычислить H_seid = (E - H_l)^(-1) * H_r - матрица перехода и определить её спектральный радиус.");
    System.out.println("7.  Вычислить решение методом верхней релаксации с точностью " + EPSILON + ".");
    System.out.println(STRING);
    Matrix matrixA = new Matrix(new double[][]{
        {10.62483, 1.15527, -2.97153},
        {1.15527, 9.30891, 0.69138},
        {-2.97153, 0.69138, 4.79937}});
    Vector vectorB = new Vector(new double[]{8.71670, 5.14441, 0.27384});
    System.out.println("Матрица A: ");
    Matrix.matrixPrint(matrixA);
    System.out.println();
    System.out.println("Вектор b: ");
    Vector.vectorPrint(vectorB.getVectorArr());
    System.out.println(STRING);
    System.out.println("Решение по схеме ГАУССА С ВЫБОРОМ ГЛАВНОГО ЭЛЕМЕНТА ПО СТОЛБЦУ: ");
    SolvingLinearSystems solvingLinearSystems3 = new SolvingLinearSystems(matrixA, vectorB, 1.E-3);
    Vector resultGauss = solvingLinearSystems3.solutionGaussElemSelec(matrixA, vectorB);
    Vector.vectorPrint(resultGauss.getVectorArr());
    System.out.println(STRING);
    IterativeSolvingLinearSystems isls = new IterativeSolvingLinearSystems(matrixA, vectorB);
    System.out.println("Матрица H: ");
    Matrix.matrixPrint(isls.getMatrixH());
    System.out.println();
    System.out.println("Вектор G: ");
    Vector.vectorPrint(isls.getVectorG().getVectorArr());
    System.out.println(STRING);
    List<Double> resLis = isls.IteratOfAprioriAssesLessEpsilon(EPSILON);
    Matrix matrixH = isls.getMatrixH();
    double normaMatrixH = Matrix.matrixNorma(matrixH);
    System.out.printf("Норма матрицы Н:\t%f%n", normaMatrixH);
    System.out.println("k при которой априорная оценка ||x* - x_k|| < "+ EPSILON + ": " + resLis.get(0));
    System.out.println(STRING);
    System.out.println("Решение системы МЕТОДОМ ПРОСТОЙ ИТЕРАЦИИ (БЕЗ УТОЧНЕНИЯ ПО ЛЮСТЕРНИКУ): ");
    IterationMethodsResult resSimpleIter = isls.solveSimpleIteration(EPSILON, false);
    Vector.vectorPrint(resSimpleIter.solution().getVectorArr());
    System.out.println();
    System.out.println("Число итераций: " + resSimpleIter.iterations());
    System.out.printf("Апостериорная оценка:\t%f%n", resSimpleIter.error());
    System.out.printf("Априорная оценка:\t%f%n", isls.caclApriorAssessment(
        resSimpleIter.iterations()));
    System.out.printf("Фактическая погрешность:\t%f%n", resultGauss.subtract(resSimpleIter.solution()).calcVectorInfNorm());
    System.out.println(STRING);
    System.out.println("Решение системы МЕТОДОМ ПРОСТОЙ ИТЕРАЦИИ (С УТОЧНЕНИЕМ ПО ЛЮСТЕРНИКУ): ");
    IterationMethodsResult resSimpleIterLus = isls.solveSimpleIteration(EPSILON, true);
    Vector.vectorPrint(resSimpleIterLus.solution().getVectorArr());
    System.out.println();
    System.out.printf("Фактическая погрешность:\t%f%n", resultGauss.subtract(resSimpleIterLus.solution()).calcVectorInfNorm());
    System.out.println(STRING);
    IterationMethodsResult resSeid = isls.solveSeidel(EPSILON);
    System.out.println("Решение системы МЕТОДОМ ЗЕЙДЕЛЯ: ");
    Vector.vectorPrint(resSeid.solution().getVectorArr());
    System.out.println();
    System.out.println("Число итераций: " + resSeid.iterations());
    System.out.printf("Апостериорная оценка:\t%f%n", resSeid.error());
    System.out.printf("Априорная оценка:\t%f%n", isls.caclApriorAssessment(
        resSeid.iterations()));
    System.out.printf("Фактическая погрешность:\t%f%n", resultGauss.subtract(resSeid.solution()).calcVectorInfNorm());
    System.out.println(STRING);
    System.out.println("Матрица перехода H_seid: ");
    Matrix.matrixPrint(isls.calcSeidelMatrix());
    System.out.println();
    System.out.printf("Спектральный радиус матрицы перехода:\t%f%n", isls.calcSeidelMatrix().spectralRadiusSeid());
    System.out.println(STRING);
    System.out.println("Решение системы МЕТОДОМ ВЕРХНЕЙ РЕЛАКСАЦИИ: ");
    IterationMethodsResult resUpRelax = isls.solveUpperRelaxation(EPSILON);
    Vector.vectorPrint(resUpRelax.solution().getVectorArr());
    System.out.println();
    System.out.println("Число итераций: " + resUpRelax.iterations());
    System.out.printf("Апостериорная оценка:\t%f%n", resUpRelax.error());
    System.out.printf("Априорная оценка:\t%f%n", isls.caclApriorAssessment(
        resUpRelax.iterations()));
    System.out.printf("Фактическая погрешность:\t%f%n", resultGauss.subtract(resUpRelax.solution()).calcVectorInfNorm());
    System.out.println(STRING);
  }
}
