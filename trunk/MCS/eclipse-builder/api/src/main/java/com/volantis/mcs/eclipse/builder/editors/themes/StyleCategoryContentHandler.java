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
package com.volantis.mcs.eclipse.builder.editors.themes;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.List;
import java.util.ArrayList;

import com.volantis.styling.properties.StyleProperty;
import com.volantis.xml.sax.ExtendedSAXParseException;
import com.volantis.mcs.themes.StylePropertyDetails;

/**
 * {@link org.xml.sax.ContentHandler} for parsing the StyleCategories XML file.
 * This handler should be used to create the set of StyleCategory objects.
 */
public class StyleCategoryContentHandler extends DefaultHandler {

    /**
     * Local name of the containing categories element (see class comments)
     */
    private final static String CATEGORIES_ELEMENT_NAME = "styleCategories";

    /**
     * Local name of the category element (see class comments)
     */
    private final static String CATEGORY_ELEMENT_NAME = "styleCategory";

    /**
     * Local name of the property element (see class comments)
     */
    private final static String PROPERTY_ELEMENT_NAME = "styleProperty";

    /**
     * Local name of the name attribute that is common to all the elements
     */
    private final static String NAME_ATTRIBUTE_NAME = "name";

    /**
     * Local name of the name attribute that is common to all the elements
     */
    private final static String IS_SYNC_ATTRIBUTE_NAME = "isSynchronizable";

    /**
     * A list of the top level style categories
     */
    private final List styleCategories = new ArrayList();

    /**
     * The {@link StyleCategory} that is currently being populated
     */
    private StyleCategory currentCategory = null;

    /**
     * Returns the list of {@link StyleCategory} objects: this will never
     * be null but may be empty if parsing has not yet occurred
     * @return the list of <code>StyleCategory</code> objects, which may
     * be empty
     */
    public List getStyleCategories() {
        return styleCategories;
    }

    // javadoc inherited
    public void startElement(
        String uri, String localName, String qName, Attributes attributes)
        throws SAXException {

        // Is it a category element
        if (localName.equals(CATEGORY_ELEMENT_NAME)) {

            // Create a new category
            final StyleCategory newCategory = createStyleCategory(attributes);

            // If the current category is not null, then the new one must
            // be a child of the current one
            if (currentCategory != null) {
                currentCategory.addSubCategory(newCategory);
            }

            // Retain a reference to the new category
            currentCategory = newCategory;

        // Or is it a property element
        } else if (localName.equals(PROPERTY_ELEMENT_NAME)) {

            // Get the StylePropertyDetails object named by the element
            // (this is guaranteed to be non-null)
            final StyleProperty details = createStylePropertyDetails(attributes);

            // Get the list of StylePropertyDetails objects from the
            // category currently being processed (which may be empty
            // but may not be null) and add it in
            if (currentCategory != null) {
                final List detailsList = currentCategory.getProperties();
                detailsList.add(details);
            } else {
                throw new ExtendedSAXParseException(
                    "Property element with no parent category",
                    null);
            }
        }
    }

    // javadoc inherited
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        // Check for a category name
        if (localName.equals(CATEGORY_ELEMENT_NAME)) {

            // Ensure that we are processing a category at present (should
            // never be null if the XML is well-formed, but easy to check)
            if (currentCategory != null) {

                // Get the category's parent
                final StyleCategory categoryParent =
                    currentCategory.getParent();

                // Are we dealing with a top-level category?
                if (categoryParent == null) {

                    // Add the top-level category to the list, and set
                    // the current category to null to indicate that
                    // we are not currently processing one
                    styleCategories.add(currentCategory);
                    currentCategory = null;

                } else {

                    // Reached the end of a child category: since this
                    // child has already been added to the parent (in start
                    // element) there is nothing to do except note that the
                    // current category being processed IS the parent
                    currentCategory = categoryParent;
                }

            } else {
                // This should never occur, but raise it anyway
                throw new ExtendedSAXParseException("Premature category end", null);
            }
        }
    }

    // javadoc inherited
    public void error(SAXParseException exception) throws SAXException {
        // super does nothing, but we want to actually throw the exception
        throw exception;
    }

    // javadoc inherited
    public void fatalError(SAXParseException exception) throws SAXException {
        // super throws the exception: make this explicit to be consistent
        // with error(SAXParseException)
        throw exception;
    }

    /**
     * Creates and returns a StyleCategory based on the set of attributes
     * supplied (which originate from the XML parser).
     *
     * @param attributes The attributes of the element as supplied by
     * the XML parser
     * @return A new StyleCategory instance (never null)
     * @throws SAXParseException if the supplied attributes do not contain
     * sufficient date to construct the StyleCategory
     */
    private StyleCategory createStyleCategory(Attributes attributes)
        throws SAXParseException {

        // The name must definitely be present
        final String nameValue = attributes.getValue(NAME_ATTRIBUTE_NAME);
        if (nameValue == null || nameValue.length() == 0) {
            throw new ExtendedSAXParseException(
                "Missing attribute: " + NAME_ATTRIBUTE_NAME,
                null);
        }

        // Is synchronizable is optional, and the default is false, so
        // take a true value only if it is present and equal to true
        final String isSyncValue = attributes.getValue(IS_SYNC_ATTRIBUTE_NAME);
        final boolean isSyncBool =
            (isSyncValue != null && isSyncValue.equals("true"));

        // We now have enough info to construct the object
        return new StyleCategory(nameValue, isSyncBool);
    }

    /**
     * Attempts to look up a StylePropertyDetails instance from the fixed
     * set of values defined in that class. If not found, an Exception is
     * thrown; hence the return value is never null.
     *
     * @param attributes The attributes of the element as supplied by
     * the XML parser
     * @return The appropriate StylePropertyDetails object (never null)
     * @throws SAXParseException if the StylePropertyDetails object cannot
     * be found
     */
    private StyleProperty createStylePropertyDetails(
        Attributes attributes)
        throws SAXParseException {

        // The name must definitely be present
        final String nameValue = attributes.getValue(NAME_ATTRIBUTE_NAME);
        if (nameValue == null || nameValue.length() == 0) {
            throw new ExtendedSAXParseException(
                "Missing attribute: " + NAME_ATTRIBUTE_NAME,
                null);
        }

        // Use the name to look up the details
        final StyleProperty details =
            StylePropertyDetails.getStyleProperty(nameValue);
        if (details == null) {
            throw new ExtendedSAXParseException(
                "Unknown StylePropertyDetails: " + nameValue,
                null);
        }

        // Return the looked-up details
        return details;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
