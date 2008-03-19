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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/htmlversion4_0/HTML4_0UnabridgedTransformerTestCase.java,v 1.2 2003/01/15 12:42:10 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Tests the
 *                              HTML4_0UnabridgedTransformer.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.htmlversion4_0;

import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.html.xhtmlfull.XHTMLFullUnabridgedTransformerTestCase;

/**
 * Tests the HTML4_0UnabridgedTransformer.
 */
public class HTML4_0UnabridgedTransformerTestCase
    extends XHTMLFullUnabridgedTransformerTestCase {

    protected DOMTransformer getTransformer() {
        return new HTML4_0UnabridgedTransformer(null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9223/2	emma	VBM:2005080403 Remove style class from within protocols and transformers

 19-Aug-05	9289/2	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
