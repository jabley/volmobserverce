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
package com.volantis.mcs.eclipse.ab.editors.dom;

import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;

/**
 * This {@link DefaultJDOMFactory} specialization overrides the
 * {@link org.jdom.input.JDOMFactory#element} method so that the element
 * created belongs to the {@link com.volantis.mcs.eclipse.common.odom.MCSNamespace#LPDM} namespace.
 */
public class LPDMJDOMFactory extends DefaultJDOMFactory {
    /**
     * Factors an element in the {@link com.volantis.mcs.eclipse.common.odom.MCSNamespace#LPDM} namespace
     */
    // rest of javadoc inherited
    public Element element(String name) {
        // create the element in the LPDM namespace
        return element(name, MCSNamespace.LPDM);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 17-Dec-03	2219/1	doug	VBM:2003121502 Added dom validation to the eclipse editors

 ===========================================================================
*/
