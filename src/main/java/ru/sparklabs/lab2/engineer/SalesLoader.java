package ru.sparklabs.lab2.engineer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import ru.sparklabs.lab2.common.SalesSchema;

/**
 * Роль: дата-инженер. Загрузка исходного CSV (п. 1 задания).
 */
public interface SalesLoader {

    /**
     * Читает CSV с продажами и приводит его к схеме {@link SalesSchema#SALES},
     * включая вычисляемую колонку {@link SalesSchema#TOTAL_PRICE}.
     *
     * <p>Строки не отбрасывает: значения, которые не удалось разобрать,
     * становятся null, а решение о мусорных строках принимает DQ.
     *
     * @param spark активная сессия
     * @param path  путь к CSV-файлу
     * @return все строки файла в схеме {@link SalesSchema#SALES}
     */
    Dataset<Row> load(SparkSession spark, String path);
}
