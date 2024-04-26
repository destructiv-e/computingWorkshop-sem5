package taskthird;

import taskfirst.Vector;

/**
 * Метод для сохранения результата нахождения противоположного элемента спектра
 *
 * @param eigenVal - собственное число
 * @param eigenVec - собственный вектор
 */
public record FindingOppositeEndSpectrumResult(double eigenVal, Vector eigenVec) {

}
