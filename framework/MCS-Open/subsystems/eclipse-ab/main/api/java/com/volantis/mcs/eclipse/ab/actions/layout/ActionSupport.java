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

import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.UndeclaredThrowableException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.JDOMFactory;

/**
 * Provides supporting methods for use in those actions that need to use a
 * pre-modified version of the required device layout in order to determine
 * their enablement.
 */
public class ActionSupport {

    public static final class ActionIdentifier {
        private ActionIdentifier() {
        }
    }

    public static final ActionIdentifier SWAP_ACTION =
            new ActionIdentifier();

    public static final ActionIdentifier REPLACE_ACTION =
            new ActionIdentifier();

    /**
     * A unique prefix used to identify the LPDM namespace in the special
     * XPaths defined here
     */
    private static final String PREFIX = "dlclone"; //$NON-NLS-1$

    /**
     * The set of namespaces used in the special XPaths defined here
     */
    private static final Namespace[] dlnamespace = new Namespace[]{
        Namespace.getNamespace(PREFIX,
                MCSNamespace.LPDM.getURI())};

    /**
     * A special XPath used to select the containing device layout for a given
     * format element.
     */
    private static final XPath containingDeviceLayout =
            new XPath(new StringBuffer("ancestor::").//$NON-NLS-1$
            append(PREFIX).
            append(":").//$NON-NLS-1$
            append("canvasLayout").
            append("|ancestor::").//$NON-NLS-1$
            append(PREFIX).
            append(":").//$NON-NLS-1$
            append("montageLayout").toString(),
                    dlnamespace);

    /**
     * Clones the device layout containing the given element and, if
     * successful, returns the equivalent element in the clone to that
     * originally supplied. The given element must be a format and not a layout
     * or device layout.
     *
     * @param element used to identify the device layout
     * @return an element equivalent to that given but this time in the clone.
     *         Will be null if the given element is not a format
     */
    public static Element cloneContainingDeviceLayout(Element element) {
        JDOMFactory factory = FormatPrototype.factory;
        Element equivalent = null;
        Element deviceLayout = getContainingDeviceLayout(element);

        if (deviceLayout != null) {
            Element layout = deviceLayout.getParent();
            Element newLayout = factory.element(layout.getName(),
                    layout.getNamespace());
            Element newDeviceLayout = (Element) deviceLayout.clone();

            // Put the new layout in a document to allow absolute XPaths
            // to operate correctly
            factory.document(newLayout);

            // Put the cloned device layout into the new layout
            newLayout.addContent(newDeviceLayout);

            // Locate the equivalent element in the clone to that
            // originally supplied
            equivalent = equivalentElement(element, newLayout);
        }

        return equivalent;
    }

