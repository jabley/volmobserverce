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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/PaneInstanceTestCase.java,v 1.8 2003/03/05 10:10:28 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Dec-2002  Sumit           VBM:2002111103 - Created
 * 30-Jan-03    Geoff           VBM:2003012101 - Refactor so that it uses the 
 *                              new shared Test... versions of classes rather  
 *                              their own "cut & paste" inner classes.
 * 07-Feb-03    Chris W         VBM:2003020609 - Moved the test cases from
 *                              FormatIteratorFormatFilterTestCase to this
 *                              class. FormatIteratorFormatFilter and its
 *                              corresponding test case has been removed.
 * 03-Mar-03    Sumit           VBM:2003022107 - Test for paneAttributes
 *                              never being the same reference. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstanceTestAbstract;

/**
 * The test class for PaneInstance
 */
public class PaneInstanceTestCase extends AbstractPaneInstanceTestAbstract {

    // Javadoc inherited.
    protected AbstractPaneInstance createTestableAbstractPaneContext(
            NDimensionalIndex index) {
        
        return new PaneInstance(index);
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 29-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
