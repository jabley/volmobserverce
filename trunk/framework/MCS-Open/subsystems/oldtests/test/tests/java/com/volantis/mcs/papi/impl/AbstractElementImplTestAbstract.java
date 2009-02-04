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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/AbstractElementImplTestAbstract.java,v 1.3 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Feb-03    Byron           VBM:2003022813 - Created
 * 16-Apr-03    Allan           VBM:2003041604 - Renamed from 
 *                              AbstractElementTestCase. Made abstract. Removed
 *                              element member, createTestablePAPIElement() and 
 *                              setTestableElement(). 
 * 17-Apr-03    Allan           VBM:2003041506 - Made construct protected. 
 *                              Updated testElementReset in light of rename of 
 *                              createTestableElement. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.papi.PAPIElementTestAbstract;

/**
 * This class tests the internal method of AbstractElement.
 * <p>
 * Note that the methods getContentWriter and
 * elementContent do not have specific test fixtures. They were not included
 * because this test case would effectively be testing the Stub framework or
 * java return functionality only. The write method itself
 * should have its own test case.
 */
public abstract class AbstractElementImplTestAbstract 
        extends PAPIElementTestAbstract {

    /**
     * elementReset does nothing in this implementation. Simply test that the
     * call does not throw any exceptions.
     */
    public void testElementReset() throws Exception {
//        PAPIElement element =
                createTestablePAPIElement();
// @todo: sort this out
        //element.elementReset(null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Aug-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 18-Mar-04	3412/1	claire	VBM:2004031201 Early implementation of new menus in PAPI

 ===========================================================================
*/
