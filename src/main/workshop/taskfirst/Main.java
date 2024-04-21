package taskfirst;

public class Main {

  static final String STRING = "====================================================================================================";

  public static void main(String[] args) {
    System.out.println("ЗАДАЧА 1");
    System.out.println("Решить систему Ах=В линейных уравнений 3 прямыми методами:");
    System.out.println("1. Методом Гаусса единственного деления");
    System.out.println("2. Методом Гаусса с выбором главного элемента");
    System.out.println("3. Методом LU-разложения");
    System.out.println(
        "Так же реализовать методы, позволяющие найти обратную матрицу и число обусловленности.");
    System.out.println(STRING);
    double[][] matrixArr1 = {
        {10.62483, 1.15527, -2.97153},
        {1.15527, 9.30891, 0.69138},
        {-2.97153, 0.69138, 4.79937}};
    double[] vectorArr1 = {8.71670, 5.14441, 0.27384};
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
        {10.62483, 1.15527, -2.97153},
        {1.15527, 9.30891, 0.69138},
        {-2.97153, 0.69138, 4.79937}};
    double[] vectorArr2 = {8.71670, 5.14441, 0.27384};
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
        {10.62483, 1.15527, -2.97153},
        {1.15527, 9.30891, 0.69138},
        {-2.97153, 0.69138, 4.79937}};
    double[] vectorArr3 = {8.71670, 5.14441, 0.27384};
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
        {10.62483, 1.15527, -2.97153},
        {1.15527, 9.30891, 0.69138},
        {-2.97153, 0.69138, 4.79937}};
    double[] vectorArr4 = {8.71670, 5.14441, 0.27384};
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
    System.out.println(STRING);
    System.out.println("Решение системы Cx=B, где матрица C отличается от А одним элементом: \n");
    double[][] matrixArr5 = {
        {10.62483, 1.15527, -2.97153},
        {1.15527, 9.30891, 0.69138},
        {-2.97153, 0.69138, 4.79937}};
    double[][] matrixArr6 = {
        {10.62483, 1.15527, -2.97153},
        {1.15527, 9.30891, 0.69138},
        {-2.97153, 0.69138, 4.79937}};
    double[] vectorArr5 = {8.71670, 5.14441, 0.27384};
    Matrix matrix5 = new Matrix(matrixArr5);
    Matrix matrix6 = new Matrix(matrixArr6);
    Vector vector5 = new Vector(vectorArr5);
    matrix5.set(Math.pow(10, -8) * matrix6.getElemMatrix(0, 0), 0, 0);
    Matrix.matrixPrint(matrix5.getExtendedMatrix(matrix5, vector5));
    System.out.println(STRING);
    SolvingLinearSystems solvingLinearSystems5 = new SolvingLinearSystems(matrix5, vector5, 1.E-3);
    Vector vecRes5 = solvingLinearSystems5.solutionGauss(matrix5, vector5);
    Vector veсtorResult6 = solvingLinearSystems5.solutionGaussElemSelec(matrix5, vector5);
    System.out.println("Результат метода ГАУССА С ЕДИНСТВЕННЫМ ДЕЛЕНИЕМ: \n");
    Vector.vectorPrint(vecRes5.getVectorArr());
    System.out.println("\nНевязка: \n");
    Vector vecX = matrix5.multiplyByVector(vecRes5);
    Vector res =  vector5.subtract(vecX);
    Vector.vectorPrint(res.getVectorArr());
    System.out.println(STRING);
    System.out.println("Результат метода ГАУССА С ВЫБОРОМ ГЛАВНОГО ЭЛЕМЕНТА: \n");
    Vector.vectorPrint(veсtorResult6.getVectorArr());
    System.out.println("\nНевязка: \n");
    Vector vecX1 = matrix5.multiplyByVector(veсtorResult6);
    Vector res1 =  vector5.subtract(vecX1);
    Vector.vectorPrint(res1.getVectorArr());
  }
}

