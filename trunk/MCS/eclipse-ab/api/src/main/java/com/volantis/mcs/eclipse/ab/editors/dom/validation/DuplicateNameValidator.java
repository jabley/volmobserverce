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

package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.xml.jdom.Visitor;
import com.volantis.xml.jdom.Walker;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This supplementary validator checks all layout for duplicate names and
 * reports violations to the given error reporter.
 *
 * <p>It should be registered on the root node of layout documents.</p>
 */
public class DuplicateNameValidator
        implements DOMSupplementaryValidator {

    /**
     * The XPath used to identify name scope within the hierarchy.
     */
    private static final XPath XPATH = new XPath(".//" + MCSNamespace.LPDM.
            getPrefix() + ":" + FormatType.FRAGMENT.getElementName(),
            new Namespace[]{MCSNamespace.LPDM});

    /**
     * The DOM walker.
     */
    private Walker walker = new Walker();

    /**
     * A visitor that creates a map of duplicate names.
     * @see #listMap
     */
    private class LayoutVisitor implements Visitor {
        /**
         * Maintain a map that is keyed a composite value of the element type
         * and its non-null attribute value for the 'name' attribute. The value
         * of this is a list of elements (more than one element implies a
         * duplicate name).
         */
        private Map listMap = new HashMap();

        // javadoc inherited
        public Action visit(Element element) {
            Action resultingAction = Action.CONTINUE;
            if (element.getName().equals(FormatType.FRAGMENT.getElementName())) {
                resultingAction = Action.SKIP;
            } else {
                updateMap(element);
            }
            return resultingAction;
        }

        // javadoc inherited
        public Action visit(Attribute attribute) {
            return null;
        }

        // javadoc inherited
        public Action visit(Text text) {
            return null;
        }

        /**
         * Update the map for the given element creating the relevant sub-maps and
         * lists as necesary.
         *
         * @param element the element slot into the map structure.
         */
        private void updateMap(Element element) {
            String attributeValue = element.getAttributeValue("name");
            if (attributeValue != null) {
                CompositeKey key = new CompositeKey(element.getName(), attributeValue);
                List duplicates = (List) listMap.get(key);
                if (duplicates == null) {
                    duplicates = new ArrayList();
                    listMap.put(key, duplicates);
                }
                duplicates.add(element);
            }
        }

        /**
         * Getter for the map containing a list of duplicate elements.
         *
         * @return a map containing a list of duplicate elements.
         */
        public Map getDuplicates() {
            return listMap;
        }
    }

    /**
     * Provide an object that may be used a key in a Map composed of the element
     * type and element's attributes value.
     */
    private class CompositeKey {
        /**
         * Store the element type value that is used to compose this composite
         * key.
         */
        private String elementType;

        /**
         * Store the attribute value that is used to compose this composite key.
         */
        private String attributeValue;

        /**
         * Constructor that expects non-null element type and attribute value
         * parameters that is used to construct the composite key.
         * @param elementType the element type.
         * @param attributeValue the element's attribute value for 'name'.
         */
        CompositeKey(String elementType, String attributeValue) {
            if (elementType == null) {
                throw new IllegalArgumentException(
                        "Element type may not be null.");
            }
            if (attributeValue == null) {
                throw new IllegalArgumentException(
                        "Attribute Value may not be null.");
            }
            this.elementType = elementType;
            this.attributeValue = attributeValue;
        }

        // javadoc inherited.
        public int hashCode() {
            int result = 17;
            result = 37 * result + elementType.hashCode();
            result = 37 * result + attributeValue.hashCode();
            return result;
        }

        // javadoc inherited.
        public boolean equals(Object obj) {
            boolean result = false;
            if (this == obj) {
                result = true;
            } else {
                if (obj instanceof CompositeKey) {
                    CompositeKey key = (CompositeKey) obj;
                    result = this.elementType.equals(key.elementType) &&
                            attributeValue.equals(key.attributeValue);
                }
            }
            return result;
        }
    }

    // javadoc inherited
    public void validate(Element element, ErrorReporter errorReporter) {
        final String deviceLayoutName = "canvasLayout";

        LayoutVisitor layoutVisitor = new LayoutVisitor();

        // If the element is a layoutFormat
        if (element.getName().equals(LayoutSchemaType.LAYOUT.getName())) {
            Iterator iterator = element.getChildren().iterator();
            while (iterator.hasNext()) {
                Element child = (Element) iterator.next();
                if (child.getName().equals(deviceLayoutName)) {
                    findDuplicates(child, layoutVisitor);
                    reportDuplicates(errorReporter, layoutVisitor.getDuplicates());

                    // Reset the duplicates since we have just reported them.
                    layoutVisitor.getDuplicates().clear();
                }
            }
        } else {
            findDuplicates(element, layoutVisitor);
            reportDuplicates(errorReporter, layoutVisitor.getDuplicates());
        }
    }

    /**
     * Find any duplicates for the given element. Any duplicates found are
     * stored in the layout visitor itself.
     *
     * @param element       the topmost JDOM element used to find duplicate
     *                      names.
     * @param layoutVisitor the layout visitor used to nextHeader the children of the
     *                      element and store any duplicates found.
     */
    private void findDuplicates(Element element,
                                LayoutVisitor layoutVisitor) {
        try {
            walker.visit(element, layoutVisitor);

            List list = XPATH.selectNodes(element);

            for (int i = 0; i < list.size(); i++) {
                Element e = (Element) list.get(i);
                Iterator iterator = e.getChildren().iterator();
                while (iterator.hasNext()) {
                    walker.visit((Element) iterator.next(), layoutVisitor);
                }
            }
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

    /**
     * Report any duplicates found using the error reporter.
     *
     * @param errorReporter the error reporter used to report the errors.
     * @param map           the map of possible duplicates.
     */
    private void reportDuplicates(ErrorReporter errorReporter, Map map) {
        Iterator keys = map.keySet().iterator();
        // The key should be the element type.
        while (keys.hasNext()) {
            List duplicates = (List) map.get(keys.next());
            // More than one element in the list means that we have
            // an element with a duplicate name.
            if (duplicates.size() > 1) {
                for (int i = 0; i < duplicates.size(); i++) {
                    Element element = (Element) duplicates.get(i);
                    ErrorDetails details = new ErrorDetails(element, new XPath(element),
                            null, FaultTypes.DUPLICATE_NAME, null, null);
                    errorReporter.reportError(details);
                }
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/1	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 07-May-04	4068/5	allan	VBM:2004032103 Structure page policies section.

 06-May-04	4068/3	allan	VBM:2004032103 Structure page policies section.

 27-Apr-04	4059/1	byron	VBM:2004042205 Format names incorrectly flagged as duplicate across multiple device layouts

 02-Apr-04	3717/1	doug	VBM:2004022404 Fixed ClassCastException problem

 30-Mar-04	3614/1	byron	VBM:2004022404 Layout: Panes are allowed same names

 ===========================================================================
*/
