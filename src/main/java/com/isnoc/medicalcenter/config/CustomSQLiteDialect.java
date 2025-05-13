package com.isnoc.medicalcenter.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.CommonFunctionFactory;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.dialect.pagination.LimitOffsetLimitHandler;
import org.hibernate.dialect.unique.UniqueDelegate;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.query.sqm.TemporalUnit;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.jdbc.TimestampJdbcType;

import java.sql.Types;

/**
 * Custom SQLite dialect implementation for compatibility with the latest Hibernate version
 * Only use this if the default community dialect doesn't work correctly
 */
public class CustomSQLiteDialect extends Dialect {

    public CustomSQLiteDialect() {
        this(DatabaseVersion.make(3, 0));
    }

    public CustomSQLiteDialect(DialectResolutionInfo info) {
        this(DatabaseVersion.make(info.getDatabaseMajorVersion(), info.getDatabaseMinorVersion()));
    }

    public CustomSQLiteDialect(DatabaseVersion version) {
        super(version);
        
        // Register column types
        registerColumnType(Types.BIT, "boolean");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.NUMERIC, "numeric($p, $s)");
        registerColumnType(Types.DECIMAL, "decimal");
        registerColumnType(Types.CHAR, "char");
        registerColumnType(Types.VARCHAR, "varchar");
        registerColumnType(Types.LONGVARCHAR, "longvarchar");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.LONGVARBINARY, "blob");
        registerColumnType(Types.BLOB, "blob");
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.BOOLEAN, "boolean");
    }

    @Override
    public boolean supportsIdentityColumns() {
        return true;
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public boolean qualifyIndexName() {
        return false;
    }

    @Override
    public boolean supportsSequences() {
        return false;
    }

    @Override
    public UniqueDelegate getUniqueDelegate() {
        return super.getUniqueDelegate();
    }

    @Override
    public boolean supportsTemporaryTables() {
        return true;
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }

    @Override
    public LimitHandler getLimitHandler() {
        return new LimitOffsetLimitHandler();
    }
    
    @Override
    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    @Override
    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    @Override
    public String getCurrentTimestampSelectString() {
        return "select current_timestamp";
    }

    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);

        CommonFunctionFactory functionFactory = new CommonFunctionFactory(functionContributions);
        functionFactory.dateAdd();
        functionFactory.dateDiff(TemporalUnit.DAY);
        
        // Add other needed functions
        functionFactory.trim();
        functionFactory.substring();
        functionFactory.concat();
        functionFactory.locate();
        functionFactory.length();
    }
}
