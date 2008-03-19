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
package com.volantis.mcs.expression;

import com.volantis.mcs.objects.PolicyIdentity;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


/**
 * Implmentation of a RepositoryObjectIdentityValue. This is an atomic value 
 * that returns itself as a RepositoryObjectIdentity.
 */
public class RepositoryObjectIdentityValueImpl extends AtomicSequence
    implements RepositoryObjectIdentityValue, StringValue {
        
    /**
     * The RepositoryObjectIdentity we are wrapping
     */
    private PolicyIdentity identity;

    /**
     * Create a RepositoryObjectIdentityValue from a FormatReference
     * @param factory ExpressionFactory used to build the StringValue
     */
    public RepositoryObjectIdentityValueImpl(ExpressionFactory factory,
        PolicyIdentity identity) {
        super(factory);
        this.identity = identity;
    }

    /**
     * Return the RepositoryObjectIdentity we were passed in the constructor.
     * @return the wrapped RepositoryObjectIdentity
     */
    public PolicyIdentity asPolicyIdentity() {
        return identity;
    }

    /**
     * Return the RepositoryObjectIdentity as a string. 
     * @return the string representation of the RepositoryObjectIdentity
     */
    public StringValue stringValue() throws ExpressionException {
        return this;
    }

    // Javadoc inherited
    public void streamContents(ContentHandler handler)
        throws ExpressionException, SAXException {
        String value = identity.toString();
        handler.characters(value.toCharArray(), 0, value.length());
    }
    

    // javadoc inherited
    public String asJavaString() {
        return identity.toString();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 ===========================================================================
*/
