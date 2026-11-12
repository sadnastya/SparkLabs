package ru.sparklabs.lab2.engineer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/**
 * Роль: дата-инженер. Выгрузка готовых отчётов (пп. 2 и 3 задания).
 */
public interface ReportExporter {

    /**
     * Сохраняет отчёт по указанному пути, перезаписывая предыдущий результат.
     * Формат (CSV с заголовком, Parquet и т.п.) и число файлов выбирает реализация.
     *
     * @param report     готовый отчёт от аналитика
     * @param outputPath каталог назначения
     */
    void export(Dataset<Row> report, String outputPath);
}
