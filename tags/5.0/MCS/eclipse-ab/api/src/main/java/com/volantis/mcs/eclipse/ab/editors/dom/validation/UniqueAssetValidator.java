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
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Attribute;
import org.jdom.Element;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidationProvider;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.objects.PropertyValueLookUp;
import com.volantis.mcs.layouts.LayoutSchemaType;

/**
 * This class provides supplementary validation that checks for uniqueness
 * the assets, and to report as errors any instances of non-unique assets. This
 * must be registered on the parent node of the various assets to be checked.
 */
public class UniqueAssetValidator implements DOMSupplementaryValidator {
    /**
     * This method returns the names of all the attributes present in the given
     * element whose names are present in the given list of attribute names
     * (which therefore acts as an attribute name "filter").
     *
     * @param element           The element for which a filtered subset of
     *                          attribute names are to be extracted
     * @param attribNamesFilter A list of attribute names such that only
     *                          attribute names from the element that are in
     *                          this list are extracted
     * @return The list of attribute names as described
     */
    private static List getFilteredAttribNames(
        Element element,
        List attribNamesFilter) {

        List result = new ArrayList();
        Iterator attribIter = element.getAttributes().iterator();
        while (attribIter.hasNext()) {
            final Attribute attribute = (Attribute) attribIter.next();
            if (attribNamesFilter.contains(attribute.getName())) {
                result.add(attribute.getName());
            }
        }
        return result;
    }

    /**
     * This method determines whether the attribute values of one element are
     * equal to another, which is true if and only if:
     *
     * <ol>
     *
     * <li>each element contains the same subset <dfn>s</dfn> (which may be the
     * full subset or the empty subset) of the attributes in the specified
     * attribute names filter, <strong>and</strong></li>
     *
     * <li>for each attribute <dfn>a</dfn> in <dfn>s</dfn>, <dfn>a</dfn>'s
     * value in the first element equals <dfn>a</dfn>'s value in the second
     * element</li>
     *
     * </ol>
     *
     * @param elementA          The first element to be compared as described
     * @param elementB          The second element to be compared as described
     * @param attribNamesFilter The names of the attributes that form the basis
     *                          of the comparison, which must be non-null and
     *                          non-empty
     * @return true if and only if the attributes are equal as described above
     */
    private static boolean attributesEqual(
        Element elementA,
        Element elementB,
        List attribNamesFilter) {
        // There must be SOME attribute names in the filter
        if (attribNamesFilter == null || attribNamesFilter.isEmpty()) {
            throw new IllegalArgumentException(
                "No identified attributes for " + elementA.getName()); //$NON-NLS-1$
        }

        // Get the names of the attributes in list A that are "germane", i.e.
        // that are part of the set of attribute names that form the basis
        // of the comparison (then do the same for B)
        final List filteredNameListA =
            getFilteredAttribNames(elementA, attribNamesFilter);
        final List filteredNameListB =
            getFilteredAttribNames(elementB, attribNamesFilter);

        // Assume the elements are non-equal unless proved otherwise    
        boolean attributesEqual = false;

        // If the sets of germane attributes are the same size and one
        // contains the other, then the sets are equal (this is a bit
        // quicker that testing A contains B && vice-versa)
        if (filteredNameListA.size() == filteredNameListB.size() &&
            filteredNameListA.containsAll(filteredNameListB)) {

            // Now assume equality unless proved otherwise
            attributesEqual = true;

            // The name lists are the same, so pick one and loop through it
            // and keep going as long as the values are the same
            final Iterator nameIter = filteredNameListA.iterator();
            while (attributesEqual && nameIter.hasNext()) {
                final String name = (String) nameIter.next();
                if (!elementA.getAttributeValue(name).equals(
                        elementB.getAttributeValue(name))) {
                    attributesEqual = false;
                }
            }
        }

        // Return the result
        return attributesEqual;
    }

