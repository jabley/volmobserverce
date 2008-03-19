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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * An implementation of the StyleEmulationElementConfiguration interface.<p>
 *
 * This class provides a mechanism of easily setting up the configuration data
 * for a particular protocol. If necessary, the configuration may be modified
 * by sub-classes.
 */
public class DefaultStyleEmulationElementConfiguration
        implements StyleEmulationElementConfiguration {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(DefaultStyleEmulationElementConfiguration.class);


    /**
     * The map stylistic element to permitted elements for that stylistic element.
     */
    private final Map stylisticElementMap;

    /**
     * The set of divisible elements that may contain stylistic elements.
     */
    private final Set divisibleElementsThatPermitStyles;

    /**
     * The set of indivisible elements that may contain stylistic elements.
     */
    private final Set indivisibleElementsThatPermitStyles;

    /**
     * The map of anti elements. For example, ANTI-B maps to b.
     */
    private final Map antiElementMap;

    /**
     * The set of mergeable elements such as 'font'
     */
    private final Set mergeableElements;

    /**
     * Create an instance of this class.
     */
    public DefaultStyleEmulationElementConfiguration() {
        // The ordering of the stylistic and atomic elements is important hence
        // the use of TreeMaps
        stylisticElementMap = new TreeMap();
        divisibleElementsThatPermitStyles = new HashSet();
        indivisibleElementsThatPermitStyles = new HashSet();
        antiElementMap = new HashMap();
        mergeableElements = new HashSet();

        // The set of indivisible MCS elements
        addIndivisibleElementsThatPermitStyles(
                new String[]{ DissectionConstants.KEEPTOGETHER_ELEMENT,
                              DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT
                });

        //@todo later Investigate whether the algorithm can be improved based
        //@todo       on the ordering of the elements because of differences
        //@todo       found when migrating from jdk 1.3 to 1.4
    }

    // javadoc inherited
    public boolean isAntiElement(String element) {
        return antiElementMap.keySet().contains(element);
    }

    // javadoc inherited
    public boolean isIndivisibleElement(String element) {
        boolean indivisible = true;
        // NOTE: All elements that are not stylistic or in the divisibleElements
        // set are classified as indivisible. The indivisible set includes
        // anti-elements and MCS KEEPTOGETHER/DISSECTABLE_CONTENTS.
        if (isStylisticElement(element) || isAntiElement(element) ||
                divisibleElementsThatPermitStyles.contains(element)) {
            indivisible = false;
        }
        return indivisible;
    }

    // javadoc inherited
    public boolean isMergeableElement(String element) {
        return mergeableElements.contains(element);
    }

    // javadoc inherited
    public boolean isStylisticElement(String element) {
        return (element != null &&
                stylisticElementMap.keySet().contains(element));
    }

    // javadoc inherited
    public boolean isStylePermittedInElement(String element) {
        boolean stylePermitted = false;
        if ((isStylisticElement(element) ||
                 divisibleElementsThatPermitStyles.contains(element) ||
                 indivisibleElementsThatPermitStyles.contains(element))) {
            stylePermitted = true;
        }
        return stylePermitted;
    }

    // javadoc inherited
    public boolean isDivisibleStyleElement(String element) {
        return divisibleElementsThatPermitStyles.contains(element);
    }

    // javadoc inherited
    public Iterator getCounterpartElementNames(String antiElement) {
        return ((Set)antiElementMap.get(antiElement)).iterator();
    }

    // javadoc inherited
    public boolean isCounterpart(String antiElement,
                                 String antiElementCounterpart) {
        return ((Set)antiElementMap.get(antiElement)).contains(antiElementCounterpart);
    }

    // javadoc inherited
    public boolean isContainmentPermitted(String element, String childElement) {
        boolean permitted = false;
        Set permittedElements = (Set)stylisticElementMap.get(element);
        if (permittedElements == null) {
            throw new IllegalArgumentException(
                    "Unable to obtain permitted elements for: " + element);
        } else {
            permitted = permittedElements.contains(childElement);
        }
        return permitted;
    }

    /**
     * Associate the stylistic element with the elements it may contain.
     *
     * @param elementName the stylistic element name.
     * @param antiElement the anti-element name.
     * @param permittedChildren the set of elements it may contain.
     */
    public void associateStylisticAndAntiElements(
            String elementName,
            String antiElement,
            Set permittedChildren) {

        if (elementName == null) {
            throw  new IllegalArgumentException(
                    "Parameter may not be null: elementName");
        }
        if (antiElement != null) {
            associateAntiElement(antiElement, elementName);
        }
        HashSet permittedChildrenSet = (HashSet)stylisticElementMap.get(
                elementName);

        if (permittedChildrenSet != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Element (" + elementName + ") has already been " +
                             "associated with permitted children. Permitted " +
                             "children will be merged with existing " +
                             "permitted children.");
            }
        }

        if (permittedChildren != null) {
            final int numPermittedChildren = permittedChildren.size();
            if (numPermittedChildren > 0) {
                if (permittedChildrenSet == null) {
                    permittedChildrenSet = new HashSet(numPermittedChildren);
                }
                permittedChildrenSet.addAll(permittedChildren);
            }
        }
        stylisticElementMap.put(elementName, permittedChildrenSet);
    }

    /**
     * Associate an anti-element with a stylistic element.
     *
     * @param antiElementName the anti-element name.
     * @param elementName  the stylistic element's name.
     */
    private void associateAntiElement(String antiElementName,
                                     String elementName) {
        if (elementName != null) {
            Set elementNameSet = (Set)antiElementMap.get(antiElementName);
            if (elementNameSet == null) {
                elementNameSet = new HashSet(1);
                antiElementMap.put(antiElementName, elementNameSet);
            }
            elementNameSet.add(elementName);
        }
    }

    /**
     * Add divisible elements that permit styles.
     *
     * @param elements an array of element names that permit styles and are
     *                 divisible.
     */
    public void addDivisibleElementsThatPermitStyles(String[] elements) {
        divisibleElementsThatPermitStyles.addAll(Arrays.asList(elements));
    }

    /**
     * Add indivisible elements that permit styles.
     *
     * @param elements an array of element names that permit styles and are
     *                 indivisible.
     */
    public void addIndivisibleElementsThatPermitStyles(String[] elements) {
        indivisibleElementsThatPermitStyles.addAll(Arrays.asList(elements));
    }

    /**
     * Add a mergeable element.
     * @param elementName the mergeable element's name.
     */
    public void addMergeableElement(String elementName) {
        if (mergeableElements.contains(elementName)) {
            throw new IllegalArgumentException("Mergeable element " +
                    elementName + " has already been added.");
        }
        mergeableElements.add(elementName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 29-Oct-04	5877/11	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 28-Oct-04	5877/9	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements - rework issues

 27-Oct-04	5877/7	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 26-Oct-04	5877/5	byron	VBM:2004101911 Style Emulation: fix definition and implementation of Atomic Elements

 01-Sep-04	5341/1	claire	VBM:2004090101 New Build Mechanism : MCS support for JDK 1.4.2_05

 21-Jul-04	4752/6	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - fix anti-size in WML

 14-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 14-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 ===========================================================================
*/
