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
package com.volantis.mcs.eclipse.ab.actions.layout;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.jdom.Element;

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;


/**
 * This is the Delete action command appropriate to the Layout Outline page and
 * the Layout Graphical Editor page. It allows an arbitrary number of elements
 * to be deleted, but doesn't allow deletion of the layout document root
 * element.
 */
public class DeleteActionCommand extends LayoutActionCommand {

    /**
     * stateless comparator, declared static final to save on garbage
     */
    private static final Comparator childrenFirstComparator =
            new JDOMChildrenFirstComparator();

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param selectionManager The selectionManager for the DOM you are
     * modifying. Can not be null.
     */
    public DeleteActionCommand(ODOMSelectionManager selectionManager) {
        super(selectionManager);
    }


    /**
     * Delete is available with any non-empty selection that doesn't contain
     * the root element.
     */
    public boolean enable(ODOMActionDetails details) {
        boolean result = (details.getNumberOfElements() > 0);

        if (result) {        	        	       	       	
            // Only need to disable the action if the root node is selected
            for (int i = 0;
                 result && (i < details.getNumberOfElements());
                 i++) {
                result = (details.getElement(i).getParent() != null);
            }
        }

        //deletion of empty format does nothing so we disable that
        if(details.getNumberOfElements() == 1 && details.getElement(0).getName().equals(
        		FormatType.EMPTY.getElementName())) {
        	result = false;
        }
        
        return result;
    }


    /**
     * Delete simply removes all selected elements from the document.
     * <p>
     * To support undo/redo children nodes are deleted before parents.
     */
    public void run(ODOMActionDetails details) {
        ODOMElement element;
        Element parent;

        // javadoc for details.getElements() tells us we get a clone,
        // so we can sort that array directly
        ODOMElement[] detailsElementsClone = details.getElementsClone();
        sortChildrenFirst(detailsElementsClone);

        for (int i = 0;
             i < detailsElementsClone.length;
             i++) {
            element = detailsElementsClone[i];
            parent = element.getParent();

            // Delete can be used to delete formats or device layouts, but
            // the processing is different in these two cases
            if (parent.getParent() != null) {
                // Only non-empty formats need to be handled (deletion of
                // an empty format does nothing)
                if (!element.getName().equals(
                        FormatType.EMPTY.getElementName())) {
                    // A format is being deleted as opposed to a device
                    // layout so an empty format must take its place
                    List content = parent.getContent();
                    int index = content.indexOf(element);

                    // This automatically detaches the original element
                    content.set(index, FormatPrototype.get(FormatType.EMPTY));
                }
            } else if (parent != null) {
                // Simply remove the device layout
                element.detach();
            }
        }
    }


    /**
     * package access for unit testing
     * @param elements will be sorted children first
     */
    static void sortChildrenFirst(Element[] elements) {
        Arrays.sort(elements, childrenFirstComparator);
    }


    /**
     * Compares JDOM Elements so that children come before parents.
     * Unrelated nodes are considered equals with respect to sort order.
     */
    private static class JDOMChildrenFirstComparator implements Comparator {

        //javadoc inherited
        public int compare(Object o1, Object o2) {

            Element e1 = (Element) o1;
            Element e2 = (Element) o2;

            if (e1.isAncestor(e2)) {
                return -1;
            }

            if (e2.isAncestor(e1)) {
                return 1;
            }

            return 0;
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 11-Feb-04	2939/1	eduardo	VBM:2004020506 ODOM DeleteActionCommand changed to be undo/redo friendly

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
