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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.sgml;

import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.dtd.DTDBuilder;
import com.volantis.mcs.dom.xml.XMLDTDImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Builder for {@link SGMLDTDImpl}.
 */
public class SGMLDTDBuilder
        extends DTDBuilder {

    /**
     * Set of attribute names that are not treated as replaceable character
     * data.
     */
    private Set nonReplaceableAttributes;

    /**
     * Map from {@link String} to {@link ETD}.
     */
    private Map element2ETD;

    /**
     * Set of element names that have an optional end tag.
     */
    private Set optionalEndTags;

    /**
     * Initialise.
     */
    public SGMLDTDBuilder() {
        nonReplaceableAttributes = new HashSet();
        optionalEndTags = new HashSet();
        element2ETD = new HashMap();
    }

    /**
     * Set the model for the element with the specified name.
     *
     * @param name  The element name.
     * @param model The model.
     */
    public void setElementModel(String name, ElementModel model) {
        ETDImpl etd = getETD(name);

        if (etd.getElementModel() != ElementModel.UNKNOWN) {
            throw new IllegalStateException("Element '" + name +
                    "' has already has its content model set.");
        }
        etd.setElementModel(model);
    }

    /**
     * Set the content model of all the specified elements to the specified
     * model.
     *
     * @param elements The elements.
     * @param model The model.
     */
    public void setElementModel(String[] elements, ElementModel model) {
        for (int i = 0; i < elements.length; i++) {
            String element = elements[i];
            setElementModel(element, model);
        }
    }

    /**
     * Get the ETD for the element with the specified name.
     *
     * <p>If the ETD does not exist then one is created.</p>
     *
     * @param name The element name.
     * @return The ETD.
     */
    private ETDImpl getETD(String name) {
        ETDImpl etd = (ETDImpl) element2ETD.get(name);
        if (etd == null) {
            etd = new ETDImpl();
            element2ETD.put(name, etd);
        }
        return etd;
    }

    public DTD buildDTD() {
        // Only create an SGML DTD if there are characteristics of this DTD
        // that can only be supported within an SGML DTD.
        if (element2ETD.isEmpty() && nonReplaceableAttributes.isEmpty()
                && optionalEndTags.isEmpty()) {
            return new XMLDTDImpl(this);
        } else {

            optimizeETD(element2ETD);

            return new SGMLDTDImpl(this);
        }
    }

    /**
     * Optimize the ETD map to reduce memory usage.
     *
     * <p>There are potentially a lot of different instances of ETD but there
     * are only very few different variations. So in order to reduce memory
     * duplicate instances of ETD are replaced with a single unique instance.
     * This is similar to the flyweight pattern except that it is happening
     * after they have been created. The reason for doing this is because these
     * objects are going to have a long life span and so it is worth reducing
     * the number of instances that have to be managed by the garbage
     * collector.</p>
     *
     * @param element2ETD The Map to be optimized.
     * @return The optimized map.
     */
    private void optimizeETD(Map element2ETD) {
        Map uniqueETD = new HashMap();
        for (Iterator i = element2ETD.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            ETD etd = (ETD) entry.getValue();
            ETD unique = (ETD) uniqueETD.get(etd);
            if (unique == null) {
                uniqueETD.put(etd, etd);
            } else {
                entry.setValue(unique);
            }
        }
    }

    /**
     * Add an attribute that should be treated as non replaceable.
     *
     * <p>This applies to any use of this attribute on any element.</p>
     *
     * <p>Attribute values should always be treated as replaceable according
     * to the SGML specification so this should not be required but
     * unfortunately some devices do not work correctly.</p>
     *
     * @param attributeName The name of the attribute.
     */
    public void addNonReplaceableAttribute(String attributeName) {
        nonReplaceableAttributes.add(attributeName);
    }

    /**
     * Indicates that the end tag for the specified element is optional.
     *
     * @param element The element.
     */
    public void setEndTagOptional(String element) {
        ETDImpl etd = getETD(element);
        etd.setEndTagOptional(true);
    }

    Map getElement2ETD() {
        return element2ETD;
    }

    Set getNonReplaceableAttributes() {
        return nonReplaceableAttributes;
    }
}
