package tasksecond;

import taskfirst.Vector;

/**
 * Record-класс(конструктор + геттеры), который служит для сохранения результата итерационных
 * методов решения линейных систем
 *
 * @param solution   - вектор решениие
 * @param iterations - количество итераций
 * @param error      - апостериорная оценка ошибки
 */
public record IterationMethodsResult(Vector solution, int iterations, double error) {

}
