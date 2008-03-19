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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * A {@link WBDOMAttribute} with a literal name.
 * <p>
 * This corresponds to a WBSAX attribute beginning with
 * {@link com.volantis.mcs.wbsax.GlobalToken#LITERAL}, etc.
 */ 
public class LiteralNameAttribute extends WBDOMAttribute
        implements LiteralNameProvider {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            LiteralNameAttribute.class);

    /**
     * The name of this attribute.
     */ 
    private StringReference name;

    /**
     * Construct an instance of this class with the name provided.
     * 
     * @param name the name of this class.
     */ 
    public LiteralNameAttribute(StringReference name) {
        this.name = name;
    }

    // Javadoc inherited.
    public String getName() throws WBDOMException {
        try {
            return name.resolveString().getString();
        } catch (WBSAXException e) {            
            throw new WBDOMException(
                        exceptionLocalizer.format(
                                    "wbdom-attribute-name-resolution-error"),
                        e);
        }
    }

    // Javadoc inherited.
    public String getValuePrefix() {
        return null;
    }

    // Javadoc inherited.
    public StringReference getLiteralName() {
        return name;
    }

    // Javadoc inherited.
    public void accept(NameVisitor visitor) throws WBDOMException {
        visitor.visitLiteralProvider(this);
    }
    
    // Javadoc inherited.
    public String toString() {
        String name;
        try {
            name = getName();
        } catch (WBDOMException e) {
            name = "unavailable";
        } 
        
        return getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(this))
                + " [" + name + "=" + getValueBuffer() + "]";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/8	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
