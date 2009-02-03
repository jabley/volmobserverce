package com.volantis.xml.expression.functions.pipeline;

import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.Value;


public class EvaluateFunction implements Function {

    /**
     * Constant defining the name under which the function is assumed to be
     * registered. The namespace for this function isn't important in the
     * implementation.
     */
    public static final String NAME = "evaluate";

    /**
     * Used for localizing exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(EvaluateFunction.class);

    public Value invoke(ExpressionContext context, Value[] arguments)
            throws ExpressionException {

        if (arguments.length != 1) {
            throw new ExpressionException(EXCEPTION_LOCALIZER.format(
                "invalid-num-of-args",
                new Object[]{NAME, 1, arguments.length}));
        }

        ExpressionFactory expressionFactory = context.getFactory();
        ExpressionParser parser = expressionFactory.createExpressionParser();

        Expression expression = parser.parse(arguments[0].stringValue().asJavaString());
        return expression.evaluate(context);
    }
}
