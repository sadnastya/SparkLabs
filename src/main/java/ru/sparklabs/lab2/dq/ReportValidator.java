package ru.sparklabs.lab2.dq;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.sparklabs.lab2.analyst.CustomerStatsAnalyzer;
import ru.sparklabs.lab2.analyst.TopProductsAnalyzer;

/**
 * Роль: DQ. Проверка отчётов аналитика перед выгрузкой.
 */
public interface ReportValidator {

    /**
     * Проверяет результат {@link TopProductsAnalyzer#topProducts}: строк не больше
     * {@code limit}, StockCode не повторяются, сортировка по убыванию TotalQuantity.
     *
     * @param topProducts отчёт аналитика
     * @param limit       ожидаемый размер топа
     */
    DqReport validateTopProducts(Dataset<Row> topProducts, int limit);

    /**
     * Проверяет результат {@link CustomerStatsAnalyzer#customerStats} и сверяет его
     * с исходными данными: CustomerID уникален и не null, OrdersCount &gt; 0,
     * AvgCheck = TotalSpent / OrdersCount, сумма TotalSpent по всем клиентам равна
     * сумме TotalPrice по строкам с CustomerID.
     *
     * @param customerStats отчёт аналитика
     * @param cleanSales    очищенные продажи, по которым он построен
     */
    DqReport validateCustomerStats(Dataset<Row> customerStats, Dataset<Row> cleanSales);
}
