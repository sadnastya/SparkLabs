package ru.sparklabs.lab2.analyst;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.sparklabs.lab2.common.SalesSchema;

/**
 * Роль: дата-аналитик. Метрики по клиентам (п. 3 задания).
 */
public interface CustomerStatsAnalyzer {

    /** Колонки результата. */
    String CUSTOMER_ID = SalesSchema.CUSTOMER_ID;
    /** Число различных InvoiceNo клиента. */
    String ORDERS_COUNT = "OrdersCount";
    /** Сумма TotalPrice по всем позициям клиента. */
    String TOTAL_SPENT = "TotalSpent";
    /** Средний чек: TotalSpent / OrdersCount. */
    String AVG_CHECK = "AvgCheck";

    /**
     * Считает для каждого клиента число заказов, общую сумму и средний чек.
     * Строки без CustomerID в расчёт не входят: их нельзя отнести к клиенту.
     *
     * @param sales очищенные продажи в схеме {@link SalesSchema#SALES}
     * @return по одной строке на клиента с колонками {@link #CUSTOMER_ID},
     *         {@link #ORDERS_COUNT}, {@link #TOTAL_SPENT}, {@link #AVG_CHECK}
     */
    Dataset<Row> customerStats(Dataset<Row> sales);
}
