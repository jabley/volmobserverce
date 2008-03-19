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

/**
 * A constraint requiring the existence or non-existence of a named
 * ancestor element within a specified namespace URI.
 */
public class AncestorContainment implements DOMConstraint {
    /**
     * An internally used namespace prefix for use within the XPath generated
     * to locate the ancestor element
     */
    private static final String PREFIX = "constrained"; //$NON-NLS-1$

    /**
     * The ancestor element name
     */
    private String ancestorElementName;

    /**
     * An XPath used to locate the named ancestor element
     */
    private XPath xpath;

    /**
     * The key to be used in reporting the violation to an error reporter
     */
    private String errorKey;

    /**
     * Whether the ancestor is required or must not exist
     */
    private boolean required;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param ancestorElementName the name of the ancestor element on which the
     *                            constraint is based
     * @param namespaceURI        the namespace URI in which the ancestor
     *                            exists
     * @param errorKey            the key to be used when reporting to the
     *                            error reporter
     * @param required            indicates that the named ancestor must exist
     *                            when true or must not exist when false
     */
    public AncestorContainment(String ancestorElementName,
                               String namespaceURI,
                               String errorKey,
                               boolean required) {
        Namespace[] namespaces = new Namespace[1];

        if (ancestorElementName == null) {
            throw new IllegalArgumentException(
                "A non-null ancestor element name must be specified"); //$NON-NLS-1$
        } else if (errorKey == null) {
            throw new IllegalArgumentException(
                "A non-null error key must be specified"); //$NON-NLS-1$
        }

        namespaces[0] = Namespace.getNamespace(PREFIX,
                                               namespaceURI);

        this.ancestorElementName = ancestorElementName;

        xpath = new XPath(
            new StringBuffer("ancestor::"). //$NON-NLS-1$
            append(PREFIX).
            append(':').
            append(ancestorElementName).toString(),
            namespaces);

        this.errorKey = errorKey;
        this.required = required;
    }

    /**
     * This constraint is violated as follows given the specified value for
     * {@link #required}:
     *
     * <dl>
     *
     * <dt>true</dt>
     *
     * <dd>when the current element is a decendent of an element with the
     * <dfn>ancestorElementName</dfn> passed to the constructor.</dd>
     *
     * <dt>false</dt>
     *
     * <dd>when the current element is not a decendent of an element with the
     * <dfn>ancestorElementName</dfn> passed to the constructor.</dd>
     *
     * </dl>
     */
    public boolean violated(Element element,
                            ErrorReporter errorReporter) {
        boolean result = false;

        if (required == (getAncestor(element) == null)) {
            result = true;

            report(errorReporter, element);
        }

        return result;
    }

    /**
     * This method returns the ancestor associated with the given element, or
     * null if there is none.
     *
     * @param element the element for which the ancestor is to be determined
     * @return the ancestor element or null if there is none
     */
    protected Element getAncestor(Element element) {
        try {
            return (Element)xpath.selectSingleNode(element);
        } catch (XPathException e) {
            // We should not have any XPath errors thrown here so re-throw as
            // unchecked
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * This method reports an error to the given error reporter, if available.
     *
     * @param errorReporter the error reporter to use. May be null
     * @param element       the element against which the violation is being
     *                      reported
     */
    protected void report(ErrorReporter errorReporter,
                          Element element) {
        if (errorReporter != null) {
            ErrorDetails details = new ErrorDetails(element, new XPath(element),
                    null, errorKey, ancestorElementName, null);
            errorReporter.reportError(details);
        }
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

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/
