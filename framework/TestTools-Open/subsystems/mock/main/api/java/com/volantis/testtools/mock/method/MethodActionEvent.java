/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.method;

import com.volantis.testtools.mock.Event;

/**
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface MethodActionEvent extends Event {

    /**
     * Get the identifier of the method that triggered this event.
     * @return The identifier of the method that triggered this event.
     */
    MethodIdentifier getMethod();

    /**
     * Get the arguments passed to the triggering method.
     * @return The arguments passed to the triggering method.
     */
    Object[] getArguments();

    /**
     * Get the value of an argument with the specified name and of the
     * specified type.
     * @param name The name of the parameter.
     * @param type The type of the parameter.
     * @return The value of the argument.
     * @throws IllegalArgumentException If no such parameter exists in the
     * method identifier associated with this event.
     */
    Object getArgument(String name, Class type);

    /**
     * Get the value of the first argument with the specified type.
     * @param type The type of the parameter.
     * @return The value of the argument.
     * @throws IllegalArgumentException If no such parameter exists in the
     * method identifier associated with this event.
     */
    Object getArgument(Class type);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Jun-05	7995/1	pduffin	VBM:2005050323 Additional enhancements for mock framework, allow setting of occurrences of a method call

 25-May-05	7997/1	pduffin	VBM:2005050324 Committing enhancements to mock object framework

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 ===========================================================================
*/
