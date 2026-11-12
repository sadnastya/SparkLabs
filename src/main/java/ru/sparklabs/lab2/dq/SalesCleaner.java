package ru.sparklabs.lab2.dq;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.sparklabs.lab2.common.SalesSchema;

/**
 * Роль: DQ. Очистка данных перед расчётом метрик.
 */
public interface SalesCleaner {

    /**
     * Убирает строки, непригодные для расчёта метрик.
     *
     * <p>Гарантии для аналитика на выходе:
     * <ul>
     *   <li>InvoiceNo, StockCode, Quantity, UnitPrice не null;</li>
     *   <li>нет отмен (InvoiceNo на "C");</li>
     *   <li>Quantity &gt; 0 и UnitPrice &gt; 0;</li>
     *   <li>нет полных дубликатов строк.</li>
     * </ul>
     * Строки без CustomerID остаются: они нужны для топа товаров.
     *
     * @param sales данные от загрузчика в схеме {@link SalesSchema#SALES}
     * @return очищенные данные в той же схеме
     */
    Dataset<Row> clean(Dataset<Row> sales);
}
