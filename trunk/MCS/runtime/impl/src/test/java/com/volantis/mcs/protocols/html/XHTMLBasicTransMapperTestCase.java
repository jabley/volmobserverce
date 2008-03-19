/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasicTransMapperTestCase.java,v 1.3 2002/10/15 11:13:14 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Sep-02    Phil W-S        VBM:2002091901 - Created. 
 * 11-Oct-02    Phil W-S        VBM:2002100804 - Corrected ordering of expected
 *                              and actual results in the assertion.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.TransMapperTestAbstract;
import com.volantis.mcs.protocols.trans.TransMapper;
import com.volantis.mcs.protocols.trans.TransformationConfiguration;

/**
 * This is the unit test for the XHTMLBasicTransMapper and AbstractTransMapper
 * classes, the BasicMapperElement.remap method and the TransMapper interface.
 */
public class XHTMLBasicTransMapperTestCase extends TransMapperTestAbstract {

    /**
     * Simple method test
     */
    public void testInitialize() {
        // Could check that the mapper table has been initialized as
        // expected, but the actual remap method test will verify this.
        // Therefore, this test is empty.
    }

    // Javadoc inherited.
    public TransMapper getTransMapper(TransformationConfiguration configuration) {
        return new XHTMLBasicTransMapper(configuration);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9223/6	emma	VBM:2005080403 Remove style class from within protocols and transformers

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-04	4781/3	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 ===========================================================================
*/
