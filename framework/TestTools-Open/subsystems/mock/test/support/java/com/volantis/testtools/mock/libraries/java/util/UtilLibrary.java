/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.java.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Triggers auto generation of classes within <code>java.util</code> for which
 * the source is not available.
 *
 * <p>If you add new fields in this file then remember to update the associated
 * test case to ensure that the generated mocks are usable.</p>
 *
 * @mock.generate library="true"
 */
public class UtilLibrary {

    /**
     * @mock.generate interface="true"
     */
    public Collection collection;

    /**
     * @mock.generate interface="true" base="Collection"
     */
    public Set set;

    /**
     * @mock.generate interface="true" base="Collection"
     */
    public List list;

    /**
     * @mock.generate interface="true"
     */
    public Iterator iterator;

    /**
     * @mock.generate interface="true"
     */
    public Map map;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 ===========================================================================
*/
