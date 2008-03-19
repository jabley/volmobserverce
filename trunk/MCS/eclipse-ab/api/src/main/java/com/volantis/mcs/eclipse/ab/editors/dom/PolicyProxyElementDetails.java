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

import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.Iterator;

/**
 * An implementation of ProxyElementDetails that works for policies.
 */
public class PolicyProxyElementDetails implements ProxyElementDetails {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(PolicyProxyElementDetails.class);
    

    /**
     * The name of the current proxy element.
     */
    private String elementName = ODOMElement.NULL_ELEMENT_NAME;

    /**
     * The array of attribute names associated with the proxy.
     */
    private String[] attributes;

    /**
     * Construct a new PolicyProxyElementDetails.
     * @param elementName The name of the policy element whose child
     * elements are being proxied.
     */
    public PolicyProxyElementDetails(String elementName) {
        PolicyAttributesDetails details =
                new PolicyAttributesDetails(elementName, true);
        attributes = details.getAttributes();
    }

    /**
     * Update the elementName if necessary.
     */
    // rest of javadoc inherited
    public boolean setProxiedElements(Iterator elements,
                                      ProxyElementDetails.ChangeReason reason) {

        if (logger.isDebugEnabled()) {
            logger.debug("setProxiedElements"); //$NON-NLS-1$
        }

        boolean needsUpdate = false;
        if (reason != ATTRIBUTES && reason != ATTRIB_VALUES) {
            if (logger.isDebugEnabled()) {
                logger.debug("setProxiedElements: reason is valid"); //$NON-NLS-1$
            }

            // Check and update the element name if necessary.
            String newElementName = ODOMElement.NULL_ELEMENT_NAME;

            while (elements.hasNext() &&
                    !ODOMElement.UNDEFINED_ELEMENT_NAME.
                    equals(newElementName)) {
                Element next = (Element) elements.next();

                if (newElementName == ODOMElement.NULL_ELEMENT_NAME &&
                        next != null && !next.getName().
                        equals(ODOMElement.UNDEFINED_ELEMENT_NAME)) {
                    newElementName = next.getName();
                } else if (next != null &&
                        !newElementName.equals(next.getName())) {
                    newElementName = ODOMElement.UNDEFINED_ELEMENT_NAME;
                }
            }

            if (newElementName != null && !newElementName.equals(elementName)) {
                elementName = newElementName;
                needsUpdate = true;
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("setProxiedElements: needsUpdate=" + needsUpdate); //$NON-NLS-1$
            logger.debug("setProxiedElements: elementName=\"" + //$NON-NLS-1$
                    elementName + "\""); //$NON-NLS-1$
        }

        return needsUpdate;
    }

    // javadoc inherited
    public String getElementName() {
        return elementName;
    }

    /**
     * Returns the default Namespace with the "lpdm" prefix.
     */
    public Namespace getElementNamespace() {
        return MCSNamespace.LPDM;
    }

    // javadoc inherited
    public String[] getAttributeNames() {
        return attributes;
    }

    // javadoc inherited
    public boolean isAttributeName(String name) {
        boolean found = false;
        for (int i = 0; i < attributes.length && !found; i++) {
            found = attributes[i].equals(name);
        }

        return found;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Dec-03	2267/1	byron	VBM:2003121804 Upgrade to Eclipse v2.1.2 has broken deletion of assets

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
