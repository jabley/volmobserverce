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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.objects.PropertyValueLookUp;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

/**
 * A factory class that creates <code>ODOMSelectionFilter</code> instances given
 * a policy JDOM element.
 */
public class LPDMFilterFactory {

    /**
     * The LPDM prefix.
     */
    private static final String PREFIX = MCSNamespace.LPDM.getPrefix();

    /**
     * Create a filter that resolves the assets (including device themes and
     * device layouts) of a given policy (i.e. component, layout or theme)
     * element
     * @param policyElement The policy element.
     * @throws IllegalArgumentException If there are no assets for the
     * given policyElement.
     */
    public static ODOMSelectionFilter createAssetFilter(Element policyElement) {
        StringBuffer xPathBuffer = new StringBuffer();
        // For layout dependent elements we need to change to
        // use deviceLayoutCanvasFormat and deviceLayoutMontageFormat.
        // Otherwise we just create the resolver using the dependent element
        // names listed in assets. This hack should be removed when we move
        // to using the xsd for property value lookups.
        final String policyElementName = policyElement.getName();
        if(policyElementName.equals(LayoutSchemaType.LAYOUT.getName())) {
            
            xPathBuffer.append("ancestor-or-self::"). //$NON-NLS-1$
                append(PREFIX).append(':').
                append(LayoutSchemaType.CANVAS_LAYOUT.getName()).
                append("|ancestor-or-self::"). //$NON-NLS-1$
                append(PREFIX).
                append(":"). //$NON-NLS-1$
                append(LayoutSchemaType.MONTAGE_LAYOUT.getName()).
                // select canvasLayout/montageLayout for layout elements
                append("|child::"). //$NON-NLS-1$
                append(PREFIX).
                append(":"). //$NON-NLS-1$
                append(LayoutSchemaType.CANVAS_LAYOUT.getName()).
                append("|child::"). //$NON-NLS-1$
                append(PREFIX).
                append(":"). //$NON-NLS-1$
                append(LayoutSchemaType.MONTAGE_LAYOUT.getName());

        } else {
            List assets = PropertyValueLookUp.
                    getDependentElements(policyElementName);

            if(assets==null || assets.size()==0) {
                throw new IllegalArgumentException("There are no dependent " + //$NON-NLS-1$
                    "elements for elements of type: " + policyElement); //$NON-NLS-1$
            }

            if(assets.size()>1) {
                assets.add(ODOMElement.UNDEFINED_ELEMENT_NAME);
            }

            xPathBuffer.append("ancestor-or-self::"). //$NON-NLS-1$
                append(PREFIX).append(':');

            for(int i=0; i<assets.size(); i++) {
                xPathBuffer.append(assets.get(i));
                if(i<assets.size()-1) {
                    xPathBuffer.append("|ancestor-or-self::"). //$NON-NLS-1$
                            append(PREFIX).append(':');
                }
            }
        }

        XPath xPath = new XPath(xPathBuffer.toString(),
                new Namespace[] { MCSNamespace.LPDM });
        ODOMSelectionFilter filter = new ODOMSelectionFilter(xPath,
                new String[]{policyElementName});

        return filter;
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

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 14-Jul-04	4876/1	allan	VBM:2004062501 Fix asset selection filtering.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 22-Jan-04	2540/2	byron	VBM:2003121505 Added main formats attribute page

 22-Jan-04	2659/2	byron	VBM:2003121505 Added main formats attribute page

 22-Jan-04	2659/1	allan	VBM:2003112801 Fix selection filtering and add basic cell editing.

 ===========================================================================
*/