    /**
     * Retrieve the ancestor device layout of the supplied element.
     *
     * @param element   whose device layout ancestor to find
     * @return Element device layout
     */
    public static Element getContainingDeviceLayout(Element element) {
        try {
            return (Element)containingDeviceLayout.selectSingleNode(element);
        } catch (XPathException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Finds the equivalent of the given original element within the given
     * clone hierarchy (i.e. accessed via the "same" XPath). The clone can be
     * any element from the clone hierarchy, but the clone hierarchy must only
     * contain a single device layout.
     *
     * @param original the original element for which the equivalent is
     *                 required
     * @param clone    an element within the clone hierarchy to be searched
     * @return the equivalently located element
     */
    public static Element equivalentElement(Element original,
                                            Element clone) {
        Element equivalent = null;

        // This generates an absolute path (with device layout predicate to be
        // removed). An absolute path can be applied anywhere in the clone
        // document structure to find the equivalent element. This assumes that
        // the clone is really in a fully defined document (such as that
        // returned by {@link cloneContainingDeviceLayout}) with just one
        // device layout.
        XPath path = new XPath(original);

        // The device layout predicate must be removed from the path (the clone
        // will only have one device layout) if there is one
        String pathText = path.getExternalForm();
        int secondSeparator = pathText.indexOf('/', 1);

        if (secondSeparator > 0) {
            secondSeparator = pathText.indexOf('/', secondSeparator + 1);

            if (secondSeparator > 0) {
                char deviceLayoutPathEnd =
                        pathText.charAt(secondSeparator - 1);

                if (deviceLayoutPathEnd == ']') {
                    path = new XPath(
                            new StringBuffer(
                                    pathText.substring(0,
                                            pathText.indexOf('['))).
                            append(pathText.substring(secondSeparator)).
                            toString(),
                            path.getNamespacesString());
                }

                try {
                    // Get the equivalent element
                    equivalent = path.selectSingleElement(clone);
                } catch (XPathException e) {
                    throw new UndeclaredThrowableException(e);
                }
            }
        }

        return equivalent;
    }

    /**
     * Utility method that determines if two elements share a common device
     * Layout - can either be a canvas layout or montage layout
     *
     * @param elementX The first element
     * @param elementY The second element
     * @return true if both the provided elemenets share a common device layout
     * @throws UndeclaredThrowableException if there is a problem with the XPath
     */
    public static boolean haveCommonLayout(Element elementX, Element elementY) {
        boolean haveCommonLayout = false;
        try {
            Element deviceLayout1 = null;
            Element deviceLayout2 = null;
            deviceLayout1 = containingDeviceLayout.
                    selectSingleElement(elementX);
            deviceLayout2 = containingDeviceLayout.
                    selectSingleElement(elementY);
            haveCommonLayout = deviceLayout1 != null &&
                    deviceLayout2 != null &&
                    deviceLayout1 == deviceLayout2;
        } catch (XPathException e) {
            throw new UndeclaredThrowableException(e);
        }
        return haveCommonLayout;
    }

    /**
     * Utility method that determines if element Y is within element X
     *
     * @param elementX The element which might contain element Y
     * @param elementY The element which might be contained within
     * @return true if element X is within element Y
     * @throws UndeclaredThrowableException if there is a problem with the XPath
     */
    public static boolean isDescendent(Element elementX, Element elementY) {
        boolean isDescendent = false;
        if (elementX != null && elementY != null) {
            Element ancestor = elementY.getParent();
            while (ancestor != null && !isDescendent) {
                isDescendent = ancestor == elementX;
                ancestor = ancestor.getParent();
            }
        }
        return isDescendent;
    }

    /**
     * Determine if a format can be replaced by another format.
     * @param sourceFormat the source format that will replace
     * @param targetFormat the target format that will be replaced
     * @param sourceFormatXPath XPath to the original source format - can't use
     * the supplied sourceFormat as it is a clone
     * @return true if sourceFormat can replace targetFormat; false otherwise
     */
    public static boolean canReplace(Element sourceFormat,
                                     Element targetFormat,
                                     XPath sourceFormatXPath) {

        boolean canReplace = !sourceFormat.getName().equals(
                FormatType.EMPTY.getElementName());

        if (canReplace) {
            // A single format can be pasted into the device layout, replacing
            // the current selection, if constraints are not violated
            Element clonedTargetFormat =
                    cloneContainingDeviceLayout(targetFormat);

            if (clonedTargetFormat != null) {

                Element newLayout = getContainingDeviceLayout(
                        clonedTargetFormat).getParent();
                try {
                    // Can't use equivalentElement to find the cloned source
                    // format because the sourceFormat is a clone itself and
                    // has no information about its position in the layout.
                    Element clonedSourceFormat = (Element)sourceFormatXPath.
                            selectSingleNode(newLayout);

                    List content = clonedTargetFormat.getParent().getContent();
                    final int targetIndex = content.indexOf(clonedTargetFormat);

                    final int index;
                    if (clonedSourceFormat != null) {
                        List sourceParentContent =
                                clonedSourceFormat.getParent().getContent();
                        index = sourceParentContent.indexOf(clonedSourceFormat);
                        sourceParentContent.set(index,
                                FormatPrototype.get(FormatType.EMPTY));
                    }

                    // Replace them (only after calculating both indexes - they
                    // could have the same parent).
                    content.set(targetIndex, sourceFormat);

                    canReplace = !LayoutConstraintsProvider.constraints.
                            violated(sourceFormat, null);

                } catch (XPathException e) {
                    throw new UndeclaredThrowableException(e);
                }
            } else {
                canReplace = false;
            }
        }

        return canReplace;
    }

    /**
     * Determine if two formats can be swapped.
     * @param formatElement1 one format element
     * @param formatElement2 the other format element
     * @return true if the given formats can be swapped; false otherwise.
     */
    public static boolean canSwap(Element formatElement1,
                                  Element formatElement2) {

        boolean valid = false;
        // check that they are both elements of the same device layout
        if (haveCommonLayout(formatElement1, formatElement2)) {
            // check that neither of the elements are in each other to
            // avoid trying to make an element a descendant of itself
            if (!isDescendent(formatElement1, formatElement2)
                    && !isDescendent(formatElement2,
                            formatElement1)) {

                // create a clone of the device layout and swap the two
                // elements
                Element clonedElement1 = cloneContainingDeviceLayout(formatElement1);
                Element clonedElement2 = cloneContainingDeviceLayout(formatElement2);

                // get parents
                Element clonedElement1Parent = clonedElement1.getParent();
                Element clonedElement2Parent = clonedElement2.getParent();

                // get the indexes of the elements
                int indexOfElement1 = clonedElement1Parent.getContent().
                        indexOf(clonedElement1);
                int indexOfElement2 = clonedElement2Parent.getContent().
                        indexOf(clonedElement2);

                // detatch cloned Elements
                clonedElement1.detach();
                clonedElement2.detach();

                // attatch them at the indexes of their swap partners
                clonedElement1Parent.getContent().add(indexOfElement1,
                        clonedElement2);
                clonedElement2Parent.getContent().add(indexOfElement2,
                        clonedElement1);

                // validate - return validation
                if (!LayoutConstraintsProvider.constraints.
                        violated(clonedElement1, null)) {
                    if (!LayoutConstraintsProvider.constraints.
                            violated(clonedElement2, null)) {
                        valid = true;
                    }
                }
            }
        }

        return valid;
    }

    /**
     * Replace the specified element with the replacement element.
     * <code>element</code> must have a valid parent. This method disables
     * the ODOMSelectionManager while it removes the old element. Then it
     * returns the Manager to its original state (either enabled or disabled)
     * it so that selectionEvents based on the add can/cannot be fired.
     * The manager is always enabled when this method exits.
     *
     * @param element the element to replace
     * @param replacement the replacement element
     */
    public static void pasteFormatElement(Element element, Element replacement,
                                          ODOMSelectionManager selectionManager) {
        assert(element != null);
        assert(replacement != null);
        try {
            Element parent = element.getParent();
            if (parent == null) {
                throw new IllegalArgumentException(
                        "The element \"" + element.getName() +
                        " must have a non-null parent");
            }
            List content = parent.getContent();

            selectionManager.setEnabled(false);
            replacement.detach();
            content.set(content.indexOf(element), replacement);

        } finally {
            // ensure the manger is re-enabled.
            selectionManager.setEnabled(true);
        }

        selectionManager.
                setSelection(Arrays.asList(new Element[]{replacement}));
    }

    /**
     * Swap two format elements.
     * @param element1 one of the Elements to swap
     * @param element2 the other Element to swap
     * @param selectionManager the ODOMSelectionManager
     */
    public static void swapFormatElements(Element element1,
                                          Element element2,
                                          ODOMSelectionManager selectionManager) {

        // get parents
        Element element1Parent = element1.getParent();
        Element element2Parent = element2.getParent();

        // get the indexes of the elements
        int indexOfElement1 = element1Parent.getContent().
                indexOf(element1);
        int indexOfElement2 = element2Parent.getContent().
                indexOf(element2);

        try {

            // Disable selection manager so the swap will not affect
            // selection
            selectionManager.setEnabled(false);


            // get the children
            List children1 = element1Parent.getContent();
            List children2 = element2Parent.getContent();

            // NOTE: If all the elements are detached from the parent
            // element (e.g. if the items being swapped are the only
            // two items) then SWT has a problem with the setSelection()
            // call later on because it only manages to find one of the
            // two items - i.e. TreeItem.getItems() only returns a single
            // item. The TreeItem.getItems() code ultimately relies on
            // OS level code so this problem may be limited to Linux since
            // that is where the issue was found. The workaround is to
            // ensure that the parent element always has at least one
            // element. This is done by detaching one element then
            // adding it. Then detaching the other element and adding it
            // rather than detaching both then adding both.
            element1.detach();
            children2.add(indexOfElement2, element1);
            element2.detach();
            children1.add(indexOfElement1, element2);

        } finally {
            selectionManager.setEnabled(true);

        }

        // Leave both swapped elements selected
        ArrayList elements = new ArrayList(2);

        elements.add(element2);
        elements.add(element1);

        selectionManager.setSelection(Collections.EMPTY_LIST);
        selectionManager.setSelection(elements);


    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8295/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 17-May-05	8213/2	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 02-Feb-05	6749/3	allan	VBM:2005012102 Drag n Drop support

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 15-Jul-04	4886/3	allan	VBM:2004052812 Tidied some more and added basic enable tests.

 15-Jul-04	4886/1	allan	VBM:2004052812 Workaround TreeItem.getItems() bug and tidy up.

 14-Jul-04	4833/1	tom	VBM:2004052812 Added Swap Functionality

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/
