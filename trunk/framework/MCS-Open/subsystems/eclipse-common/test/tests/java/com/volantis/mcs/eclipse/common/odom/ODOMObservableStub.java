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
package com.volantis.mcs.eclipse.common.odom;

import org.jdom.Element;

/**
 * A stub implementation of the ODOMObservable interface.
 */
public class ODOMObservableStub implements ODOMObservable {
    // javadoc inherited
    public void childChanged(ODOMChangeEvent event) {
    }

    //javadoc inherited
    public String getName() {
        return null;
    }

    //javadoc inherited
    public Element getParent() {
        return null;
    }
    
    // javadoc inherited
    public void addChangeListener(ODOMChangeListener listener) {
    }

    // javadoc inherited
    public void addChangeListener(ODOMChangeListener listener,
                                  ChangeQualifier changeQualifier) {
    }

    // javadoc inherited
    public void removeChangeListener(ODOMChangeListener listener) {
    }

    // javadoc inherited
    public void removeChangeListener(ODOMChangeListener listener,
                                     ChangeQualifier changeQualifier) {
    }

    // JavaDoc inherited
    public void detachObservable() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-May-04	4429/1	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 08-Dec-03	2157/3	pcameron	VBM:2003111302 Some tweaks to ElementAttributesSection

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
