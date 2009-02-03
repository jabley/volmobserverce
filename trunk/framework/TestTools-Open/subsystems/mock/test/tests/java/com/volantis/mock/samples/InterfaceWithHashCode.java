/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * Makes sure that an interface that overrides hashCode() is mockable.
 *
 * @mock.generate
 */
public interface InterfaceWithHashCode {

    /**
     * Override {@link Object#hashCode()}.
     */
    int hashCode();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/4	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
