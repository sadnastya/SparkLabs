package ru.sparklabs.lab2.dq;

import java.util.List;

/**
 * Результат проверок качества данных.
 *
 * @param checks результаты отдельных проверок
 */
public record DqReport(List<Check> checks) {

    /**
     * Результат одной проверки.
     *
     * @param rule       что проверялось, например "Quantity > 0"
     * @param violations сколько строк нарушают правило
     * @param blocking   останавливает ли нарушение конвейер; ожидаемые для датасета
     *                   отклонения, которые затем вычищаются, не блокирующие
     */
    public record Check(String rule, long violations, boolean blocking) {
    }

    /** Нет ни одного блокирующего нарушения. */
    public boolean passed() {
        return checks.stream().noneMatch(c -> c.blocking() && c.violations() > 0);
    }
}
