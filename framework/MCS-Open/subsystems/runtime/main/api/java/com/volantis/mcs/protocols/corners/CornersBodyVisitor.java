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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.corners;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;

/**
 * Visitor which search body element in docuemnt and save its reference 
 */
public class CornersBodyVisitor extends WalkingDOMVisitorStub {

    private Element bodyElement = null;
    
    /**
     * @return Returns the bodyElement.
     */
    public Element getBodyElement() {
        return bodyElement;
    }

    public CornersBodyVisitor() {
        
    }

    /**
     * @param bodyElement The bodyElement to set.
     */
    private void setBodyElement(Element bodyElement) {
        this.bodyElement = bodyElement;
    }

    public void visit(Element element) {
        String nameElement = element.getName(); 
        if(nameElement.equals("body")) {
            setBodyElement(element);
        }
    }
}
