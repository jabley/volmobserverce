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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.PolicyUtils;
import com.volantis.mcs.eclipse.common.odom.xpath.ODOMXPath;
import com.volantis.mcs.eclipse.controls.ItemContainer;
import com.volantis.mcs.eclipse.controls.ItemToolTipper;
import com.volantis.mcs.xml.xpath.XPath;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Item;
import org.jdom.Element;

/**
 * An ItemToolTipper for use on Items whose data is an ODOMElement.
 *
 * This class is required because of the difficulty in obtaining the Items
 * in a Tree. It is only possible to obtain the root level items or their
 * immeadiate children without using recursion to construct a list of
 * all the items. Since this list could change at any time it would need
 * to be re-created each time the list of items are required and this
 * creates a lot of garbage (e.g. every time the tree is refreshed which
 * could easily happen as much as once per second if for example a validation
 * listener was involved).
 *
 * This class will work for ItemContainers that are not trees such as tables
 * but it would be better to use ItemToolTipper for such ItemContainers since
 * there is no difficulty in obtaining all the items in the container.
 *
 * This class will only work for ItemContainers that contain ODOMElements and
 * it expects the data on the contained Items to be set to the ODOMElement
 * that corresponds to the Item.
 */
public class ODOMItemToolTipper extends ItemToolTipper {

    /**
     * The IResource associated with this ODOMItemToolTipper. This is used
     * to find problem markers from which the tool tip is obtained.
     */
    private final IResource resource;

    /**
     * Construct a new ODOMItemToolTipper for Items based on a given
     * IResource.
     * @param resource The IResource.
     * @param itemContainer The ItemContainer that contains the items for
     * this ItemToolTipper.
     */
    public ODOMItemToolTipper(IResource resource,
                              ItemContainer itemContainer) {
        super(itemContainer);
        this.resource = resource;
    }

    /**
     * Override getToolTipText() to use the ODOMElement that should be
     * on the Item to find problem Markers on the Resource that provide
     * the tooltip text.
     *
     * NOTE: This is a hack. There is no way to retrieve the item associated
     * with an element in an ItemContainer without some sort of hack e.g.
     * sub-class the viewer, use reflection to access the viewer, or this
     * method - there may be others. Basically there is no support for
     * tool tips on Items in SWT/JFace.
     *
     * The hack here assumes that the given element is set as the data
     * item on the Item in the ItemContainer that it corresponds to. This
     * is how StructuredViewers work so we are reasonably assuming this
     * ODOMItemTooltipper is being used within the context of a StructuredViewer
     * and rather dubiouly assuming that StructuredViewer will call
     * Item.setData(element) - it does at the moment but this is not public
     * api. This hack is used in preference to this others because we don't
     * have to know about the viewer and because it is less complicated.
     *
     * Note that the javadoc for ICellModifier suggests using item.getData() to
     * get the underlying element. Although somewhat dubious in comparison with
     * the javadoc for getData() this does add weight to the validity of
     * this hack.
     */
    // rest of javadoc inherited
    protected String getToolTipText(Item item) {
        String toolTipText = null;

        // Assume the the data on the item is the Element.
        Element element = (Element) item.getData();
        if (element != null) {
            // Look for problem markers associated with this element
            // from which to obtain the tool tip text.
            XPath xPath = new ODOMXPath(element);
            try {
                IMarker markers [] = PolicyUtils.findProblemMarkers(resource,
                        xPath);
                if (markers.length > 0) {
                    toolTipText = (String) markers[0].
                            getAttribute(IMarker.MESSAGE);
                }
            } catch (CoreException e) {
                EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
            }
        }

        return toolTipText;
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

 22-Jan-04	2659/1	allan	VBM:2003112801 Fix selection filtering and add basic cell editing.

 19-Jan-04	2562/3	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 ===========================================================================
*/
