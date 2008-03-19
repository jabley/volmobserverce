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
import com.volantis.mcs.eclipse.ab.actions.ClipboardSupport;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMElementTransfer;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.xml.xpath.XPath;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.dnd.Clipboard;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * This is the Paste action command appropriate to the Layout Outline page and
 * the Layout Graphical Editor page. It allows a single element (with content)
 * to be pasted into a single selected target empty format if the result of the
 * paste would be valid.
 */
public class PasteActionCommand extends LayoutActionCommand {

    /**
     * The clipboard that this action command utilizes
     */
    protected Clipboard clipboard;

    /**
     * The root element of the document against which this action can be
     * executed.
     */
    protected ODOMEditorContext context;

    /**
     * A transfer identifier for formats in the clipboard. This conforms to the
     * processing performed in {@link TransferSupport#getTransferId}.
     */
    protected static final ODOMElementTransfer FORMAT_TRANSFER =
            new ODOMElementTransfer(FormatPrototype.factory,
                    TransferSupport.FORMAT_TRANSFER_ID);

    /**
     * A transfer identifier for device layouts. This conforms to the
     * processing performed in {@link TransferSupport#getTransferId}.
     *
     * @todo later avoid the hard-coding of the root element name
     */
    protected static final ODOMElementTransfer DEVICE_LAYOUT_TRANSFER =
            new ODOMElementTransfer(FormatPrototype.factory,
                    LayoutSchemaType.LAYOUT.getName());

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param clipboard the clipboard to be used by this action
     * @param context the editor context within which this action is performed.
     *  Can not be null.
     * @throws IllegalArgumentException if either argument is null.
     */
    public PasteActionCommand(Clipboard clipboard,
                              ODOMEditorContext context) {
        super(context);
        if (clipboard == null) {
            throw new IllegalArgumentException(
                    "A non-null clipboard is required"); //$NON-NLS-1$
        } else if (context == null) {
            throw new IllegalArgumentException(
                    "A non-null editor context is required"); //$NON-NLS-1$
        }

        this.clipboard = clipboard;
        this.context = context;
    }

