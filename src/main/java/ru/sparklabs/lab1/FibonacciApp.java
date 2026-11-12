package ru.sparklabs.lab1;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Вычисление N-го числа Фибоначчи на Apache Spark.
 *
 * <p>Рекуррентность F(n) = F(n-1) + F(n-2) последовательна и напрямую не
 * распараллеливается. Зато она эквивалентна возведению матрицы в степень.
 *
 * <p>Умножение матриц ассоциативно, поэтому произведение n копий M можно считать
 * в любом порядке группировки — это ровно тот контракт, которого требует
 * {@link JavaRDD#reduce}. Spark сворачивает партиции независимо на executors,
 * а затем объединяет частичные результаты на драйвере.
 */
public final class FibonacciApp {

    /** Сколько секунд держать драйвер живым после вывода результата. */
    private static final int HOLD_SECONDS = 120;

    /** Базовая матрица [[1,1],[1,0]] в виде плоского массива [a, b, c, d]. */
    private static final BigInteger[] BASE = {
            BigInteger.ONE, BigInteger.ONE,
            BigInteger.ONE, BigInteger.ZERO
    };

    private FibonacciApp() {
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            System.err.println("Usage: FibonacciApp <n> [partitions]");
            System.exit(1);
        }
        int n = Integer.parseInt(args[0]);
        if (n < 0) {
            System.err.println("n должно быть >= 0, получено: " + n);
            System.exit(1);
        }
        int partitions = args.length > 1 ? Integer.parseInt(args[1]) : 4;


        SparkConf conf = new SparkConf().setAppName("Fibonacci(" + n + ")");

        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            System.out.println("master = " + sc.sc().master()
                    + ", deployMode = " + sc.sc().deployMode());

            long startedAt = System.nanoTime();
            BigInteger result = fibonacci(sc, n, partitions);
            long elapsedMs = (System.nanoTime() - startedAt) / 1_000_000;

            String digits = result.toString();
            System.out.println();
            System.out.println("==========================================================");
            System.out.println(" F(" + n + ") = " + digits);
            System.out.println(" знаков в числе: " + digits.length() + ", время: " + elapsedMs + " мс");
            System.out.println(" Spark UI драйвера: "
                    + (sc.sc().uiWebUrl().isDefined() ? sc.sc().uiWebUrl().get() : "отключён"));
            System.out.println("==========================================================");
            System.out.println();

            // SparkContext открыт фиксированное время: пока он жив,
            // доступен Spark UI драйвера на :4040
            System.out.println("Драйвер держится ещё " + HOLD_SECONDS
                    + " с, Spark UI доступен. Прервать: Ctrl+C.");
            Thread.sleep(HOLD_SECONDS * 1000L);
        }
    }

    /**
     * Считает F(n) как элемент [0][1] матрицы M^n, полученной распределённым reduce.
     */
    static BigInteger fibonacci(JavaSparkContext sc, int n, int partitions) {
        if (n < 2) {
            return BigInteger.valueOf(n);            // F(0) = 0, F(1) = 1
        }

        List<BigInteger[]> factors = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            factors.add(BASE);
        }

        JavaRDD<BigInteger[]> rdd = sc.parallelize(factors, Math.min(partitions, n));
        BigInteger[] power = rdd.reduce(FibonacciApp::multiply);
        return power[1];                             // M^n[0][1] == F(n)
    }

    /** Перемножение матриц 2x2, заданных плоским массивом [a, b, c, d]. */
    static BigInteger[] multiply(BigInteger[] x, BigInteger[] y) {
        return new BigInteger[]{
                x[0].multiply(y[0]).add(x[1].multiply(y[2])),
                x[0].multiply(y[1]).add(x[1].multiply(y[3])),
                x[2].multiply(y[0]).add(x[3].multiply(y[2])),
                x[2].multiply(y[1]).add(x[3].multiply(y[3]))
        };
    }
}
