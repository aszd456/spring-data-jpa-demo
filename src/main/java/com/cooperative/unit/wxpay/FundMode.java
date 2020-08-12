package com.cooperative.unit.wxpay;

import java.sql.Types;

public enum FundMode {
    IN("收入"), OUT("支出"), NONE("无");
    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * @param typeName {@link #typeName}
     */
    private FundMode(String typeName) {
        this.typeName = typeName;
    }

    /**
     * @return {@link #typeName}
     */
    public String getTypeName() {
        return typeName;
    }

    public static final String SQL_TYPE = "" + Types.TINYINT;
}
