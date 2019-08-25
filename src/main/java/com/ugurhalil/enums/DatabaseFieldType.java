package com.ugurhalil.enums;

/**
 * @author  Halil UĞUR
 * @version 1.0
 * @since   2019-06-06
 */
public enum DatabaseFieldType {

    TEXT("String"),
    INT("Integer"),
    SMALLINT("Integer"),
    MEDIUMTEXT("String"),
    MEDIUMBLOB("String"),
    CHAR("String"),
    VARCHAR("String"),
    LONGVARCHAR("String"),
    NUMERIC("java.math.BigDecimal"),
    DECIMAL("java.math.BigDecimal"),
    BIT("Boolean"),
    TINYINT("Byte"),
    INTEGER("Integer"),
    BIGINT("Long"),
    REAL("Float"),
    FLOAT("Double"),
    DOUBLE("Double"),
    BINARY("Byte[]"),
    VARBINARY("Byte[]"),
    LONGVARBINARY("Byte[]"),
    DATE("java.sql.Date"),
    DATETIME("java.sql.Date"),
    TIME("java.sql.Date"),
    TIMESTAMP("java.sql.Date");

    private String fieldType;

    DatabaseFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String fieldType() {
        return fieldType;
    }

}
