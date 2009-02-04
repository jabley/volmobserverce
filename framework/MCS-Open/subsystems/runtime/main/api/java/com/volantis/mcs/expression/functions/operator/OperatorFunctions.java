/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.expression.functions.operator;

import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.functions.FunctionTable;
import com.volantis.xml.expression.functions.FunctionTableBuilder;
import com.volantis.xml.namespace.ImmutableExpandedName;

/**
 * The set of Operator related functions.
 */
public class OperatorFunctions {

    private static final String OPERATOR_FN = "op";
    private static final String OPERATOR_FN_URI =
            "http://www.w3.org/2001/12/xquery-operators";

    private final static ImmutableExpandedName SUBTRACT_DATES_NAME =
            new ImmutableExpandedName(OPERATOR_FN_URI, "subtract-dates");
    private final static Function SUBTRACT_DATES_FUNCTION =
            new SubtractDatesFunction();

    private final static ImmutableExpandedName SUBTRACT_TIMES_NAME =
            new ImmutableExpandedName(OPERATOR_FN_URI, "subtract-times");
    private final static Function SUBTRACT_TIMES_FUNCTION =
            new SubtractTimesFunction();

    private final static ImmutableExpandedName SUBTRACT_DATETIMES_NAME =
            new ImmutableExpandedName(OPERATOR_FN_URI, "subtract-dateTimes");
    private final static Function SUBTRACT_DATETIMES_FUNCTION =
            new SubtractDateTimesFunction();

    /**
     * The table of functions.
     */
    public final static FunctionTable FUNCTION_TABLE;

    static {
        ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
        FunctionTableBuilder builder = factory.createFunctionTableBuilder();
        builder.addDefaultPrefixMappings(OPERATOR_FN,  OPERATOR_FN_URI);

        // Register the Operator functions
        builder.addFunction(SUBTRACT_DATES_NAME, SUBTRACT_DATES_FUNCTION);
        builder.addFunction(SUBTRACT_TIMES_NAME, SUBTRACT_TIMES_FUNCTION);
        builder.addFunction(SUBTRACT_DATETIMES_NAME, SUBTRACT_DATETIMES_FUNCTION);

        FUNCTION_TABLE = builder.buildTable();
    }

}
