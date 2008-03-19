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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.editors.devices.odom;

import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.xml.validation.DOMValidator;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * A factory for creating DeviceODOMElement instances.
 */
public class DeviceODOMElementFactory extends ODOMFactory {
    
    // javadoc inherited
    public Element element(String name) {
        return new DeviceODOMElement(name);
    }

    // javadoc inherited
    public Element element(String name, String prefix, String uri) {
        return new DeviceODOMElement(name, prefix, uri);
    }

    // javadoc inherited
    public Element element(String name, Namespace namespace) {
        return new DeviceODOMElement(name, namespace);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	4394/1	allan	VBM:2004051018 Undo/Redo in device editor.

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 ===========================================================================
*/
