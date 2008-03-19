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
package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.xml.pipeline.sax.XMLPipeline;
import org.xml.sax.SAXException;

/**
 * Permits a value to be defined that actually delegates its definition,
 * excluding the name under which it is registered, to an alterative (delegate)
 * value.
 */
public class DelegateValue
        implements TValue {

    /**
     * Holds the verification status for the value.
     */
    private boolean verified = false;

    /**
     * The alternative TValue instance to which this value should delegate.
     */
    private TValue delegate;

    /**
     * Initializes the new instance from the parameters provided.
     *
     * @param delegate the alternative value to which all but name is
     *                 delegated
     * @throws NullPointerException if a null value is given for delegate
     */
    public DelegateValue(TValue delegate) {
        if (delegate != null) {
            this.delegate = delegate;
        } else {
            throw new NullPointerException("A delegate must be specified");
        }
    }

    // javadoc inherited
    public void insert(XMLPipeline target, String parameter)
            throws SAXException {
        delegate.insert(target, parameter);
    }

    public Complexity getComplexity() {
        return delegate.getComplexity();
    }

    // javadoc inherited
    public boolean requiresDynamicPipeline() {
        return delegate.requiresDynamicPipeline();
    }

    // javadoc inherited
    public void verify() {
        verified = true;
    }

    // javadoc inherited
    public boolean isVerified() {
        return verified;
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

 18-Jul-03	213/1	doug	VBM:2003071615 Refactored XMLProcess interface

 10-Jun-03	13/2	philws	VBM:2003030610 Integrate with Template Model Expression facilities

 ===========================================================================
*/
