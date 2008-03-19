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
package com.volantis.xml.expression.impl.jxpath;

import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.impl.SimpleExpressionFactory;
import com.volantis.xml.namespace.NamespacePrefixTracker;

/**
 * JXPath implementation of the ExpressionFactory interface.
 */
public class JXPathExpressionFactory extends SimpleExpressionFactory {
    // javadoc inherited
    public ExpressionParser createExpressionParser() {
        return new JXPathExpressionParser(this);
    }

    // javadoc inherited
    public ExpressionContext createExpressionContext(
            EnvironmentInteractionTracker environmentInteractionTracker,
            NamespacePrefixTracker namespacePrefixTracker) {
        return new JXPathExpressionContext(this,
                                           environmentInteractionTracker,
                                           namespacePrefixTracker);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jun-03	102/2	sumit	VBM:2003061906 request:getParameter XPath function support

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
