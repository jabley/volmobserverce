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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;

import java.util.List;


/**
 * The ODOM Selection filter. This class should be immutable:
 *  - the state of the object is only modified at construction time
 *  - any modifications to the internal state should result in a new instance
 *    of this object.
 */
public class ODOMSelectionFilter {
    /**
     * List of filters. Each is a simple name as a String, can be null
     */
    protected String[] filteredNames;

    /**
     * XPath resolver, can be null
     */
    protected XPath resolver;

    /**
     * The configuration for this ODOMSelectionFilter.
     */
    private final ODOMSelectionFilterConfiguration configuration;


    /**
     * Create an instance of this class with the specified resolver and
     * filterNames.
     *
     * @param resolver    the XPath resolver.
     * @param filterNames an array of filterNames.
     * @throws IllegalArgumentException if the filterNames array contains a null
     *                                  name.
     */
    public ODOMSelectionFilter(XPath resolver, String[] filterNames)
            throws IllegalArgumentException {
        this(resolver, filterNames,
                new ODOMSelectionFilterConfiguration(false, false));
    }

    /**
     * Create an instance of this class with the specified resolver and
     * filterNames.
     *
     * @param resolver    the XPath resolver.
     * @param filterNames an array of filterNames.
     * @param configuration the ODOMSelectionFilterConfiguration for this
     * filter.
     * @throws IllegalArgumentException if the filterNames array contains a null
     *                                  name or configuration is null.
     */
    public ODOMSelectionFilter(XPath resolver, String[] filterNames,
                               ODOMSelectionFilterConfiguration configuration)
            throws IllegalArgumentException {
        setResolver(resolver);
        setFilters(filterNames);
        if(configuration==null) {
            throw new IllegalArgumentException("Cannot be null: configuration");
        }
        this.configuration = configuration;
    }

    /**
     * Set the filterNames for this selector. This method is private in order to
     * ensure the class is immutable.
     *
     * @param filterNames an array of element names to filter out. This can be
     *                    <code>null</code>
     * @throws IllegalArgumentException if the filterNames array contains a null
     *                                  name.
     */
    private void setFilters(String[] filterNames)
            throws IllegalArgumentException {
        filteredNames = cloneFilterNamesArray(filterNames);
    }

