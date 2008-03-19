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
package com.volantis.mcs.wbsax;

/**
 * A simple implementation of OpaqueValue which requires it's value to be
 * provided as a {@link WBSAXString} into an inline string.
 * <p>
 * This class is abstract even though it is complete because we use instanceof
 * checks to distinguish between the different uses of OpaqueValue, and thus
 * it would be dangerous for multiple users to use it directly.
 */ 
public abstract class StringOpaqueValue extends OpaqueValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private WBSAXString value;

    public StringOpaqueValue() {
    }

    public void setValue(WBSAXString value) {
        if (value == null) {
            throw new IllegalArgumentException("Invalid value: " + 
                    value);
        }
        this.value = value;
    }

    public WBSAXString getValue() {
        return value;
    }

    public String getString() throws WBSAXException {
        if (value == null) {
            throw new IllegalStateException(
                    "Attempt to render opaque value before value was provided");
        }
        return value.getString();
    }

    public byte[] getBytes() throws WBSAXException {
        if (value == null) {
            throw new IllegalStateException(
                    "Attempt to render opaque value before value was provided");
        }
        byte[] string = value.getBytes();
        byte[] attribute = new byte[string.length + 1];
        attribute[0] = GlobalToken.STR_I;
        System.arraycopy(string, 0, attribute, 1, string.length);               
        return attribute;
    }

    public String toString() {
        return "[StringOpaqueValue:" + getValue() + "]";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05	7243/5	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix WMLC generation)

 02-Oct-03	1469/2	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
