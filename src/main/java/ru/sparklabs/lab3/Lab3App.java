package ru.sparklabs.lab3;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.count;
import static org.apache.spark.sql.functions.sum;

/**
 * Шаблон Spark-приложения для лабораторной 3.
 *
 * <p>Master и deploy mode здесь намеренно не задаются: их передаёт spark-submit
 * (см. сервис {@code spark-submit} в docker-compose.yml), поэтому один и тот же jar
 * запускается и в {@code local[*]}, и на кластере.
 *
 * <p>Запуск: {@code docker compose run --rm spark-submit}
 */
public final class Lab3App {

    private Lab3App() {
    }

    public static void main(String[] args) {
        // Аргументы приложения — всё, что идёт после пути к jar в spark-submit
        long rows = args.length > 0 ? Long.parseLong(args[0]) : 1_000_000L;

        SparkSession spark = SparkSession.builder()
                .appName("Lab3App")
                .getOrCreate();
        try {
            System.out.println("master = " + spark.sparkContext().master()
                    + ", deployMode = " + spark.sparkContext().deployMode());

            run(spark, rows);
        } finally {
            spark.stop();
        }
    }

    /**
     * Логика лабораторной. Сейчас здесь заглушка-пример, проверяющая, что
     * вычисление действительно уходит на executors: её нужно заменить своим кодом.
     */
    static void run(SparkSession spark, long rows) {
        // TODO lab3: заменить пример ниже своей логикой
        Dataset<Row> result = spark.range(rows)
                .withColumn("bucket", col("id").mod(5))
                .groupBy("bucket")
                .agg(count("*").alias("cnt"), sum("id").alias("sum_id"))
                .orderBy("bucket");

        result.show();
    }
}
