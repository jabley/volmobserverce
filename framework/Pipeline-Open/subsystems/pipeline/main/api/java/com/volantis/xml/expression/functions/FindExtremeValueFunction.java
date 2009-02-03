/*
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.functions;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.CompareResult;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.PipelineExpressionHelper;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.numeric.NumericValue;
import com.volantis.xml.expression.sequence.Sequence;

/**
 * Base class for functions that find an extreme value in sequence
 */
public abstract class FindExtremeValueFunction implements Function {
    
    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(FindExtremeValueFunction.class);
    
    // javadoc inherited
    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {
        FunctionArgumentsValidationHelper.checkArgumentsCount(getName(), 1, 
                arguments.length);
        Sequence sequence = arguments[0].getSequence();
        Value result;
        if (sequence.getLength() == 0) {
            result = Sequence.EMPTY;
        } else {
            result = sequence.getItem(1);
            for (int i = 2; i <= sequence.getLength(); i++) {
                Value currentItem = sequence.getItem(i);
                CompareResult compareResult = PipelineExpressionHelper.compare(
                        result, currentItem);
                if (compareResult == getExtremeValueComparisonResult()) {
                    result = currentItem;
                } else if (compareResult == CompareResult.INCOMPARABLE) {
                    if (result instanceof NumericValue && currentItem instanceof NumericValue) {
                        result = context.getFactory().createDoubleValue(Double.NaN);
                    } else {
                        throw new ExpressionException(
                                EXCEPTION_LOCALIZER.format(
                                        "incomparable-types",
                                        new Object[]{
                                            getName(),
                                            result.getClass().getName(),
                                            currentItem.getClass().getName()}));
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Returns a result of comparison of extreme value to other values in sequence.
     */
    protected abstract CompareResult getExtremeValueComparisonResult();
    
    /**
     * Returns function name
     */
    protected abstract String getName();
}
