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

import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.xml.xpath.XPath;

import org.eclipse.swt.dnd.Clipboard;
import org.jdom.Element;

/**
 * This is the Replace action command appropriate to the Layout Outline page
 * and the Layout Graphical Editor page. It allows a single element (with
 * content) from the clipboard to be pasted into a single selected target
 * non-empty format if the result of the paste would be valid. It also allows
 * replacement of one device layout with another from the clipboard.
 */
public class ReplaceActionCommand extends PasteActionCommand {
    /**
     * Initializes the new instance using the given parameters.
     *
     * @param clipboard the clipboard to be used by this action.
     * @param context the context to be used by this action. Can not be null.
     */
    public ReplaceActionCommand(Clipboard clipboard,
                                ODOMEditorContext context) {
        super(clipboard, context);
    }

    // javadoc inherited
    protected boolean enableForDeviceLayout(ODOMActionDetails details,
                                            Object content) {
        // Will only be enabled if the selection and the clipboard each contain
        // a single device layout
        boolean enable = (details.getNumberOfElements() == 1);

        if (enable) {
            Element element = details.getElement(0);

            enable = isDeviceLayout(element);

            if (enable) {
                element = singleElementFromContent(content);

                enable = (element != null) &&
                    isDeviceLayout(element);
            }
        }

        return enable;
    }

    /**
     * Called to determine whether the action should be enabled given a format
     * clipboard content and the specified action details.
     *
     * @param details the details as to the action's selection
     * @param content the clipboard content to be pasted
     * @return true if the action should be enabled
     */
    protected boolean enableForFormat(ODOMActionDetails details,
                                      Object content) {
        final Element sourceFormat = singleElementFromContent(content);
        boolean canEnable = false;

        // Determine if an element is selected.
        if (details.getNumberOfElements() == 1 &&
                !details.getElement(0).getName().equals(
                        FormatType.EMPTY.getElementName())) {
            // If so, then determine if the source (from the clipboard) could
            // replace the selected content.
            final XPath sourceFormatXPath = recreateXPathForClipboardContent(
                    sourceFormat, MCSNamespace.LPDM);
            canEnable = ActionSupport.canReplace(sourceFormat,
                    details.getElement(0), sourceFormatXPath);
        }
        return canEnable;    
    }

    /**
     * Supporting method that returns true if the given element is a device
     * layout.
     *
     * @param element the element to test
     * @return true if the element is a device layout
     */
    protected boolean isDeviceLayout(Element element) {
        final String name = element.getName();

        return name.equals(LayoutSchemaType.CANVAS_LAYOUT.getName()) ||
            name.equals(LayoutSchemaType.MONTAGE_LAYOUT.getName());

    }

    // javadoc inherited
    protected Element[] pasteDeviceLayout(ODOMActionDetails details,
                                          Object content) {
        Element[] elements = null;
        Element element = singleElementFromContent(content);

        if (element == null) {
            throw new IllegalStateException(
                "The clipboard content is not appropriate"); //$NON-NLS-1$
        } else {
            // The format pasted here is actually a device layout, but the
            // processing is the same in both of these cases
            pasteFormat(details.getElement(0),
                        element);

            elements = new Element[] {element};
        }

        return elements;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/1	matthew	VBM:2004041406 reduce flicker in layout designer

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 27-Jan-04	2705/1	philws	VBM:2003121517 Full implementation of Paste and Replace without unit tests

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
