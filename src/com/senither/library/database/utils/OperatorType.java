package com.senither.library.database.utils;

public enum OperatorType
{

    AND("AND"),
    OR("OR");

    private final String operator;

    private OperatorType(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
}
