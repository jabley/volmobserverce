/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.example;

/**
 * Example interface for illustrating how to write a mock object.
 *
 * @mock.generate
 */
public interface Foo {

    /**
     * Set the bar property.
     * @param bar The new value of the bar property, may not be null.
     */
    public void setBar(String bar);

    /**
     * Get the bar property.
     * @return The value of the bar property.
     */
    public String getBar();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Apr-04	3703/3	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
