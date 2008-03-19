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

import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.List;

/**
 * This constraint applies a mandatory containment constraint and then ensures
 * that only up to a specified number of the given element are contained within
 * the mandatory container.
 */
public class LimitedMandatoryContainment implements DOMConstraint {
    /**
     * An internally used namespace prefix for use within the XPath generated
     * to locate the child elements
     */
    private static final String PREFIX = "limited"; //$NON-NLS-1$

    /**
     * The ancestor element name
     */
    private String ancestorElementName;

    /**
     * The mandatory containment constraint associated with this limiting
     * constraint
     */
    private AncestorContainment containmentConstraint;

    /**
     * The maximum number of like-named elements that may exist within the
     * ancestor defined by the containment constraint
     */
    private int maxCount;

    /**
     * The key to be used in reporting a limit violation
     */
    private String errorKey;

    /**
     * A cache of the last element name for which this constraint was used
     */
    private String lastElementName;

    /**
     * A cache of the XPath used to find all decendents of the containment
     * constraint's ancestor
     */
    private XPath xpath;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param ancestorElementName         the mandatory ancestor element's
     *                                    name
     * @param ancestorElementNamespaceURI the mandatory ancestor element's
     *                                    namespace URI
     * @param ancestorErrorKey            the error key to be used when
     *                                    reporting an ancestor constraint
     *                                    violation to the error reporter
     * @param maxCount                    the maximum number of elements with
     *                                    the given name that may exist in the
     *                                    <dfn>ancestorElementName</dfn>
     * @param errorKey                    the key to be used when reporting a
     *                                    count violation to the error
     *                                    reporter
     */
    public LimitedMandatoryContainment(
        String ancestorElementName,
        String ancestorElementNamespaceURI,
        String ancestorErrorKey,
        int maxCount,
        String errorKey) {
        if (maxCount <= 0) {
            throw new IllegalArgumentException(
                "The max count must be a natural number (given " + //$NON-NLS-1$
                maxCount + ")"); //$NON-NLS-1$
        } else if (errorKey == null) {
            throw new IllegalArgumentException(
                "A non-null count error key is required"); //$NON-NLS-1$
        }

        this.containmentConstraint =
            new AncestorContainment(ancestorElementName,
                                    ancestorElementNamespaceURI,
                                    ancestorErrorKey,
                                    true);
        this.maxCount = maxCount;
        this.errorKey = errorKey;
        this.ancestorElementName = ancestorElementName;
    }

    /**
     * This constraint is violated if the current element is not a child of the
     * <dfn>ancestorElementName</dfn> passed to the constructor or there are
     * too many instances of the current element type within that ancestor.
     */
    public boolean violated(Element element,
                            ErrorReporter errorReporter) {
        // Check the containment constraint first
        boolean result = containmentConstraint.violated(element,
                                                        errorReporter);

        // OK so far: the element must be within the required ancestor for the
        // containment constraint
        if (!result) {
            // Determine if the count limit constraint has been violated.

            // Sadly, the following call means we have had to call getAncestor
            // twice (once here and once as a side-effect of checking violation
            // of the containment constraint).
            Element ancestor = containmentConstraint.getAncestor(element);
            List decendents;

            // Only calculate the XPath to find all instances of the current
            // type of element if necessary
            if ((xpath == null) ||
                !element.getName().equals(lastElementName)) {
                Namespace[] namespaces = new Namespace[1];

                lastElementName = element.getName();

                namespaces[0] = Namespace.getNamespace(
                    PREFIX,
                    element.getNamespaceURI());

                // This path looks for all instances of the named element
                // somewhere below the context node (i.e. the element passed
                // into the {@link XPath#selectNodes} method
                xpath = new XPath(
                    new StringBuffer(".//"). //$NON-NLS-1$
                    append(PREFIX).
                    append(':').
                    append(lastElementName).toString(),
                    namespaces);
            }

            // Execute the XPath to select all required decendent nodes
            try {
                decendents = xpath.selectNodes(ancestor);
            } catch (XPathException e) {
                throw new UndeclaredThrowableException(e);
            }

            // Check the count limit constraint
            if (decendents.size() > maxCount) {
                result = true;

                if (errorReporter != null) {
                    // @todo it would be nice to pass actual and max counts
                    ErrorDetails details = new ErrorDetails(element, new XPath(element),
                            null, errorKey, ancestorElementName, null);
                    errorReporter.reportError(details);
                }
            }
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

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 07-Apr-04	3778/1	byron	VBM:2004040706 Error while saving a Dissecting Pane - supplementary validation

 07-Apr-04	3776/1	byron	VBM:2004040706 Error while saving a Dissecting Pane - supplementary validation

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/
