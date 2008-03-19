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
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

/**
 * Defines an input context derived from a given point within a document
 * source.
 */
public interface InputContext {
    /**
     * Returns the name of the element within which a contextual match is to
     * be performed.
     *
     * @return the containing element's name. May include a namespace prefix
     */
    String getContainingElementName();

    /**
     * Returns the (partial) element or attribute name for which a match is
     * required.
     *
     * @return partial element or attribute name against which a match is
     * required. May include a namespace prefix
     */
    String getMatchName();

    /**
     * Indicates whether the context represents an attribute name request or
     * an element name request.
     *
     * @return true if an attribute match is required
     */
    boolean isAttribute();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Jan-04	2309/1	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
