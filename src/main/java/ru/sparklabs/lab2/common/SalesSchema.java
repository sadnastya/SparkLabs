package ru.sparklabs.lab2.common;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

/**
 * Общий контракт данных для всех ролей: имена и типы колонок датасета продаж.
 *
 * <p>Датасет — Online Retail: транзакции британского онлайн-магазина,
 * одна строка соответствует одной позиции в счёте. Дата-инженер отдаёт данные
 * ровно в схеме {@link #SALES}, аналитик и DQ обращаются к колонкам
 * только через эти константы.
 */
public final class SalesSchema {

    /** Номер счёта, он же заказ. Префикс "C" означает отмену. */
    public static final String INVOICE_NO = "InvoiceNo";
    /** Код товара. */
    public static final String STOCK_CODE = "StockCode";
    /** Название товара; у одного StockCode может встречаться несколько вариантов. */
    public static final String DESCRIPTION = "Description";
    /** Количество единиц в позиции; у отмен отрицательное. */
    public static final String QUANTITY = "Quantity";
    /** Дата и время выставления счёта. */
    public static final String INVOICE_DATE = "InvoiceDate";
    /** Цена за единицу в фунтах стерлингов. */
    public static final String UNIT_PRICE = "UnitPrice";
    /** Идентификатор клиента; бывает пустым. */
    public static final String CUSTOMER_ID = "CustomerID";
    /** Страна клиента. */
    public static final String COUNTRY = "Country";
    /** Вычисляемая колонка: Quantity * UnitPrice. Добавляет загрузчик. */
    public static final String TOTAL_PRICE = "TotalPrice";

    /** Схема данных, которую загрузчик передаёт дальше по конвейеру. */
    public static final StructType SALES = new StructType()
            .add(INVOICE_NO, DataTypes.StringType)
            .add(STOCK_CODE, DataTypes.StringType)
            .add(DESCRIPTION, DataTypes.StringType)
            .add(QUANTITY, DataTypes.IntegerType)
            .add(INVOICE_DATE, DataTypes.TimestampType)
            .add(UNIT_PRICE, DataTypes.DoubleType)
            .add(CUSTOMER_ID, DataTypes.StringType)
            .add(COUNTRY, DataTypes.StringType)
            .add(TOTAL_PRICE, DataTypes.DoubleType);

    private SalesSchema() {
    }
}