    // javadoc inherited
    public void validate(Element element, ErrorReporter errorReporter) {
        if (element == null) {
            throw new IllegalArgumentException("element may not be null"); //$NON-NLS-1$
        }

        // Get the children of the deemed asset parent, i.e. the assets,
        // as an array for performance and to avoid endless casting
        // @todo later refactor to avoid this unnecessary garbage
        final Element[] assets =
            (Element[]) element.getChildren().toArray(new Element[0]);

        // First assume that none is duplicates
        final XPath[] duplicates = new XPath[assets.length];

        // Start comparing each pair of assets
        for (int outer = 0; outer < assets.length; outer++) {

            // Skip this one if it has already been marked as a duplicate
            // when it took the role of "inner" in a previous iteration
            // of the "outer" loop
            if (duplicates[outer] != null) {
                continue;
            }

            // Outer is not a duplicate yet, so do an inner loop through
            // all the ones after it
            for (int inner = outer + 1;
                inner < assets.length;
                inner++) {

                // Skip this one if it's already been marked as a duplicate
                if (duplicates[inner] != null) {
                    continue;
                }

                // Elements might be equal: get them
                final Element elementA = assets[outer];
                final Element elementB = assets[inner];

                // Elements deviceLayoutCanvasFormat and
                // deviceLayoutMontage format are a special case since a)
                // they do not have equivalent classes so their properties
                // are not available directly from PropertyValueLookUp and
                // b) their element names have no bearing on the equality
                // of the elements themselves from the point of view of this
                // method. When the xsd is updated to handle this kind of
                // validation we will remove this Class so while we are
                // waiting the most reliable and fastest solution to this
                // problem is to hardcode a specific case for these two
                // kinds of asset elements....
                String elementAName = elementA.getName();
                final String dlcf = LayoutSchemaType.CANVAS_LAYOUT.getName();
                final String dlmf = LayoutSchemaType.MONTAGE_LAYOUT.getName();

                if (elementAName.equals(dlcf) || elementAName.equals(dlmf)) {
                    elementAName = "deviceLayout"; //$NON-NLS-1$
                }

                String elementBName = elementB.getName();
                if (elementBName.equals(dlcf) || elementBName.equals(dlmf)) {
                    elementBName = "deviceLayout"; //$NON-NLS-1$
                }


                // If the element names are not the same, the elements
                // cannot be equal, so skip
                if (!elementAName.equals(elementBName) ||
                    ODOMElement.UNDEFINED_ELEMENT_NAME.equals(
                        elementAName) ||
                    ODOMElement.UNDEFINED_ELEMENT_NAME.equals(
                        elementBName)) {
                    // @todo later refactor to remove this horrid code style
                    continue;
                }

                final List identityAttributes =
                    PropertyValueLookUp.getIdentityAttributes(
                        elementAName);

                // Compare the attributes of one element against the
                // attributes of the other, also passing in the list of
                // names of the attributes that form the basis of the
                // comparison: if the attributes are equal, then the
                // elements must be as the element names are equal too
                final boolean elementsEqual =
                    attributesEqual(elementA, elementB, identityAttributes);

                // If they are equal, mark them both as duplicates
                if (elementsEqual) {
                    duplicates[outer] = new XPath(assets[outer]);
                    duplicates[inner] = new XPath(assets[inner]);
                }
            }
        }

        // Now we just report the duplicates
        for (int i = 0; i < duplicates.length; i++) {
            if (duplicates[i] != null) {
                ErrorDetails details = new ErrorDetails(element, duplicates[i],
                        null, FaultTypes.DUPLICATE_ASSET, null, null);
                errorReporter.reportError(details);
            }
        }
    }
    
    /**
     * Utility method for adding, as a supplementary validator, a new instance
     * of this class to the specified validation provider for the given
     * element. This element is expected to be the parent of the assets to be
     * checked for uniqueness.
     *
     * @param parentElement      The element on which the validator should be
     *                           registered. May not be null
     * @param validationProvider The validation provider against which a new
     *                           instance of this class will be registered. May
     *                           not be null
     */
    public static void addValidatorToProvider(
        Element parentElement,
        DOMSupplementaryValidationProvider validationProvider) {

        // Check arguments
        if (parentElement == null) {
            throw new IllegalArgumentException(
                "parentElement may not be null"); //$NON-NLS-1$
        } else if (validationProvider == null) {
            throw new IllegalArgumentException(
                "validationProvider may not be null"); //$NON-NLS-1$
        }

        validationProvider.addSupplementaryValidator(
            parentElement.getNamespaceURI(),
            parentElement.getName(),
            new UniqueAssetValidator());
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

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 08-Jan-04	2431/1	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2447/3	philws	VBM:2004010609 Fix UniqueAssetValidator and repackage it

 07-Jan-04	2426/7	richardc	VBM:2004010607 Refactored registration of UniqueAssetValidator

 07-Jan-04	2426/5	richardc	VBM:2004010607 Refactored registration of UniqueAssetValidator

 06-Jan-04	2323/2	doug	VBM:2003120701 Added better validation error messages

 04-Jan-04	2364/1	doug	VBM:2004010401 Fixed problem with ComboViewer set/getValue()

 31-Dec-03	2306/6	richardc	VBM:2003121723 Added UniqueAssetValidator and applied to AssetsSection

 ===========================================================================
*/
