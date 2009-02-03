/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.value.ExpectedValue;

/**
 * The expected arguments of a method invocation.
 */
public class ExpectedArguments {

    private static final ExpectedValue[] EMPTY_VALUES_ARRAY
            = new ExpectedValue[0];

    /**
     * The expected values.
     */
    private ExpectedValue[] values;

    /**
     * Initialise from an array of arguments.
     *
     * <p>Arguments that are instances of {@link ExpectedValue} are passed
     * straight through, others are wrapped inside a ExpectsValue that checks
     * for equality.</p>
     *
     * @param method    The method that is expected the arguments.
     * @param arguments The array of arguments.
     * @param exact     True if the arguments are expected to match exactly,
     *                  false if some can match in other ways.
     */
    public ExpectedArguments(
            MethodIdentifier method, Object[] arguments, boolean exact) {

        // Make sure that the arguments are compatible with the method.
        ensureCompatibleExpectedArguments(method, arguments);

        if (arguments == null) {
            values = EMPTY_VALUES_ARRAY;
        } else {
            values = new ExpectedValue[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                Object argument = arguments[i];
                if (!exact && argument instanceof ExpectedValue) {
                    values[i] = (ExpectedValue) argument;
                } else {
                    values[i] = MockFactory.getDefaultInstance()
                            .expectsEqual(argument);
                }
            }
        }
    }

    /**
     * Get the expected values.
     * @return The array of expected values.
     */
    public ExpectedValue[] getExpectedValues() {
        return values;
    }

    /**
     * Ensures that the expected arguments are compatible with the method.
     *
     * <p>This ensures that the arguments are either implementations of
     * {@link ExpectedValue} or are of the same type as the argument.</p>
     *
     * @param method
     * @param arguments
     */
    private static
            void ensureCompatibleExpectedArguments(MethodIdentifier method,
                                                   Object[] arguments) {

        int argumentsLength;
        if (arguments == null) {
            argumentsLength = 0;
        } else {
            argumentsLength = arguments.length;
        }

        // Make sure that the correct number of arguments have been added.
        Class[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != argumentsLength) {
            throw new IllegalArgumentException
                    ("Expected " + parameterTypes.length
                     + " arguments, received " + argumentsLength);
        }

        // Check each argument in turn.
        int size = argumentsLength;
        for (int i = 0; i < size; i += 1) {
            Object argument = arguments[i];
            Class type = parameterTypes[i];

            // If a argument is not the correct type or an instance of
            // ExpectedValue then fail.
            if (argument != null && !ReflectionHelper.isInstance(type, argument)
                    && !(argument instanceof ExpectedValue)) {
                throw new IllegalArgumentException
                        ("Argument " + i + " is neither a "
                         + type + " or an ExpectedValue");
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	8978/1	pduffin	VBM:2005070712 Further enhanced mock framework

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3514/4	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
