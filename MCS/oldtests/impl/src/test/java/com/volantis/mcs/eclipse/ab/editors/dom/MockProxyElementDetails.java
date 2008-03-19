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

import com.volantis.mcs.eclipse.ab.editors.dom.ProxyElementDetails.ChangeReason;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Attribute;
import org.jdom.Namespace;

/**
 * Mock Proxy Element Details Object.
 */
class MockProxyElementDetails implements ProxyElementDetails {

    private final List attributeNames = new ArrayList();

    /**
     * Various useful properties that track how many times a method has been
     * called.
     */
    private int setProxiedElementsCallCount = 0;
    private int getElementNameCount = 0;
    private int getElementNamespaceCount = 0;
    private int getAttributeNamesCount = 0;

    public int getGetAttributeNamesCount() {
        return getAttributeNamesCount;
    }

    public int getGetElementNameCount() {
        return getElementNameCount;
    }

    public int getGetElementNamespaceCount() {
        return getElementNamespaceCount;
    }

    public int getSetProxiedElementsCallCount() {
        return setProxiedElementsCallCount;
    }


    public void setProxiedElements(Iterator elements) {
        if (elements == null) {
            throw new IllegalArgumentException("Bad elements");
        }
        attributeNames.clear();
        if (elements.hasNext()) {
            ODOMElement element = (ODOMElement) elements.next();
            if (element != null) {
                Iterator attribIter = element.getAttributes().iterator();
                while (attribIter.hasNext()) {
                    Attribute attrib = (Attribute) attribIter.next();
                    attributeNames.add(attrib.getName());
                }
                while (elements.hasNext()) {
                    element = (ODOMElement) elements.next();
                    if (element != null) {
                        attribIter = element.getAttributes().iterator();
                        List nextAttribNames = new ArrayList();
                        while (attribIter.hasNext()) {
                            Attribute attrib = (Attribute) attribIter.next();
                            nextAttribNames.add(attrib.getName());
                        }
                        attributeNames.retainAll(nextAttribNames);
                    }
                }
            }
        }

        ++setProxiedElementsCallCount;
    }

    public String getElementName() {
        ++getElementNameCount;
        return "proxyName";
    }

    public Namespace getElementNamespace() {
        ++getElementNamespaceCount;
        return null;
    }

    public String [] getAttributeNames() {
        ++getAttributeNamesCount;
        String array[] = new String[attributeNames.size()];
        attributeNames.toArray(array);
        return array;
    }

    public boolean isAttributeName(String name) {
        return attributeNames.contains(name);
    }

    public boolean setProxiedElements(Iterator elements, ChangeReason reason) {
        // TODO Auto-generated method stub
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Oct-05	9734/1	adrianj	VBM:2005100510 Allow text in ProxyElements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Mar-04	3256/1	byron	VBM:2004021106 The theme StyleList property types do not work in the theme editor

 10-Dec-03	1968/3	richardc	VBM:2003111502 Second semi-tested draft for code release

 03-Dec-03	1968/1	richardc	VBM:2003111502 Untested draft for code review

 28-Nov-03	2041/1	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 ===========================================================================
*/
