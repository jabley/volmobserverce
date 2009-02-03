/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock;

/**
 * The base interface for a mock object.
 *
 * <p>See {@link com.volantis.testtools.mock} for more details.</p>
 */
public interface MockObject {

    /**
     * Get the identifier for this mock object.
     *
     * @return The identifier for this mock object.
     */
    String _getIdentifier();

    /**
     * Get the container of the expectations for this object.
     *
     * @return The expectation container.
     */
    ExpectationContainer _getExpectationContainer();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Jul-05	8996/1	pduffin	VBM:2005071103 Enhanced mock generation to support methods defined on Object better

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 20-May-05	8277/3	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 06-Apr-04	3703/6	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/3	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 06-Apr-04	3703/1	pduffin	VBM:2004040106 Added beginnings of a mock object framework

 ===========================================================================
*/
