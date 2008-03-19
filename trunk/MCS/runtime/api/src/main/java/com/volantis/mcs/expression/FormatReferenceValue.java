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

import com.volantis.mcs.protocols.FormatReference;

import com.volantis.xml.expression.atomic.AtomicValue;

/**
 * Interface defining a FormatReferenceValue. This is an atomic value that
 * returns itself as a FormatReference.
 */
public interface FormatReferenceValue extends AtomicValue {
    /**
     * Return the value as a FormatReference object
     * @return the FormatReference
     */
    FormatReference asFormatReference();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 07-Jan-04	2389/5	steve	VBM:2003121701 Enhanced pane referencing

 06-Jan-04	2389/2	steve	VBM:2003121701 Pre-test save

 ===========================================================================
*/
