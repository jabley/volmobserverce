/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

/**
 * Makes sure that an interface that overrides equals() is mockable.
 *
 * @mock.generate
 */
public interface InterfaceWithEquals {

    /**
     * Override {@link java.lang.Object#equals(java.lang.Object)}.
     */
    boolean equals(java.lang.Object obj);

    int equals(int a);

    long equals(byte a, short c);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/4	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 ===========================================================================
*/
