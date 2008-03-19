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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/papi/DissectingPaneTestCase.java,v 1.1 2003/03/26 14:08:33 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Mar-03    Steve           VBM:2003031907 - Test case for PAPI DissectingPane
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;
import com.volantis.mcs.papi.impl.PAPIInternals;
import junit.framework.TestCase;

/**
 * @todo reformat and align this class with current standards (extend TestCaseAbstract)
 */
public class DissectingPaneTestCase extends TestCase
{
    
    public DissectingPaneTestCase(java.lang.String testName)
    {
        super(testName);
    }
    
    /** Test of getName method, of class com.volantis.mcs.papi.DissectingPane. */
    public void testGetName()
    {
        MyDissectingPaneContext context = new MyDissectingPaneContext();
        DissectingPane pane = PAPIInternals.createDissectingPane( context );
        assertEquals( "dissecting", pane.getName() );
    }
    
    /** Test of getLinkToText method, of class com.volantis.mcs.papi.DissectingPane. */
    public void testGetLinkToText()
    {
        MyDissectingPaneContext context = new MyDissectingPaneContext();
        DissectingPane pane = PAPIInternals.createDissectingPane( context );
        assertEquals( "LinkTo", pane.getLinkToText() );
    }
    
    /** Test of getLinkFromText method, of class com.volantis.mcs.papi.DissectingPane. */
    public void testGetLinkFromText()
    {
        MyDissectingPaneContext context = new MyDissectingPaneContext();
        DissectingPane pane = PAPIInternals.createDissectingPane( context );
        assertEquals( "LinkFrom", pane.getLinkFromText() );
    }
    
    /** Test of overrideLinkToText method, of class com.volantis.mcs.papi.DissectingPane. */
    public void testOverrideLinkToText()
    {
        MyDissectingPaneContext context = new MyDissectingPaneContext();
        DissectingPane pane = PAPIInternals.createDissectingPane( context );
        pane.overrideLinkToText( "wibble" );
        assertEquals( "wibble", pane.getLinkToText() );
    }
    
    /** Test of overrideLinkFromText method, of class com.volantis.mcs.papi.DissectingPane. */
    public void testOverrideLinkFromText()
    {
        MyDissectingPaneContext context = new MyDissectingPaneContext();
        DissectingPane pane = PAPIInternals.createDissectingPane( context );
        pane.overrideLinkFromText( "wibble" );
        assertEquals( "wibble", pane.getLinkFromText() );
    }
    
    // Add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    
    class MyDissectingPaneContext extends DissectingPaneInstance
    {
        MyDissectingPaneContext() {
            super(NDimensionalIndex.ZERO_DIMENSIONS);
            setLinkToText( "LinkTo" );
            setLinkFromText( "LinkFrom" );
        }
        
        public String getDissectingPaneName() {
            return "dissecting";
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
