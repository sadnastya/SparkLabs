package ru.sparklabs.lab2;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import ru.sparklabs.lab2.analyst.CustomerStatsAnalyzer;
import ru.sparklabs.lab2.analyst.TopProductsAnalyzer;
import ru.sparklabs.lab2.dq.DqReport;
import ru.sparklabs.lab2.dq.ReportValidator;
import ru.sparklabs.lab2.dq.SalesCleaner;
import ru.sparklabs.lab2.dq.SalesQualityChecker;
import ru.sparklabs.lab2.engineer.ReportExporter;
import ru.sparklabs.lab2.engineer.SalesLoader;

/**
 * Связывает роли в один конвейер. Зависит только от интерфейсов,
 * поэтому каждая роль подставляет свою реализацию независимо от остальных.
 *
 * <pre>
 *  инженер: load -> DQ: check, clean -> аналитик: отчёты -> DQ: validate -> инженер: export
 * </pre>
 */
public final class SalesReportPipeline {

    /** Размер топа товаров по заданию. */
    public static final int TOP_LIMIT = 5;

    private final SalesLoader loader;
    private final SalesQualityChecker checker;
    private final SalesCleaner cleaner;
    private final TopProductsAnalyzer topProducts;
    private final CustomerStatsAnalyzer customerStats;
    private final ReportValidator validator;
    private final ReportExporter exporter;

    public SalesReportPipeline(SalesLoader loader,
                               SalesQualityChecker checker,
                               SalesCleaner cleaner,
                               TopProductsAnalyzer topProducts,
                               CustomerStatsAnalyzer customerStats,
                               ReportValidator validator,
                               ReportExporter exporter) {
        this.loader = loader;
        this.checker = checker;
        this.cleaner = cleaner;
        this.topProducts = topProducts;
        this.customerStats = customerStats;
        this.validator = validator;
        this.exporter = exporter;
    }

    public void run(SparkSession spark, String inputPath, String outputDir) {
        Dataset<Row> raw = loader.load(spark, inputPath);
        requirePassed("исходные данные", checker.check(raw));

        // очищенные данные читают оба отчёта и валидатор
        Dataset<Row> sales = cleaner.clean(raw).cache();

        Dataset<Row> top = topProducts.topProducts(sales, TOP_LIMIT);
        requirePassed("топ товаров", validator.validateTopProducts(top, TOP_LIMIT));
        exporter.export(top, outputDir + "/top_products");

        Dataset<Row> customers = customerStats.customerStats(sales);
        requirePassed("метрики клиентов", validator.validateCustomerStats(customers, sales));
        exporter.export(customers, outputDir + "/customer_stats");
    }

    private static void requirePassed(String stage, DqReport report) {
        System.out.println("DQ [" + stage + "]: " + report);
        if (!report.passed()) {
            throw new IllegalStateException("DQ не пройден на этапе «" + stage + "»");
        }
    }
}
