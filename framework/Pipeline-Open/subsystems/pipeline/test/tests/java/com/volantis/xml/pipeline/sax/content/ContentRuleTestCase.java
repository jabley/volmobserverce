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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-May-03    Doug            VBM:2002121802 - Created. TestCase for the 
 *                              ContentRule class
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.content;

import com.volantis.xml.pipeline.sax.drivers.web.rules.DynamicRuleTestAbstract;

/**
 * Test cases for {@link ContentRule}.
 */
public class ContentRuleTestCase
        extends DynamicRuleTestAbstract {

    /**
     * Ensure that the rule does nothing if no attributes are specified.
     */
    public void testProcessAttributes() throws Exception {
        ContentRule rule = new ContentRule();
        rule.startElement(dynamicProcessMock, elementName, attributes);
    }

    /**
     * Ensure that the rule fails if any attributes are specified.
     */
    public void testInvalidAttributes() throws Exception {
        ContentRule rule = new ContentRule();
        addAttribute("blah", "foo");
        doStartFailure(rule, "Content operation does not support attributes");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 ===========================================================================
*/
