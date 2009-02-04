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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/PAPIElementTestAbstract.java,v 1.5 2003/04/22 10:59:03 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Feb-03    Byron           VBM:2003022813 - Created
 * 04-Mar-03    Byron           VBM:2003022813 - Updated comments in the setUp
 *                              method.
 * 16-Apr-03    Allan           VBM:2003041604 - Removed setUp(), tearDown() 
 *                              and abstract setTestableElement() methods. 
 *                              Modified createTestablePAPIElement() to return a 
 *                              PAPIElement instead of an AbstractElement. Now 
 *                              extends TestCaseAbstract. 
 * 17-Apr-03    Allan           VBM:2003041506 - Made constructor protected. 
 *                              Renamed createTestableElement to 
 *                              createTestablePAPIElement. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class provides the framework for all underlying test cases for the
 * Element hierarchy.   
 */
abstract public class PAPIElementTestAbstract
        extends TestCaseAbstract {
    
    /**
     * Create the testable element.
     *
     * @return  the testable element
     */
    abstract protected PAPIElement createTestablePAPIElement();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 ===========================================================================
*/
