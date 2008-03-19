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
package com.volantis.mcs.eclipse.common;

import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.objects.PropertyValueLookUp;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Provides general policy-related utilities.
 */
public abstract class PolicyUtils {

    /**
     * Find any problem type IMarkers on an IResources that have a particular
     * line number attribute.
     *
     * @param resource The IResource to on which to find markers.
     * @param lineNumber The line number associated with the markers to find.
     * @return All the problem type IMarkers on this contexts IResource that
     * are assocatied with the given id. Will return an empty array if
     * there are no matching markers.
     * @throws CoreException If there is a problem
     * calling Resource.findMarkers.
     * @throws IllegalStateException If resource is null.
     */
    public static IMarker[] findProblemMarkers(IResource resource,
                                               int lineNumber)
            throws CoreException {

        if (resource == null) {
            throw new IllegalStateException("There is no resource on which" + //$NON-NLS-1$
                    "to find markers."); //$NON-NLS-1$
        }

        if (lineNumber < 0) {
            throw new IllegalArgumentException("Line number must be >= 0."); //$NON-NLS-1$
        }

        IMarker result [] = resource.findMarkers(IMarker.PROBLEM, true,
                IResource.DEPTH_INFINITE);

        if (result.length > 0) {
            // Now find all the problem markers that have an the specified
            // line number
            List resultList = new ArrayList();
            for (int i = 0; i < result.length; i++) {
                IMarker marker = result[i];
                int markerLineNumber =
                        marker.getAttribute(IMarker.LINE_NUMBER, -1);
                if (markerLineNumber == lineNumber) {
                    resultList.add(marker);
                }
            }

            result = new IMarker[resultList.size()];
            resultList.toArray(result);
        }

        return result;

    }

    /**
     * Find any problem type IMarkers on an IResource that have an
     * associated XPath attribute that is equal to or a child of the
     * specified XPath.
     *
     * @param resource The IResource to on which to find markers.
     * @param xPath The XPath used as a key to find IMarkers on the
     * IResource in this context.
     * @return All the problem type IMarkers on this contexts IResource that
     * are assocatied with the given XPath. Will return an empty array if
     * there are no matching markers.
     * @throws org.eclipse.core.runtime.CoreException If there is a problem
     * calling Resource.findMarkers.
     * @throws IllegalStateException If there is no IResource associated with
     * this context.
     */
    public static IMarker[] findProblemMarkers(IResource resource, XPath xPath)
            throws CoreException {

        final String xPathString = xPath.getExternalForm();
        return findProblemMarkers(resource,
                new XPathComparison() {
                    public boolean matched(String markerXPath) {
                        return markerXPath.startsWith(xPathString);
                    }
                });
    }

    /**
     * Find any problem type IMarkers on an IResource that have an
     * associated XPath attribute that is equal to a specified XPath (i.e.
     * this does not include XPaths that are children of the specified
     * XPath).
     *
     * @param resource The IResource to on which to find markers.
     * @param xPath The XPath used as a key to find IMarkers on the
     * IResource in this context.
     * @return All the problem type IMarkers on this contexts IResource that
     * are assocatied with the given XPath. Will return an empty array if
     * there are no matching markers.
     * @throws org.eclipse.core.runtime.CoreException If there is a problem
     * calling Resource.findMarkers.
     * @throws IllegalStateException If there is no IResource associated with
     * this context.
     */
    public static IMarker[] findSpecificProblemMarkers(IResource resource,
                                                       XPath xPath)
            throws CoreException {

        final String xPathString = xPath.getExternalForm();
        return findProblemMarkers(resource,
                new XPathComparison() {
                    public boolean matched(String markerXPath) {
                        return markerXPath.equals(xPathString);
                    }

                });
    }

    /**
     * Find any problem type IMarkers on an IResource that have an
     * associated XPath attribute that matches using an XPathComparison.
     *
     * @param resource The IResource to on which to find markers.
     * @return All the problem type IMarkers on this contexts IResource that
     * are assocatied with the given XPath using the XPathComparison. Will
     * return an empty array if there are no matching markers.
     * @throws org.eclipse.core.runtime.CoreException If there is a problem
     * calling Resource.findMarkers.
     * @throws IllegalStateException If there is no IResource associated with
     * this context.
     */
    private static IMarker[]
            findProblemMarkers(IResource resource,
                               XPathComparison comparison)
            throws CoreException {
        if (resource == null) {
            throw new IllegalStateException("There is no resource on which" + //$NON-NLS-1$
                    "to find markers."); //$NON-NLS-1$
        }

        IMarker result [] = resource.findMarkers(IMarker.PROBLEM, true,
                IResource.DEPTH_INFINITE);

        if (result.length > 0) {
            // Now find all the problem markers that have an XPath attribute
            // that is in the specified XPath.
            List resultList = new ArrayList();
            String xPathAttributeKey = XPath.class.getName();
            for (int i = 0; i < result.length; i++) {
                IMarker marker = result[i];
                String markerPath = (String)
                        marker.getAttribute(xPathAttributeKey);
                if (markerPath != null && comparison.matched(markerPath)) {
                    resultList.add(marker);
                }
            }

            result = new IMarker[resultList.size()];
            resultList.toArray(result);
        }

        return result;
    }


