/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * Makes sure that an interface that overrides clone() and makes it public is
 * mockable.
 *
 * @mock.generate
 */
public interface InterfaceWithCloneNoException {

    /**
     * Make {@link Object#clone()} public.
     *
     * @return The clone.
     */
    java.lang.Object clone();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/4	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
