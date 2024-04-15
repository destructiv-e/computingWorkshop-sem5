package taskfirst;

public class Main {

  static final String STRING = "====================================================================================================";

  public static void main(String[] args) {
    System.out.println("ЗАДАЧА 1");
    System.out.println("Решить систему Ах=В линейных уравнений 3 способами:");
    System.out.println("1. Методом Гаусса единственного деления");
    System.out.println("2. Методом Гаусса с выбором главного элемента");
    System.out.println("3. Методом LU-разложения");
    System.out.println(
        "Так же реализовать методы, позволяющие найти обратную матрицу и число обусловленности.");
    System.out.println(STRING);
    double[][] matrixArr1 = {
        {8.29381, 0.995516, -0.560617},
        {0.995516, 6.298198, 0.595772},
        {-0.560617, 0.595772, 4.997407}};
    double[] vectorArr1 = {0.766522, 3.844422, 5.239231};
    Matrix matrix1 = new Matrix(matrixArr1);
    Vector vector1 = new Vector(vectorArr1);
    System.out.println("МАТРИЦА А с которой будем работать: \n");
    Matrix.matrixPrint(matrix1);
    System.out.println(STRING);
    System.out.println("ВЕКТОР В: \n");
    Vector.vectorPrint(vector1.getVectorArr());
    System.out.println(STRING);
    System.out.println("РАСШИРЕННАЯ МАТРИЦА: \n");
    Matrix extendedMatrix = matrix1.getExtendedMatrix(matrix1, vector1);
    Matrix.matrixPrint(extendedMatrix);
    System.out.println(STRING);
    Matrix inverseMatrix = matrix1.getInverseMatrix(matrix1);
    System.out.println("ОБРАТНАЯ МАТРИЦА: \n");
    Matrix.matrixPrint(inverseMatrix);
    System.out.println("\nРезультат умножения матрицы A на обратную матрицу:\n");
    Matrix matrixMul = matrix1.multiply(inverseMatrix);
    Matrix.matrixPrint(matrixMul);
    System.out.println(STRING);
    System.out.printf("НОРМА МАТРИЦЫ: %e%n", Matrix.matrixNorma(matrix1));
    System.out.printf("НОРМА ОБРАТНОЙ МАТРИЦЫ: %e%n", Matrix.matrixNorma(inverseMatrix));
    System.out.printf("ЧИСЛО ОБУСЛОВЛЕННОСТИ: %e%n", Matrix.matrixCond(matrix1));
    System.out.println(STRING);
    System.out.println("Решение по схеме ГАУССА ЕДИНСТВЕННОГО ДЕЛАЕНИЯ: \n");
    double[][] matrixArr2 = {
        {8.29381, 0.995516, -0.560617},
        {0.995516, 6.298198, 0.595772},
        {-0.560617, 0.595772, 4.997407}};
    double[] vectorArr2 = {0.766522, 3.844422, 5.239231};
    Matrix matrix2 = new Matrix(matrixArr2);
    Vector vector2 = new Vector(vectorArr2);
    SolvingLinearSystems solvingLinearSystems2 = new SolvingLinearSystems(matrix2, vector2, 1.E-3);
    Vector result2 = solvingLinearSystems2.solutionGauss(matrix2, vector2);
    Vector.vectorPrint(result2.getVectorArr());
    System.out.println();
    System.out.println("ВЕКТОР НЕВЯЗКИ: (A*x - B) \n");
    Vector exactVec2 = matrix2.multiplyByVector(result2);
    Vector residual2 = exactVec2.subtract(vector2);
    Vector.vectorPrint(residual2.getVectorArr());
    System.out.println(STRING);
    System.out.println("Решение по схеме ГАУССА С ВЫБОРОМ ГЛАВНОГО ЭЛЕМЕНТА ПО СТОЛБЦУ: \n");
    double[][] matrixArr3 = {
        {8.29381, 0.995516, -0.560617},
        {0.995516, 6.298198, 0.595772},
        {-0.560617, 0.595772, 4.997407}};
    double[] vectorArr3 = {0.766522, 3.844422, 5.239231};
    Matrix matrix3 = new Matrix(matrixArr3);
    Vector vector3 = new Vector(vectorArr3);
    SolvingLinearSystems solvingLinearSystems3 = new SolvingLinearSystems(matrix3, vector3, 1.E-3);
    Vector result3 = solvingLinearSystems3.solutionGaussElemSelec(matrix3, vector3);
    Vector.vectorPrint(result3.getVectorArr());
    System.out.println();
    System.out.println("ВЕКТОР НЕВЯЗКИ: (A*x - B) \n");
    Vector exactVec3 = matrix3.multiplyByVector(result3);
    Vector residual3 = exactVec3.subtract(vector3);
    Vector.vectorPrint(residual3.getVectorArr());
    System.out.println(STRING);
    System.out.println("Решение ИСПОЛЬЗУЯ LU-разложение: \n");
    double[][] matrixArr4 = {
        {8.29381, 0.995516, -0.560617},
        {0.995516, 6.298198, 0.595772},
        {-0.560617, 0.595772, 4.997407}};
    double[] vectorArr4 = {0.766522, 3.844422, 5.239231};
    Matrix matrix4 = new Matrix(matrixArr4);
    Vector vector4 = new Vector(vectorArr4);
    SolvingLinearSystems solvingLinearSystems4 = new SolvingLinearSystems(matrix4, vector4, 1.E-3);
    Vector result4 = solvingLinearSystems4.solutionLU(matrix4, vector4);
    Vector.vectorPrint(result4.getVectorArr());
    System.out.println();
    System.out.println("ВЕКТОР НЕВЯЗКИ: (A*x - B) \n");
    Vector exactVec4 = matrix4.multiplyByVector(result4);
    Vector residual4 = exactVec4.subtract(vector4);
    Vector.vectorPrint(residual4.getVectorArr());
  }
}

