package taskthird;

import taskfirst.Vector;

/**
 * Record-класс, для сохранения результат степенного метода и метода скалярных произведений
 *
 * @param maxEigenval - максимальное по модулю собственное число
 * @param eigenVec - соответсвтующий собственнный вектор
 * @param iteration - количество итераций
 */
public record PowerAndScalarMethodResult(double maxEigenval, Vector eigenVec, int iteration) {

}