    /**
     * Internal method that will clone the names array of Strings, or return
     * null if the input names array is null.
     *
     * @param names the names array of String objects to clone.
     * @return a new array of the names String, or return null if the array is
     *         null.
     * @throws IllegalArgumentException if the filterNames array contains a null
     *                                  name.
     */
    private String[] cloneFilterNamesArray(String[] names)
            throws IllegalArgumentException {

        String[] clone = null;
        if (names != null) {
            clone = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                if (names[i] == null) {
                    throw new IllegalArgumentException("Filter name with array " + //$NON-NLS-1$
                            "index " + i + " may not be null."); //$NON-NLS-1$ //$NON-NLS-2$
                }
                clone[i] = names[i];
            }
        }
        return clone;
    }

    /**
     * Set the XPath resolvers for this selector. This method is private in
     * order to ensure the class is immutable.
     *
     * @param resolver an XPath resolver to resolve ODOM elements. This can be
     *                 <code>null</code>.
     */
    private void setResolver(XPath resolver) {
        this.resolver = resolver;
    }

    /**
     * Get the configuration for this ODOMSelectionFilter.
     * @return the ODOMSelectionFilterConfiguration for this filter.
     */
    public ODOMSelectionFilterConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Get the filtered name properties array as a clone which will ensure
     * immutabality of the array itself.
     *
     * @return a cloned version of filtered name properties array.
     */
    public String[] getFilteredNames() {
        return cloneFilterNamesArray(filteredNames);
    }

    /**
     * Get the XPath resolver.
     *
     * @return the XPath resolver.
     */
    public XPath getXPathResolver() {
        return resolver;
    }

    /**
     * Indicate whether an element should be included. The element name is
     * checked against each filter name in our list of filters. If the name
     * matches any of the filtered names, false is returned denoting that the
     * element should not be included. If the name does not match any of the
     * filter names, true is returned denoting that the element should be
     * included. An element with a null name will not match any element in the
     * filter.
     *
     * @param element the <code>ODOMElement</code> to filter.
     * @return true if the Element should be included, false otherwise.
     * @see ODOMElement
     */
    public boolean include(ODOMElement element) {
        boolean include = true;
        if (filteredNames != null) {
            String name = element.getName();
            if (name != null) {
                for (int i = 0; include && (i < filteredNames.length); i++) {
                    include = name.equals(filteredNames[i]) ?
                            configuration.isInclusionFilter():
                            !configuration.isInclusionFilter();
                }
            }
        }
        return include;
    }

    /**
     * Resolve an element using an XPath as the resolver.
     *
     * @param element the <code>ODOMElement</code> to resolve.
     * @return the resolved <code>ODOMElement</code> element or the element
     *         passed in if the resolver is null, or null if the result could
     *         not resolve to any elements.
     * @throws com.volantis.mcs.xml.xpath.XPathException if the resolver failed (the resolve resulted in
     *                        more than one element, or an Attribute or Text
     *                        node).
     * @see ODOMElement
     */
    public ODOMElement resolve(ODOMElement element) throws XPathException {
        ODOMElement resolved = element;
        if (resolver != null) {
            List resolvedElements = resolver.selectNodes(element);
            if (resolvedElements == null || resolvedElements.size() == 0) {
                resolved = null;
            } else {
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
        }
        return resolved;
    }

    // Javadoc inherited from Object
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (obj.getClass() != this.getClass())) {
            return false;
        }

        ODOMSelectionFilter filter = (ODOMSelectionFilter) obj;

        // Determine whether the filter arrays contents are equal
        boolean equals = filterArraysEqual(filteredNames, filter.filteredNames);
        if (equals) {
            if (getXPathResolver() != null) {
                equals = getXPathResolver().externalFormsEqual(filter.getXPathResolver());
            } else {
                equals = (filter.getXPathResolver() == null);
            }
        }

        // Determine whether the configurations are equal
        if(equals) {
            equals = getConfiguration().equals(filter.getConfiguration());
        }

        return equals;
    }

    /**
     * Determine whether the filter arrays' contents are equal ((both are null)
     * or (both not null and are the same length and contain the same values).
     *
     * @param firstArray  the first array
     * @param secondArray the second array.
     * @return true if they are equal, false otherwise.
     */
    private boolean filterArraysEqual(String[] firstArray, String[] secondArray) {

        boolean equal = true;
        if (firstArray != null) {
            // If my names are not null then the filter's names must be non-null
            // and the length of each should match.
            if ((secondArray == null) ||
                    (firstArray.length != secondArray.length)) {
                equal = false;
            } else {
                // Both are not null and are the same length.
                for (int i = 0; equal && i < firstArray.length; i++) {
                    if (!firstArray[i].equals(secondArray[i])) {
                        equal = false;
                    }
                }
            }
        } else {
            equal = (secondArray == null);
        }
        return equal;
    }

    // Javadoc inherited from Object
    public int hashCode() {
        int h = 0;
        if (filteredNames != null) {
            for (int i = 0; i < filteredNames.length; i++) {
                h += filteredNames[i].hashCode();
            }
        }
        h = h + (16 * ((getXPathResolver() == null) ? 0 :
                getXPathResolver().getExternalForm().hashCode()));
        return h;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/4	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 21-Dec-04	6524/2	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: Format Attributes View: Row Page

 21-Jan-04	2659/1	allan	VBM:2003112801 RuleSection basics (read only)

 27-Nov-03	2036/2	byron	VBM:2003111902 Element Selection implementation - added testcase and fixed bugs

 24-Nov-03	2005/2	byron	VBM:2003112006 Eclipse to ODOM events

 23-Nov-03	1974/1	steve	VBM:2003112006 ODOM Selection changes

 ===========================================================================
*/