    /**
     * Enables the action based on the type of clipboard content and the
     * action's selection content.
     */
    public boolean enable(ODOMActionDetails details) {
        boolean enable = false;
        try {
            selectionManager.setEnabled(false);
            // Object to receive the result of the clipboard content access
            Object[] data = new Object[1];
            ODOMElementTransfer transfer = clipboardTransfer(data);

            if (transfer == DEVICE_LAYOUT_TRANSFER) {
                enable = enableForDeviceLayout(details, data[0]);
            } else if (transfer == FORMAT_TRANSFER) {
                enable = enableForFormat(details, data[0]);
            }
        } finally {
            selectionManager.setEnabled(true);
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
                details.getElement(0).getName().equals(
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
     * {@link ClipboardSupport#copySelectionToClipboard} adds the xpath of the
     * cloned element to the {@link ClipboardSupport#XPATH} attribute to the
     * ODOMElements that are added to the clipboard. This can then be used to
     * recreate the XPath that described the original element, without
     * providing access to the original itself. This method will also remove
     * the xpath attribute so that it doesn't appear in the output.
     *
     * @param format    whose XPath to recreate
     * @param namespace in which the format appeared
     * @return XPath which identifies the element in a layout.
     */
    protected XPath recreateXPathForClipboardContent(Element format,
                                                     Namespace namespace) {

        final String externalForm =
                format.getAttribute(ClipboardSupport.XPATH).getValue();
        // Ensure that the xpath attribute does not appear in the output.
        format.removeAttribute(ClipboardSupport.XPATH);
        final Namespace[] namespaces = new Namespace[] {namespace};
        return new XPath(externalForm, namespaces);
    }

    /**
     * Called to determine whether the action should be enabled given a device
     * layout clipboard content and the specified action details.
     *
     * @param details the details as to the action's selection
     * @param content the clipboard content to be pasted
     * @return true if the action should be enabled
     */
    protected boolean enableForDeviceLayout(ODOMActionDetails details,
                                            Object content) {
        // The selection and the actual clipboard content is irrelevant except
        // for the extent that the latter is a set of elements
        return elementsFromContent(content) != null;
    }

    /**
     * Returns one of {@link #DEVICE_LAYOUT_TRANSFER}, {@link #FORMAT_TRANSFER}
     * or null depending on the clipboard content availability. The zeroth
     * indexed item in the given array parameter is updated with the available
     * data, if a non-null array with at least one index is given.
     *
     * @param data an optional array whose first item is updated with the
     *             result of the clipboard query if available or is set null
     *             otherwise. If null or of zero size this parameter is ignored
     * @return an ODOMElementTransfer instance or null
     */
    public ODOMElementTransfer clipboardTransfer(Object[] data) {
        ODOMElementTransfer transfer = null;
        Object result;

        if ((result = clipboard.getContents(DEVICE_LAYOUT_TRANSFER)) != null) {
            transfer = DEVICE_LAYOUT_TRANSFER;
        } else if ((result = clipboard.getContents(FORMAT_TRANSFER)) != null) {
            transfer = FORMAT_TRANSFER;
        }

        if ((data != null) && (data.length >= 1)) {
            // The caller has asked for the clipboard content to be returned,
            // so let's oblige them. This will return null if no appropriate
            // content was available
            data[0] = result;
        }

        return transfer;
    }

    /**
     * Performs the actual paste, updating the selection after doing so.
     */
    public void run(ODOMActionDetails details) {
        // Object to receive the result of the clipboard content access
        Object[] data = new Object[1];
        ODOMElementTransfer transfer = clipboardTransfer(data);

        if (transfer == DEVICE_LAYOUT_TRANSFER) {
            Element[] elements =
                    pasteDeviceLayout(details,
                            data[0]);

            setSelection(Arrays.asList(elements));
        } else if (transfer == FORMAT_TRANSFER) {
            // This assumes that there is at least one selected element and
            // that the clipboard contains one element to paste. These
            // conditions are checked by the {@link #enable} method
            Element element =
                    pasteFormat(details.getElement(0),
                            singleElementFromContent(data[0]));

            setSelection(Arrays.asList(new Element[]{element}));
        } else {
            throw new IllegalStateException(
                    "The clipboard content is not of an " + //$NON-NLS-1$
                    "appropriate type"); //$NON-NLS-1$
        }
    }

    /**
     * Gets the single element that can be pasted or returns null if the
     * content is not appropriate (e.g. not a single element).
     *
     * @param content the clipboard content
     * @return the replacement element or null
     */
    protected Element singleElementFromContent(Object content) {
        Element replacement = null;

        if ((content != null) &&
                content instanceof ODOMElement[] &&
                ((ODOMElement[]) content).length == 1) {
            replacement = ((ODOMElement[]) content)[0];
        }

        return replacement;
    }

    /**
     * Gets the multiple elements that can be pasted or returns null if the
     * content is not appropriate (e.g. not a set of elements).
     *
     * @param content the clipboard content
     * @return the replacement elements or null
     */
    protected Element[] elementsFromContent(Object content) {
        Element[] replacement = null;

        if ((content != null) &&
                content instanceof ODOMElement[]) {
            replacement = (Element[]) content;
        }

        return replacement;
    }

    /**
     * The content of the clipboard is pasted into the selection (replacing
     * it).
     *
     * @param element     the format that is the context of the paste
     *                    operation
     * @param replacement the element to be pasted from the clipboard
     * @return the new (top-level) element representing the pasted content
     */
    protected Element pasteFormat(Element element,
                                  Element replacement) {
        Element parent = element.getParent();

        if (replacement == null) {
            throw new IllegalStateException(
                    "The clipboard content is not appropriate"); //$NON-NLS-1$
        } else if (parent == null) {
            throw new IllegalArgumentException(
                    "The element \"" + element.getName() + //$NON-NLS-1$
                    " must have a non-null parent"); //$NON-NLS-1$
        } else {
            // do not fire a selection event
            replaceElement(element, replacement, false);
        }
        return replacement;
    }

    /**
     * Pastes the device layout(s) from the clipboard into the document.
     *
     * @param details the details for the action command
     * @param content the clipboard content
     * @return the (set of) pasted device layout(s)
     */
    protected Element[] pasteDeviceLayout(ODOMActionDetails details,
                                          Object content) {
        Element[] elements = elementsFromContent(content);

        if (elements == null) {
            throw new IllegalStateException(
                    "The clipboard content is not appropriate"); //$NON-NLS-1$
        } else {
            Element root = context.getRootElement();
            try {

                selectionManager.setEnabled(false);
                for (int i = 0; i < elements.length; i++) {
                    root.addContent(elements[i]);
                }
            } finally {
                // ensure its enabled
                selectionManager.setEnabled(true);
            }
        }

        return elements;
    }

    /**
     * Ensures that the GUI is updated with the new elements as the current
     * selection.
     *
     * @param elements the set of elements that should be selected
     */
    protected void setSelection(List elements) {
        context.getODOMSelectionManager().setSelection(elements);
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

 15-Jul-04	4886/1	allan	VBM:2004052812 Workaround TreeItem.getItems() bug and tidy up.

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 12-Feb-04	2924/1	eduardo	VBM:2004021003 editor actions demarcate UndoRedo UOWs

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/2	byron	VBM:2004012602 Address issues from review

 27-Jan-04	2705/2	philws	VBM:2003121517 Full implementation of Paste and Replace without unit tests

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