    /**
     * Command pattern for use in comparing XPaths.
     */
    private interface XPathComparison {
        public boolean matched(String markerXPath);
    }

    /**
     * Find any problem type IMarkers on an IResource that have an
     * associated XPath attribute that matches using an XPathComparison, and
     * that also matches the given element.
     * @param elementIdentifier identifier associated with the element against
     * which problem markers element to match
     * @param xPath the XPath to compare
     * @return All the problem type IMarkers on this context's IResource that
     * are associated with the given element and XPath, or an empty array
     * if there are no matching markers.
     * @throws IllegalArgumentException if resource, element or xPath is null
     * @throws org.eclipse.core.runtime.CoreException If there is a problem
     * accessing markers.
     */
    public static IMarker[] findProblemMarkers(IResource resource,
                                               final String elementIdentifier,
                                               XPath xPath)
            throws CoreException {
        if (resource == null) {
            throw new IllegalArgumentException("Cannot be null: resource.");
        }

        if (elementIdentifier == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "elementIdentifier.");
        }

        if (xPath == null) {
            throw new IllegalArgumentException("Cannot be null: xPath.");
        }

        // Retrieve all problem markers.
        IMarker result [] = resource.findMarkers(IMarker.PROBLEM, true,
                IResource.DEPTH_INFINITE);

        if (result.length > 0) {
            // Filter the markers based on the element and xpath.
            List resultList = new ArrayList();
            final String xpathValue = xPath.getExternalForm();
            final XPathComparison xcomp = new XPathComparison() {
                public boolean matched(String markerXPath) {
                    return markerXPath.startsWith(xpathValue);
                }
            };

            for (int i = 0; i < result.length; i++) {
                IMarker marker = result[i];

                final String elementAttrValue =
                        (String) marker.getAttribute(Element.class.getName());

                final String markerXPath =
                        (String) marker.getAttribute(XPath.class.getName());

                // If matching element then try to match the xpath.
                if (elementAttrValue != null) {
                    if (elementAttrValue.equals(elementIdentifier) &&
                            xcomp.matched(markerXPath)) {
                        resultList.add(marker);
                    }
                } else {
                    // Only matching on xpath as there is no element attribute
                    if (xcomp.matched(markerXPath)) {
                        resultList.add(marker);
                    }
                }
            }

            result = new IMarker[resultList.size()];
            resultList.toArray(result);
        }

        return result;
    }

    /**
     *
     * @param elementName The XML element name
     * @return The class that maps to elementName, or NULL
     * if no corresponding class exists
     */
    public static Class getPolicyClass(String elementName) {
        return PropertyValueLookUp.getClassForXMLElement(elementName);
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

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 27-Apr-04	3983/1	matthew	VBM:2004040203 Fixes to setFocusWith(XPath)

 16-Apr-04	3740/4	allan	VBM:2004040508 Rework issues.

 15-Apr-04	3740/2	allan	VBM:2004040508 UpdateClient/Server enhancements & fixes.

 05-Apr-04	3706/3	pcameron	VBM:2004040108 Variable name change

 05-Apr-04	3706/1	pcameron	VBM:2004040108 Fixes to PolicyUtils and MarkerGeneratingErrorReporter

 31-Mar-04	3664/3	pcameron	VBM:2004032202 A few tweaks to PolicyUtils, PolicyUtilsTestCase and MarkerGeneratingErrorReporter

 31-Mar-04	3664/1	pcameron	VBM:2004032202 Added a new findProblemMarkers and test

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 09-Jan-04	2515/1	allan	VBM:2004010513 Add hover annotation and mouse click handling to rulers.

 08-Jan-04	2431/1	allan	VBM:2004010404 Fix validation and display update issues.

 17-Dec-03	2219/2	doug	VBM:2003121502 Added dom validation to the eclipse editors

 02-Dec-03	2069/1	allan	VBM:2003111903 Basic ODOMEditorPart completed with skeleton ImageComponentEditor.

 03-Nov-03	1698/6	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 29-Oct-03	1698/3	pcameron	VBM:2003102411 Added classname from element name, and PolicyUtils

 ===========================================================================
*/
