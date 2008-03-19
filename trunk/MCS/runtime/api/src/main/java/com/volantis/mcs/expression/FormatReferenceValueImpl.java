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

import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.FormatReference;

import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


/**
 * Implmentation of a FormatReferenceValue. This is an atomic value that
 * returns itself as a FormatReference.
 */
public class FormatReferenceValueImpl extends AtomicSequence
    implements FormatReferenceValue, StringValue {
        
    /**
     * The format reference we are wrapping
     */
    private FormatReference ref;
    
    /**
     * A String version of the format reference, this will be built on request
     */
    private String value = null;

    /**
     * Create a FormatReferenceValue from a FormatReference
     * @param factory ExpressionFactory used to build the StringValue
     * @param reference the FormatReference we are wrapping
     */
    public FormatReferenceValueImpl(ExpressionFactory factory,
        FormatReference reference) {
        super(factory);
        ref = reference;
    }

    /**
     * Return the FormatReference we were passed in the constructor.
     * @return the wrapped FormatReference
     */
    public FormatReference asFormatReference() {
        return ref;
    }

    /**
     * Return the FormatReference as a string. The returned string is the
     * pane stem followed by each index separated by '.', eg pane.1.2.1.3
     * @return the string representation of the format reference 
     */
    public StringValue stringValue() throws ExpressionException {
        return this;
    }

    // Javadoc inherited
    public void streamContents(ContentHandler handler)
        throws ExpressionException, SAXException {
        buildValue();
        handler.characters(value.toCharArray(), 0, value.length());
    }
    

    // javadoc inherited
    public String asJavaString() {
        buildValue();
        return value;
    }

    /** Create the value for this reference if it does not already exist */
    private void buildValue() {
        if (value==null) {
            StringBuffer buff = new StringBuffer(ref.getStem());
            NDimensionalIndex index = ref.getIndex();
            int[] indicies = index.getIndicies();
            for (int i = 0; i < indicies.length; i++) {
                buff.append('.').append(indicies[i]);
            }
            value = buff.toString();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 08-Jan-04	2461/6	steve	VBM:2003121701 Build string value on demand

 08-Jan-04	2461/4	steve	VBM:2003121701 Unnecessary null test

 08-Jan-04	2461/2	steve	VBM:2003121701 FormatReferenceValueImpl is also a StringValue

 07-Jan-04	2389/5	steve	VBM:2003121701 Enhanced pane referencing

 06-Jan-04	2389/2	steve	VBM:2003121701 Pre-test save

 ===========================================================================
*/
