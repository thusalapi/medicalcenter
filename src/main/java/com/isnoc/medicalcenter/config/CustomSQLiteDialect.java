package com.isnoc.medicalcenter.config;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;
import org.hibernate.dialect.pagination.LimitOffsetLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;

/**
 * Basic SQLite dialect for Hibernate 6.6+
 * This is a minimal implementation that should be used only if the community dialect doesn't work
 */
public class CustomSQLiteDialect extends Dialect {

    public CustomSQLiteDialect() {
        super(DatabaseVersion.make(3, 0));
    }

    public CustomSQLiteDialect(DialectResolutionInfo info) {
        super(DatabaseVersion.make(info.getDatabaseMajorVersion(), info.getDatabaseMinorVersion()));
    }

    @Override
    public LimitHandler getLimitHandler() {
        return new LimitOffsetLimitHandler();
    }
    
    @Override
    public String getCurrentTimestampSelectString() {
        return "select current_timestamp";
    }
    
    @Override
    public String getAddColumnString() {
        return "add column";
    }
    
    @Override
    public boolean supportsTemporaryTables() {
        return true;
    }
}
