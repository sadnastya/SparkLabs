package ru.sparklabs.lab2.dq;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.sparklabs.lab2.common.SalesSchema;

/**
 * Роль: DQ. Проверка исходных данных после загрузки.
 */
public interface SalesQualityChecker {

    /**
     * Профилирует продажи и сообщает о нарушениях, данные не меняет.
     *
     * <p>Типичные для этого датасета находки: пустые CustomerID, отмены
     * (InvoiceNo на "C"), Quantity &lt;= 0, UnitPrice &lt;= 0, полные дубликаты строк.
     *
     * @param sales данные от загрузчика в схеме {@link SalesSchema#SALES}
     * @return отчёт; при блокирующих нарушениях конвейер останавливается
     */
    DqReport check(Dataset<Row> sales);
}
