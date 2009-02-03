/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.libraries.javax.sql;

import javax.sql.DataSource;

/**
 * Triggers auto generation of classes within <code>java.sql</code> and
 * <code>javax.sql</code> for which the source is not available.
 *
 * <p>If you add new fields in this file then remember to update the associated
 * test case to ensure that the generated mocks are usable.</p>
 *
 * @mock.generate library="true"
 */
public class SQLXLibrary {

    /**
     * @mock.generate interface="true"
     */
    public DataSource dataSource;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-May-05	7995/1	pduffin	VBM:2005050323 Added ability to generate mocks for external libraries

 ===========================================================================
*/
