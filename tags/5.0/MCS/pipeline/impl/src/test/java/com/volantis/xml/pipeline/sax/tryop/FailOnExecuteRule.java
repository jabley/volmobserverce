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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.xml.pipeline.sax.tryop;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import junitx.framework.Assert;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class ...
 */
public class FailOnExecuteRule extends DynamicElementRuleImpl {


    // todo javadoc this
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName elementName,
                               Attributes attributes)
            throws SAXException {
        Assert.fail("The dynamic process should have prevented this from " +
                "ever being executed.");      
        return null;
    }

    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 ===========================================================================
*/
