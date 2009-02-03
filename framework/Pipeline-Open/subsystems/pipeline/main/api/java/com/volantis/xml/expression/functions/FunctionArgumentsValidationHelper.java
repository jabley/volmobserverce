/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;

/**
 * Class to validate function arguments. It checks if number of arguments
 * is inside the specified range.
 */
class FunctionArgumentsValidationHelper {

    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(FunctionArgumentsValidationHelper.class);
    
    private String functionName;
    private int minCount;
    private int maxCount;
    
    /**
     * Throws an {@link ExpressionException} if function arguments are incorrect
     * 
     * @param functionName function name
     * @param minCount minimum number of arguments
     * @param maxCount maximum number of arguments
     * @param actualCount actual number of arguments
     * @throws ExpressionException if validation of arguments fails
     */
    static void checkArgumentsCount(String functionName, int minCount, int maxCount, int actualCount) throws ExpressionException {
        if (actualCount < minCount || actualCount > maxCount) {
            throw new ExpressionException(EXCEPTION_LOCALIZER.format(
                    "invalid-num-args-range",
                    new Object[]{
                        functionName,
                        new Integer(minCount),
                        new Integer(maxCount),
                        new Integer(actualCount)}));
        }
    }
    
    /**
     * Throws an {@link ExpressionException} if function arguments are incorrect
     * 
     * @param functionName function name
     * @param expected expected number of arguments
     * @param actualCount actual number of arguments
     * @throws ExpressionException if validation of arguments fails
     */
    static void checkArgumentsCount(String functionName, int expectedCount, int actualCount) throws ExpressionException {
        if (actualCount != expectedCount) {
            throw new ExpressionException(EXCEPTION_LOCALIZER.format(
                    "invalid-num-args",
                    new Object[]{
                        functionName,
                        new Integer(expectedCount),
                        new Integer(actualCount)}));
        }
    }
    
}
