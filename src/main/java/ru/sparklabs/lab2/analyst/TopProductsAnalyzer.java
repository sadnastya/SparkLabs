package ru.sparklabs.lab2.analyst;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import ru.sparklabs.lab2.common.SalesSchema;

/**
 * Роль: дата-аналитик. Самые популярные товары (п. 2 задания).
 */
public interface TopProductsAnalyzer {

    /** Колонки результата. */
    String STOCK_CODE = SalesSchema.STOCK_CODE;
    String DESCRIPTION = SalesSchema.DESCRIPTION;
    /** Сумма Quantity по товару. */
    String TOTAL_QUANTITY = "TotalQuantity";

    /**
     * Строит топ товаров по общему количеству проданных единиц.
     *
     * @param sales очищенные продажи в схеме {@link SalesSchema#SALES}
     * @param limit сколько товаров вернуть, по заданию 5
     * @return не более {@code limit} строк, по одной на StockCode, с колонками
     *         {@link #STOCK_CODE}, {@link #DESCRIPTION}, {@link #TOTAL_QUANTITY},
     *         отсортированные по {@link #TOTAL_QUANTITY} по убыванию
     */
    Dataset<Row> topProducts(Dataset<Row> sales, int limit);
}
