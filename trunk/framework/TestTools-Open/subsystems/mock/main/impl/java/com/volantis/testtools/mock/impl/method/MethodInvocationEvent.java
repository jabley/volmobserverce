/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl.method;

import com.volantis.testtools.mock.impl.EventImpl;
import com.volantis.testtools.mock.value.ValueHelper;
import com.volantis.testtools.mock.method.MethodCall;
import com.volantis.testtools.mock.method.MethodIdentifier;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodHelper;
import com.volantis.testtools.mock.method.ReflectionHelper;

/**
 * An event that was triggered by invoking a method on a mock object.
 */
public class MethodInvocationEvent
        extends EventImpl implements MethodCall {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * The identifier for the method that this event corresponds to.
     */
    private MethodIdentifier method;

    /**
     * The arguments, may be null.
     */
    private Object[] arguments;

    /**
     * The action that needs to be performed as a result of processing this
     * event.
     */
    private MethodAction action;

    /**
     * Initialise a new event.
     * @param source The source of the event.
     * @param identifier
     * @param arguments
     */
    public MethodInvocationEvent(Object source,
                                 MethodIdentifier identifier,
                                 Object[] arguments) {
        super(source);

        this.method = identifier;
        if (arguments == null) {
            this.arguments = EMPTY_OBJECT_ARRAY;
        } else {
            this.arguments = arguments;
        }

        ensureCompatibleActualArguments(method, this.arguments);
    }

    /**
     * Initialise a new event.
     * @param source The source of the event.
     * @param identifier
     */
    public MethodInvocationEvent(Object source,
                                 MethodIdentifier identifier) {
        this(source, identifier, null);
    }

    public MethodIdentifier getMethod() {
        return method;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Object getArgument(String name, Class type) {
        int index = method.getParameterIndexByName(name, type);
        return arguments[index];
    }

    public Object getArgument(Class type) {
        int index = method.getParameterIndexByType(type);
        return arguments[index];
    }

    public MethodAction getAction() {
        return action;
    }

    public void setAction(MethodAction action) {
        this.action = action;
    }

    // Javadoc inherited.
    public String toString() {
        StringBuffer buffer = new StringBuffer(80);

        final String shortDeclaringClassName
                = MethodHelper.getShorterName(
                        method.getDeclaringClass().getName());
        buffer
                .append("call to ")
                .append(getSource())
                .append(".")
                .append(shortDeclaringClassName)
                .append("#").append(method.getName()).append("(");

        String separator = "";
        for (int i = 0; i < arguments.length; i++) {
            Object argument = arguments[i];
            buffer.append(separator);
            ValueHelper.deepAppend(buffer, argument);
            separator = ", ";
        }
        buffer.append(")");

        return buffer.toString();
    }

    /**
     * Ensures that the arguments are compatible with the method.
     *
     * <p>This ensures that the arguments are of the same type as the
     * parameter.</p>
     *
     * @param method
     * @param arguments
     */
    private static
            void ensureCompatibleActualArguments(MethodIdentifier method,
                                                 Object[] arguments) {

        // Make sure that the correct number of arguments have been added.
        Class[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != arguments.length) {
            throw new IllegalArgumentException
                    ("Expected " + parameterTypes.length
                     + " arguments, received " + arguments.length);
        }

        // Check each argument in turn.
        int size = arguments.length;
        for (int i = 0; i < size; i += 1) {
            Object argument = arguments[i];
            Class type = parameterTypes[i];

            // If an argument is not the correct type then fail.
            if (argument != null &&
                    !ReflectionHelper.isInstance(type, argument)) {
                throw new IllegalArgumentException
                        ("Argument " + i + " is not a " + type);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 20-May-05	8277/2	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 12-May-05	8208/1	pduffin	VBM:2005051208 Committing mock object framework changes

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 08-Apr-04	3514/4	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 08-Apr-04	3514/1	pduffin	VBM:2004032208 Added DefaultMenuItemRendererSelector

 07-Apr-04	3610/1	pduffin	VBM:2004032509 Added separator API and default implementation

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
