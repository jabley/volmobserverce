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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.layouts.LayoutSchemaType;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

import java.util.Arrays;
import java.util.List;

public class ColumnODOMSelectionFilter extends ODOMSelectionFilter {

    /**
     * Create an ODOMElement that is a placeholder for no matches for this
     * filter. This is necessary because the obvious choice (null value) is
     * ignored in {@link com.volantis.mcs.eclipse.common.odom.DefaultODOMElementSelectionProvider#update}.
     */
    private static final ODOMElement NON_COLUMN = new ODOMElement(
            ODOMElement.NULL_ELEMENT_NAME);

    /**
     * Store the list of possible parent elements.
     */
    private static List parentMatchList = Arrays.asList(new Object[]{
        LayoutSchemaType.GRID_FORMAT_ROWS_ELEMENT.getName(),
        LayoutSchemaType.SEGMENT_GRID_FORMAT_ROW_ELEMENT.getName()
    });

    /**
     * Store an element filter that is used to obtain the element index
     * predicate.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * Store the standard namespace prefix.
     */
    private static final String PREFIX = MCSNamespace.LPDM.getPrefix() + ":"; //$NON-NLS-1$

    /**
     * Store the xpath part 1 (up to and including the first '[').
     */
    private static final String XPATH_PART_1 = "../../" + //$NON-NLS-1$
            PREFIX + LayoutSchemaType.GRID_FORMAT_COLUMNS_ELEMENT.getName() + "/" + //$NON-NLS-1$
            PREFIX + LayoutSchemaType.GRID_FORMAT_COLUMN_ELEMENT.getName() + "["; //$NON-NLS-1$

    /**
     * Store the xpath part 2 (from the first ']' to the next '[').
     */
    private static final String XPATH_PART_2 = "]|../../" + //$NON-NLS-1$
            PREFIX + LayoutSchemaType.SEGMENT_GRID_FORMAT_COLUMNS_ELEMENT.getName() + "/" + //$NON-NLS-1$
            PREFIX + LayoutSchemaType.SEGMENT_GRID_FORMAT_COLUMN_ELEMENT.getName() + "["; //$NON-NLS-1$

    /**
     * Store the xpath part 3 (just the last ']').
     */
    private static final String XPATH_PART_3 = "]"; //$NON-NLS-1$

    /**
     * Store the namespaces used to construct the XPath.
     */
    private static final Namespace[] NAMESPACES = new Namespace[]{
        MCSNamespace.LPDM
    };

    /**
     * Default constructor that initializes the selection filter with no
     * resolver or array filter names.
     */
    public ColumnODOMSelectionFilter() {
        super(null, null);
    }

    // javadoc inherited
    public ODOMElement resolve(ODOMElement element) throws XPathException {
        ODOMElement resolved = NON_COLUMN;

        final Element parent = element.getParent();
        // If we are in a grid (parent element is a gridFormatRow or
        // segmentGridFormatRow) then continue resolving.
        if (parent != null && parentMatchList.contains(parent.getName())) {
            int index = getIndexPosition(element);
            if (index != -1) {
                resolved = resolvePathToElement(element, index);
            }
        }
        return resolved;
    }

    /**
     * Helper method to resolve the path to an ODOM element. We need to locate
     * the column element that this element obtains its attributes from. We do
     * this by selecting the node that matches the XPath:
     * <pre>
     *  '../../segmentGridFormatColumns/segmentGridFormatColumn[predicate] |
     *   ../../gridFormatColumns/gridFormatColumn[predicate]'
     * </pre>
     *
     * where the predicate is the index of the current element relative to its
     * siblings.
     *
     * @param element the odom element to use to resolve to.
     * @param index   the column index for the current element.
     * @return the resolved element, or NON_COLUMN if this cannot be resovled.
     * @throws com.volantis.mcs.xml.xpath.XPathException if the resolve process resolves to a non
     *                        ODOMElement or more than one element.
     */
    private ODOMElement resolvePathToElement(ODOMElement element, int index)
            throws XPathException {

        ODOMElement resolved = NON_COLUMN;
        StringBuffer xpath = new StringBuffer(XPATH_PART_1);
        xpath.append(index).append(XPATH_PART_2);
        xpath.append(index).append(XPATH_PART_3);

        XPath xpathResolver = new XPath(xpath.toString(), NAMESPACES);

        List resolvedElements = xpathResolver.selectNodes(element);
        if (resolvedElements != null && resolvedElements.size() > 0) {
            if (resolvedElements.size() == 1) {
                Object node = resolvedElements.get(0);
                if (node instanceof ODOMElement) {
                    resolved = (ODOMElement) node;
                } else {
                    throw new XPathException(
                            "Unexpected node type (" + node + ")"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } else {
                throw new XPathException(
                        "Element (" + element + ") was resolved to more " + //$NON-NLS-1$ //$NON-NLS-2$
                        "than one element."); //$NON-NLS-1$
            }
        }
        return resolved;
    }

    /**
     * Determine the index position of this current element relative to its
     * siblings. Note that if the element has two adjacent element siblings,
     * then this method should return 2 as the index position (the element
     * must be the second sibling).
     *
     * @param element the element to determine its index position relative to
     *                its siblings.
     * @return the index position or -1 if one cannot be determined.
     */
    private int getIndexPosition(ODOMElement element) {
        int result = -1;
        Element parent = element.getParent();
        if (parent != null) {
            result = parent.getContent(ELEMENT_FILTER).indexOf(element) + 1;
        }
        return result;
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

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 03-Feb-04	2815/3	byron	VBM:2003121507 Eclipse PM Layout Editor: Addressed rework issues

 03-Feb-04	2815/1	byron	VBM:2003121507 Eclipse PM Layout Editor: Format Attributes View: Column Page

 ===========================================================================
*/
