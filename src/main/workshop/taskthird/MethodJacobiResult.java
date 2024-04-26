package taskthird;

import taskfirst.Matrix;

/**
 * Record-класс, который нужен для сохранения результата метода Якоби
 *
 * @param eigenvalueMatrix - матрица, у которой на главной диагонали собственные числа
 * @param eigenvectorMatrix - матрица, столбцы которой являются собственными векторами
 * @param iterations - количество итераций
 */
public record MethodJacobiResult(Matrix eigenvalueMatrix, Matrix eigenvectorMatrix,
                                 int iterations) {

}
