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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.common.Convertors;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.NamedColor;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.controls.StyledGroup;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import org.jdom.Namespace;

import java.awt.*;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The superclass for all FormatComposites. Extend this and implement
 * getTopLevel() to return the top-level composite of the actual
 * specialization. This returned Composite is then placed inside a
 * Group widget by this superclass. Subclasses should define their
 * own public void dispose() methods to dispose of any system resources
 * not taken care of by super.dispose(), and also to remove any listeners
 * that the FormatComposite registered with ODOMElements.
 *
 * An alternative design would be to separate ISelectionProvider from
 * FormatComposite by providing a FormatViewer that is a ContentViewer.
 * The relationship between FormatViewer and FormatComposite is effectively the
 * same as that between TreeViewer and Tree. This would mean that setSelection
 * would take an array of FormatComposites rather than elements as well as
 * other similar changes. However, the only real benefit from this alternative
 * is that it is consistent with the way Eclipse JFace/SWT works. It would
 * be slower and use more memory. On the other hand since there would be
 * content and label providers the design may be more flexible. On balance
 * it is not likely that FormatComposite will need to be more flexible -
 * unlike a Tree, FormatComposite is designed specifically for FormatComposite
 * objects that have a well defined relationship with particular kinds of
 * layout element. The FormatViewer/FormatComposite approach would also take
 * longer to implement and as usual time is lacking.
 *
 * FormatComposites create and maintain their own layout. Setting a Layout
 * on a FormatComposite will cause an UnssuportedOperationException. This is
 * necessary because the layout is used to represent the cells of the
 * FormatComposite so it must have control over it. The layout of a
 * FormatComposite is a GridLayout.
 *
 * @todo Remove the mouse specific selection support & replace with
 *      generic keyboard & mouse
 */
public class FormatComposite extends StyledGroup implements ISelectionProvider {

    /**
     * The resource prefix for this object.
     */
    private static final String RESOURCE_PREFIX = "FormatComposite.";

    /**
     * The name representation for an unnamed FormatComposite.
     */
    private static final String UNNAMED =
            LayoutMessages.getString(RESOURCE_PREFIX +
            "unnamed");

    /**
     * The value of the default background colour. This must be either the
     * name of a NamedColor or an rgb value starting with a '#'
     * (e.g. #ee5587).
     */
    private static final String DEFAULT_BG_COLOR =
            LayoutMessages.getString(RESOURCE_PREFIX +
            "backgroundColor.default");

    /**
     * The value of the default border colour. This must be either the
     * name of a NamedColor or an rgb value starting with a '#'
     * (e.g. #ee5587).
     */
    private static final String DEFAULT_BORDER_COLOR =
            LayoutMessages.getString(RESOURCE_PREFIX +
            "borderColor.default");

    /**
     * The width of the selection indicator.
     */
    protected static final int SELECTION_LINE_WIDTH =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
            "selection.lineWidth").intValue();

    /**
     * The name of the "background colour" attribute.
     */
    static final String BACKGROUND_COLOUR_ATTR_NAME =
            LayoutSchemaType.BACKGROUND_COLOR_ATTRIBUTE.getName();

    /**
     * The name of the "border color" attribute.
     */
    static final String BORDER_COLOUR_ATTR_NAME =
            LayoutSchemaType.BORDER_COLOR_ATTRIBUTE.getName();

    /**
     * The name of the "border width" attribute.
     */
    static final String BORDER_WIDTH_ATTR_NAME =
            LayoutSchemaType.BORDER_WIDTH_ATTRIBUTE.getName();

    /**
     * The name of the "columns" attribute.
     */
    static final String COLUMNS_ATTR_NAME =
            LayoutSchemaType.GRID_COLUMNS_ATTRIBUTE.getName();

    /**
     * The name of the "name" attribute.
     */
    static final String NAME_ATTR_NAME =
            LayoutSchemaType.NAME_ATTRIBUTE.getName();

    /**
     * The name of the "rows" attribute.
     */
    static final String ROWS_ATTR_NAME =
            LayoutSchemaType.GRID_ROWS_ATTRIBUTE.getName();

    /**
     * The name for the active selection colour i.e. selected and focused
     */
    private static final String ACTIVE_SELECTION_COLOR =
            NamedColor.ACTIVECAPTION.getName();

    /**
     * The name of the inactive selection colour i.e. selected and not focused
     */
    private static final String INACTIVE_SELECTION_COLOR =
            NamedColor.INACTIVECAPTION.getName();

    /**
     * The resource prefix for the row/column.
     */
    private static String RESOURCE_PREFIX_ROW_COLUMN =
            RESOURCE_PREFIX + "rowColumn.";

    /**
     * The margin between the border line and anything that goes inside this.
     */
    private static final int INNER_MARGIN = 2;

    /**
     * Utility method for obtaining the rowColumn resource string with the
     * specified key
     *
     * @param key the key to use to obtain the resource value.
     * @return the resource value associated with this key.
     */
    private static String getRowColumnResourceString(String key) {
        return LayoutMessages.getString(RESOURCE_PREFIX_ROW_COLUMN + key);
    }

    /**
     * A MessageFormat that provides the rows x columns text for
     * FormatComposites that have multiple rows/columns.
     */
    private static final MessageFormat GRID_TEXT_FORMAT;

    static {
        // Initialize the message format with the pattern obtained from the
        // resource bundle. Pattern is '{0}, {1}' -> '1 Row, 2 Columns'
        GRID_TEXT_FORMAT = new MessageFormat(
                getRowColumnResourceString("pattern")); //$NON-NLS-1$

        // A choice format that formats the combination of the row and
        // column patterns using provided limits.
        double[] limits = {1, 2};
        ChoiceFormat rowChoiceFormat = new ChoiceFormat(limits, new String[]{
            getRowColumnResourceString("oneRow"), //$NON-NLS-1$
            getRowColumnResourceString("multipleRows"), //$NON-NLS-1$
        });
        ChoiceFormat columnChoiceFormat = new ChoiceFormat(limits, new String[]{
            getRowColumnResourceString("oneColumn"), //$NON-NLS-1$
            getRowColumnResourceString("multipleColumns"), //$NON-NLS-1$
        });
        // Set the message formatter to use the row and column formats.
        Format[] formats = {rowChoiceFormat, columnChoiceFormat};
        GRID_TEXT_FORMAT.setFormats(formats);
    }

    /**
     * The ODOMElement represented by the FormatComposite. Cannot be null.
     */
    protected ODOMElement element;

    /**
     * The FormatType of this FormatComposite. The DeviceLayoutFormatComposite
     * is the only concrete implementation that has a type of null.
     */
    protected final FormatType formatType;

    /**
     * The menu manager used to create the context menu for this composite.
     */
    protected final MenuManager menuManager;

    /**
     * The menu listener used to populate the menu generated by the menu
     * manager each time the menu is shown.
     */
    protected final IMenuListener contextMenuGeneratingListener;

    /**
     * The list of SelectionChangedListeners listening to selection changes on
     * this FormatComposite.
     */
    private ListenerList selectionChangedListeners = new ListenerList();

    /**
     * Flag indicating focus status.
     */
    private boolean hasFocus;

    /**
     * Flag indicating selection status.
     */
    private boolean isSelected;

    /**
     * The FormatComposite at the top of the hierarchy. This FormatComposite
     * reference is used for selection changes - without this callers
     * would need to listen to every FormatComposite or FormatComposites
     * would need to listen to every child which could be a lot of listeners
     * to handle. The SWT Tree widget works in a similar fashion.
     */
    private final FormatComposite root;


    /**
     * Flag indicating that an ISelectionChangedEvent is currently being
     * fired.
     */
    private boolean firingSelectionChangedEvent;

    /**
     * The name of this FormatComposite.
     */
    private String name;

    /**
     * Initializes the new instance using the given parameters. This
     * constructor is package protected as instances are only intended to be
     * created by the {@link FormatCompositeBuilder}.
     *
     * @param parent  the parent Composite. Cannot be null.
     * @param element the ODOM element to be represented. Cannot be null.
     * @param root    the FormatComposite at the root of the FormatComposite
     *                hierarchy. If this is null then this FormatComposite
     *                composite should be considered a root.
     * @param contextMenuGeneratingListener
     *                the menu listener used to populate the composite's
     *                context menu. May be null if no context menu is required
     * @throws IllegalArgumentException if parent or element is null.
     */
    FormatComposite(Composite parent,
                    final ODOMElement element,
                    FormatComposite root,
                    IMenuListener contextMenuGeneratingListener)
            throws IllegalArgumentException {
        super(parent, SWT.NONE);

        assert(element != null);

        this.element = element;
        this.formatType = FormatType.
                getFormatTypeForElementName(element.getName());
        this.contextMenuGeneratingListener = contextMenuGeneratingListener;

        if (contextMenuGeneratingListener != null) {
            // Create and initialize the menu manager to re-populate the menu
            // each time it is shown. The listener is expected to do the menu
            // populating
            menuManager = new MenuManager();
            menuManager.setRemoveAllWhenShown(true);
            menuManager.addMenuListener(contextMenuGeneratingListener);

            // Set up a dispose listener to make sure that these resources
            // are correctly disposed of
            addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent e) {
                    disposeResources();
                }
            });

            // Initialise the menu
            setMenu(menuManager.createContextMenu(this));
        } else {
            menuManager = null;
        }

        if (formatType == null) {
            throw new IllegalArgumentException("Could not find a FormatType " +
                    "for element " + element.getName());
        }

        // If root is null then this FormatComposite is considered the root.
        this.root = root != null ? root : this;

        // Initialize background colour, border colour and border width.
        initializeBackgroundAndBorder();

        // Initialize the layout for this FormatComposite
        initializeLayout();

        addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent event) {
                FormatComposite.this.root.setFocus(true);
            }

            public void focusLost(FocusEvent event) {
                FormatComposite.this.root.setFocus(false);
            }
        });

        // Set the text on the StyleGroup to the format type name.
        //Adds a MouseListener which listens for mouse button presses
        //over the FormatComposite.
        addMouse();

        updateText();

        updateName();
    }

    /**
     * Called by a dispose listener to make sure we tidy up our resources to
     * avoid memory leaks.
     */
    private void disposeResources() {
        if (contextMenuGeneratingListener != null) {
            // Tidy up to avoid memory leaks and to adhere to the Eclipse API
            // documented usage
            menuManager.removeMenuListener(contextMenuGeneratingListener);
            menuManager.dispose();
        }
    }

    /**
     * Update the text of this FormatComposite.
     */
    void updateText() {
        setText(getLocalizedText());
    }

    /**
     * Update the name of this FormatComposite.
     */
    void updateName() {
        String name = element.getAttributeValue(NAME_ATTR_NAME);

        if (getFormatType() == FormatType.EMPTY) {
            setName(getRowColTextForEmptyFormat(getRow(), getColumn()));
        } else if (formatType.getStructure() == FormatType.Structure.LEAF) {
            if (name == null) {
                this.name = UNNAMED;
            } else {
                this.name = name;
            }
        }
    }

    /**
     * Initialize the Layout for this FormatComposite.
     */
    private void initializeLayout() {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;

        if (formatType.getStructure() == FormatType.Structure.GRID) {
            try {
                layout.numColumns = element.
                        getAttribute(FormatComposite.COLUMNS_ATTR_NAME).
                        getIntValue();
            } catch (DataConversionException e) {
                throw new UndeclaredThrowableException(e, e.getMessage());
            }
        }

        super.setLayout(layout);
    }

    /**
     * Override setLayout throw an UnsupportedOperationException since the
     * Layout for the FormatComposite is handled by the FormatComposite.
     * @param layout the layout
     * @throws UnsupportedOperationException always.
     */
    public void setLayout(Layout layout) {
        throw new UnsupportedOperationException("The layout cannot be set" +
                "on a FormatComposite");
    }

    /**
     * Get the root FormatComposite.
     * @return the root FormatComposite of the FormatComposite hierarchy.
     */
    FormatComposite getRoot() {
        return root;
    }

    /**
     * Set the name of this FormatComposite. The name is displayed within
     * the FormatComposite.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Creates and adds a MouseListener to the FormatComposite. Subclasses
     * should override the public void addMouseListener(MouseListener listener)
     * method of the Control class so that the listener is added to the
     * appropriate widgets created by the subclass.
     */
    private void addMouse() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseDown(MouseEvent mouseEvent) {
                boolean newStatus = true;
                if ((mouseEvent.stateMask & SWT.CTRL) != 0) {
                    //Toggle selection status if <ctrl> key pressed
                    newStatus = !isSelected;
                } else {
                    // This FormatComposite has been specifcally selected
                    // and this should unselect all other FormatComposites.
                    root.unselect();
                }
                // Just set the focus directly rather than call setFocus()
                // since setFocus() will redraw everything that is selected
                // and setSelected() will also do this and this is the
                // very next line. Note that the reason hasFocus is set here
                // is because in SWT (on Linux at least) focus events are only
                // gained and lost on inner most Composites - for some reason.
                hasFocus = true;
                updateSelection(newStatus);
            }
        };
        this.addMouseListener(mouseAdapter);
    }

    /**
     * Set the selection of this FormatComposite and fire a
     * SelectionChangedEvent
     * @param selected true if this FormatComposite is selected; false
     * otherwise.
     */
    public void updateSelection(boolean selected) {
        setSelected(selected);
        root.fireSelectionChangedEvent();
    }

    /**
     * Implement the setSelection() method of ISelectionProvider
     * @param selection The ISelection representing the selection.
     * This ISelection is expected to be an IStructuredSelection containing
     * ODOMElements.
     */
    public void setSelection(ISelection selection) {
        if (!firingSelectionChangedEvent) {
            IStructuredSelection structuredSelection =
                    (IStructuredSelection) selection;

            // First unselect all FormatComposites.
            unselect();

            // Search this format and its decendents and set them to be
            // selected if their element is in the selection.
            Iterator iterator = structuredSelection.iterator();
            while (iterator.hasNext()) {
                ODOMElement selected = (ODOMElement) iterator.next();
                select(selected);
            }
        }
    }

    // ISelectionChangedListener method
    public void
            addSelectionChangedListener(ISelectionChangedListener listener) {
        selectionChangedListeners.add(listener);
    }

    /**
     * Implement the getSelection() method of ISelectionProvider. The
     * returned ISelection will be an IStructuredSelection containing all
     * the ODOMElements that are selected within this FormatComposite
     * (i.e. this FormatComposite and all of its children).
     */
    public ISelection getSelection() {
        List selection = new ArrayList();
        populateFCSelection(selection);

        return new StructuredSelection(selection);
    }

    /**
     * Recurse through the FormatComposite hierarchy of this FormatComposite
     * and add the elements of any selected FormatComposite to a List.
     * @param selection the List
     */
    private void populateFCSelection(List selection) {
        if (isSelected()) {
            selection.add(getElement());
        }
        // Though it is not possible to positively ensure that the
        // children are all FormatComposites (because SWT does not call
        // setParent() preferring to set the parent variable directly!)
        // we assume that this is the case since FormatComposites should
        // not contain any other Controls except FormatComposites.
        // However, if there are no children then getChildren() returns
        // an empty array of type Control.
        Control children [] = getChildren();
        for (int i = 0; i < children.length; i++) {
            ((FormatComposite) children[i]).populateFCSelection(selection);
        }
    }

    // ISelectionChangedListener method
    public void
            removeSelectionChangedListener(ISelectionChangedListener listener) {
        selectionChangedListeners.add(listener);
    }

    /**
     * Unselect this FormatComposite and all of its decendents.
     */
    protected void unselect() {
        if (!this.isDisposed()) {
            setSelected(false);
            Control children [] = getChildren();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    ((FormatComposite) children[i]).unselect();
                }
            }
        }
    }

    /**
     * Select the FormatComposite associated with a given ODOMElement
     * if it is the current FormatComposite or one of its decendents.
     * @param element The ODOMElement.
     * @return true if a selection was made; false otherwise.
     */
    protected boolean select(ODOMElement element) {
        boolean selected = false;
        if (getElement().equals(element)) {
            setSelected(true);
            selected = true;
        } else {
            Control children [] = getChildren();
            if (children != null) {
                for (int i = 0; i < children.length && !selected; i++) {
                    selected = ((FormatComposite) children[i]).select(element);
                }
            }
        }
        return selected;
    }

    /**
     * Sets the selection status of this FormatComposite, and forces a
     * repaint so that the selection is immediately indicated by the
     * "selection border".
     * @param selected the new selection status to set.
     */
    private void setSelected(boolean selected) {
        if (selected != isSelected) {
            isSelected = selected;

            GC gc = new GC(this);
            repaintSelectionBorder(gc);
            gc.dispose();
        }
    }

    /**
     * Fires a SelectionEvent to all registered selection listeners.
     */
    private void fireSelectionChangedEvent() {
        firingSelectionChangedEvent = true;
        SelectionChangedEvent event =
                new SelectionChangedEvent(root, root.getSelection());
        Object[] listeners = selectionChangedListeners.getListeners();
        if (listeners != null && listeners.length > 0) {
            for (int i = 0; i < listeners.length; i++) {
                if (listeners[i] != null) {
                    ((ISelectionChangedListener) listeners[i]).
                            selectionChanged(event);
                }
            }
        }
        firingSelectionChangedEvent = false;
    }

    /**
     * Gets the ODOMElement represented by this FormatComposite.
     * @return the ODOMElement
     */
    public ODOMElement getElement() {
        return element;
    }

    /**
     * Get the column position of this FormatComposite. If the parent is not
     * a FormatComposite the column is assumed to be 0.
     * @return the column position - this will have a range of 0 to no. of
     * columns - 1
     */
    public int getColumn() {
        int column = 0;
        if (getParent() instanceof FormatComposite) {
            FormatComposite parent = (FormatComposite) getParent();
            column = getIndex() % parent.getColumnCount();
        }
        return column;
    }

    /**
     * Get the row position of this FormatComposite. If the parent is not
     * a FormatComposite the row is assumed to be 0.
     * @return the row position - this will have a range of 0 to no. of rows
     * - 1
     */
    public int getRow() {
        int row = 0;
        if (getParent() instanceof FormatComposite) {
            FormatComposite parent = (FormatComposite) getParent();
            row = getIndex() / parent.getColumnCount();
        }
        return row;
    }

    /**
     * Get the format type of this FormatComposite.
     * @return the FormatType
     */
    public FormatType getFormatType() {
        return formatType;
    }

    /**
     * Get the elements if any that describe the rows of the element
     * associated with this FormatComposite (e.g. the gridFormatRow elements
     * if the element is a gridFormat).
     * @return a List of Element objects that represent rows of this
     * FormatComposite.
     */
    public List getRowElements() {

        Element element = getElement();
        String rowElementName;
        if (element.getName().equals(LayoutSchemaType.
                GRID_FORMAT_ELEMENT.getName())) {
            rowElementName = LayoutSchemaType.
                    GRID_FORMAT_ROWS_ELEMENT.getName();
        } else {
            rowElementName = LayoutSchemaType.
                    SEGMENT_GRID_FORMAT_ROW_ELEMENT.getName();
        }

        StringBuffer buffer =
                new StringBuffer(element.getNamespacePrefix().length() + 1 +
                rowElementName.length());
        buffer.append(element.getNamespacePrefix()).append(":").
                append(rowElementName);
        XPath rowsXPath = new XPath(buffer.toString(),
                new Namespace[]{element.getNamespace()});

        List gridRowFormats;
        try {
            gridRowFormats =
                    rowsXPath.selectNodes(element);
        } catch (XPathException e) {
            throw new UndeclaredThrowableException(e);
        }

        return gridRowFormats;
    }

    /**
     * Get the index of this FormatComposite within its parent Composite.
     * This method relies on getChildren() being in Z order.
     * @return the index of this FormatComposite within the parent Composite.
     * The range of index will be 0 to no. of children -1.
     */
    private int getIndex() {
        Control siblings [] = getParent().getChildren();
        int index = -1;
        for (int i = 0; i < siblings.length && index == -1; i++) {
            if (siblings[i] == this) {
                index = i;
            }
        }

        if (index == -1) {
            throw new IllegalStateException("Could not find " + this +
                    " in its parent " + getParent());
        }

        return index;
    }

    /**
     * Apply the background color, border color and border width attributes
     * of the element associated with this FormatComposite to set these
     * attributes on the FormatComposite.
     *
     * The background color will default to the default colour if not
     * available or invalid. The border colour is based on the type of
     * element (i.e. the format type). The border width like background color
     * is obtained from the element (format) attribute and is set to 0 if
     * not available or invalid. Note that a border width of 0 will result
     * is a border width of 1 in the display since a StyledGroup must have
     * a border - see StyledGroup for more details.
     */
    private void initializeBackgroundAndBorder() {
        // Get the border width value of the element. Not all elements have
        // a border width attribute. In such cases the border width is not
        // set.
        Attribute borderWidthAttr = element.
                getAttribute(FormatComposite.BORDER_WIDTH_ATTR_NAME);
        if (borderWidthAttr != null) {
            int borderWidth = 0;
            try {
                borderWidth = borderWidthAttr.getIntValue();
            } catch (DataConversionException e) {
                throw new UndeclaredThrowableException(e, e.getMessage());
            }
            setBorderLineWidth(borderWidth);
        }

        String bgColorValue = retrieveBackgroundColor();
        setBackgroundColor(bgColorValue);

        // There is some inconsistency in the handling of border colours
        // that originates from the pre-Eclipse GUI. Only segment and
        // segment grids have border colour attributes. All other border
        // colours for formats are derived from property resources based
        // on the type of the format. Therefore, if there is a border
        // colour attribute value on the element then it is used. If this
        // value is not a valid colour then the border colour will default
        // to the default border colour resource property. Othewise the
        // border color will be obtained as a resource property based on
        // the format type.
        String borderColorValue = element.getAttributeValue(
                FormatComposite.BORDER_COLOUR_ATTR_NAME);

        if (borderColorValue == null) {
            // The format does not have a border color attribute so use
            // the resources.borderColor = LayoutMessages.getString(
            borderColorValue = LayoutMessages.getString(RESOURCE_PREFIX +
                    formatType.getElementName() +
                    ".borderColor");
        }

        setBorderColor(borderColorValue);
    }

    /**
     * Retrieve the background color String for this FormatComposite. This
     * color is obtained from the element assocatiated with this
     * FormatComposite. If the background color attribute does not exist then
     * this method will climb the hierarchy of ancestor FormatComposites
     * looking for a background color. If it finds one then this color is
     * returned.
     * @return the String representing the background color of this
     * FormatComposite or null if no background color is applicable.
     */
    private String retrieveBackgroundColor() {
        // Get the background colour for the element. Not all elements have
        // this attribute, so the value defaults to the default background
        // colour property where the attribute is missing or has an invalid
        // value.
        String bgColorValue = element.
                getAttributeValue(FormatComposite.BACKGROUND_COLOUR_ATTR_NAME);

        if (bgColorValue == null) {
            // Check to see if the parent is a FormatComposite and if so and
            // if it has a background color attribute then use that.
            if (getParent() instanceof FormatComposite) {
                bgColorValue = ((FormatComposite)getParent()).
                        retrieveBackgroundColor();
            }
        }

        return bgColorValue;
    }


    /**
     * Set the background colour of this FormatComposite to the named colour.
     * If the named colour is not the name of a NamedColor or an rgb value
     * starting with a '#' (e.g. #ffaa98) then the default background colour
     * will be used.
     *
     * @param colorName the name of the required background colour. Can be
     * null and if it is the default background colour will be used.
     */
    void setBackgroundColor(String colorName) {
        if (colorName == null) {
            colorName = DEFAULT_BG_COLOR;
        }
        final ColorRegistry colorRegistry = getColorRegistry();
        Color bgColor = null;
        synchronized (colorRegistry) {
            bgColor = colorRegistry.get(colorName);
            if (bgColor == null) {
                RGB backgroundRGB = obtainRGBFromString(colorName);
                if (backgroundRGB == null) {
                    bgColor = colorRegistry.get(DEFAULT_BG_COLOR);
                    if (bgColor == null) {
                        backgroundRGB = obtainRGBFromString(DEFAULT_BG_COLOR);
                        if (backgroundRGB == null) {
                            throw new IllegalStateException(
                                    "Could not obtain an"
                                            + " RGB value for default background color: \""
                                            + DEFAULT_BG_COLOR + "\"");
                        }
                        colorRegistry.put(colorName, backgroundRGB);
                        bgColor = colorRegistry.get(colorName);
                    }
                } else {
                    colorRegistry.put(colorName, backgroundRGB);
                    bgColor = colorRegistry.get(colorName);
                }
            }
        }

        // Also ensure that the background colour of child format composites
        // are set to this color if they do not have a color of their own.
        Control children [] = getChildren();
        for (int i = 0; i < children.length; i++) {
            FormatComposite child = (FormatComposite) children[i];
            String bgColorValue = child.getElement().
                    getAttributeValue(FormatComposite.BACKGROUND_COLOUR_ATTR_NAME);
            if (bgColorValue == null) {
                child.setBackgroundColor(colorName);
            }
        }

        setBackground(bgColor);
    }

    /**
     * Set the border colour of this FormatComposite to the named colour.
     * If the named colour is not the name of a NamedColor or an rgb value
     * starting with a '#' (e.g. #ffaa98) then the default border colour
     * will be used.
     * @param colorName the name of the required background colour. Can be
     * null and if it is the default border colour will be used.
     */
    void setBorderColor(String colorName) {
        if (colorName == null) {
            colorName = DEFAULT_BORDER_COLOR;
        }
        final ColorRegistry colorRegistry = EclipseCommonPlugin.getColorRegistry();
        Color borderColor = null;
        synchronized (colorRegistry) {
            borderColor = colorRegistry.get(colorName);
            if (borderColor == null) {
                RGB borderRGB = obtainRGBFromString(colorName);
                if (borderRGB == null) {
                    borderColor = colorRegistry.get(DEFAULT_BORDER_COLOR);
                    if (borderColor == null) {
                        borderRGB = obtainRGBFromString(DEFAULT_BORDER_COLOR);
                        if (borderRGB == null) {
                            throw new IllegalStateException(
                                    "Could not obtain an"
                                            + " RGB value for default border color: \""
                                            + DEFAULT_BORDER_COLOR + "\"");
                        }
                        colorRegistry.put(colorName, borderRGB);
                        borderColor = colorRegistry.get(colorName);
                    }
                } else {
                    colorRegistry.put(colorName, borderRGB);
                    borderColor = colorRegistry.get(colorName);
                }
            }
        }
        setBorderColor(borderColor);
    }

    /**
     * Get a ColorRegistry for this FormatComposite. This method is provided
     * so that FormatComposite can be tested independently of Eclipse.
     *
     * This method should provide a shared ColorRegistry.
     *
     * @return a ColorRegistry for use by this FormatComposite.
     */
    protected ColorRegistry getColorRegistry() {
        return EclipseCommonPlugin.getColorRegistry();
    }

    /**
     * Attempt to obtain an RGB from a String. The String should be the name
     * of a NamedColor or a hex rgb value starting with a '#'.
     * @param s the String
     * @return the RGB value or null if s was not the name of a NamedColor or
     * a hex rbg (e.g '#ffee22').
     */
    private RGB obtainRGBFromString(String s) {
        RGB rgbValue = null;
        if (s != null) {
            s = s.trim();

            if (s.charAt(0) != '#') {
                s = NamedColor.getHex(s);
            }
            rgbValue = Convertors.hexToRGB(s);
        }

        return rgbValue;
    }

    /**
     * Get the child FormatComposite at the specified row/column location.
     * @param row the row number (0 to no. of rows -1)
     * @param column the column numnber (0 to no. of columns -1)
     * @return the FormatComposite at the given row/column location.
     * @throws IllegalArgumentException if either row or column are out of
     * range
     */
    FormatComposite getChildFC(int row, int column) {

        int index = row * getColumnCount() + column;

        return getChildFC(index);

    }

    /**
     * Get the child FormatComposite at the specified index.
     * @param index the index (0 to no. of children).
     * @return the child FormatComposite at index
     */
    private FormatComposite getChildFC(int index) {
        Control children [] = getChildren();

        if (children.length == 0) {
            throw new IllegalStateException(this + " has no children.");
        }

        return (FormatComposite) children[index];
    }

    /**
     * Given an ODOMElement find the child FormatComposite of this
     * FormatComposite that is associated with that ODOMElement.
     * @param element the ODOMElement
     * @return the child FormatComposite associated with element or null
     * if no such child was found.
     */
    FormatComposite getChildFC(ODOMElement element) {
        FormatComposite childFC = null;
        Control children [] = getChildren();
        for (int i = 0; i < children.length && childFC == null; i++) {
            FormatComposite child = (FormatComposite) children[i];
            if (child.getElement() == element) {
                childFC = child;
            }
        }

        return childFC;
    }

    /**
     * Get the localized text for this FormatComposite. The text comprizes
     * of the localized format type information along with the format name
     * if there is one.
     * @return the text for this FormatComposite - localized.
     */
    protected String getLocalizedText() {
        String text;

        if (FormatType.Structure.GRID == getFormatType().getStructure()) {
            text = getRowColText();
        } else {
            text = EclipseCommonMessages.
                    getLocalizedPolicyName(element.getName());
        }

        String formatName = element.getAttributeValue(NAME_ATTR_NAME);

        // If the format has a name and is not a leaf format (since leaf
        // format names go inside the format) then add the format name to
        // the text.
        if (formatName != null &&
                formatType.getStructure() != FormatType.Structure.LEAF) {
            MessageFormat messageFormat = new MessageFormat(
                    LayoutMessages.getString(RESOURCE_PREFIX +
                    "text"));
            Object args [] = {text, formatName};
            text = messageFormat.format(args);
        }

        return text;
    }

    /**
     * Gets the row and column title for the grid.
     * @return the title of the grid
     */
    private String getRowColText() {
        Object[] messageArguments = {new Integer(getRowCount()),
                                     new Integer(getColumnCount())
        };
        return GRID_TEXT_FORMAT.format(messageArguments);
    }

    /**
     * Get the number of rows in this FormatComposite.
     * @return the number of rows
     */
    int getRowCount() {
        List rowElements = getRowElements();
        int rows = rowElements.size() == 0 ? 1 : rowElements.size();
        return rows;
    }

    /**
     * Get the number of columns in this FormatComposite.
     * @return the number of columns.
     */
    int getColumnCount() {
        // FormatComposites must have a GridLayout.
        return ((GridLayout) getLayout()).numColumns;
    }

    /**
     * Determine if this FormatComposite is currently selected.
     *
     * @return true if this StyledGroup is currently selected; false
     * otherwise.
     */
    private boolean isSelected() {
        return isSelected;
    }

    /**
     * Gets the text for an empty format which is a child FormatComposite of
     * this grid. The text is the row and column number.
     * @param rowPos the row position
     * @param colPos the column position
     * @return the text for the empty format
     */
    public String getRowColTextForEmptyFormat(int rowPos, int colPos) {
        MessageFormat messageFormat = new MessageFormat(
                getRowColumnResourceString("emptyFormat"));

        Integer args [] = {new Integer(rowPos + 1), new Integer(colPos + 1)};
        return messageFormat.format(args);
    }

    /**
     * Set the focus (seeing as this is not handled automatically for
     * any format composites except for leaf nodes by focus events). Setting
     * the focus on one FormatComposite sets/unsets the focus on all of the
     * FormatComposites that are children of this FormatComposite. This
     * allows handling of the focus event problem and provides tree like
     * behaviour i.e. when the tree is unfocused all the selected nodes become
     * inactive. When the tree is focussed all the selected nodes (not just
     * the individually selected node) become active.
     * @param hasFocus true if this FormatComposite and descendents should
     * have focus; false otherwise.
     */
    private void setFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;

        // It is necessary to update the selection borders on all selected
        // FormatComposites that are children of this FormatComposite - along
        // with the border of this FormatComposite if it is selected.
        if (isSelected) {
            GC gc = new GC(this);
            repaintSelectionBorder(gc);
            gc.dispose();
        }

        Control children [] = getChildren();
        for (int i = 0; i < children.length; i++) {
            ((FormatComposite) children[i]).setFocus(hasFocus);
        }
    }

    /**
     * Colours derived from the system e.g. selection colours may change
     * during the lifetime of the application. This method will keep such
     * colour up-to-date.
     */
    private void updateSystemColors() {
        final ColorRegistry colorRegistry = getColorRegistry();
        
        synchronized (colorRegistry) {            
            updateSystemColor(SystemColor.activeCaption,
                    ACTIVE_SELECTION_COLOR, colorRegistry);
            
            updateSystemColor(SystemColor.inactiveCaption,
                    INACTIVE_SELECTION_COLOR, colorRegistry);
        }
    }
    
    private void updateSystemColor(final SystemColor systemColor,
            String systemColorName, ColorRegistry colorRegistry) {

        final int red = systemColor.getRed();
        final int green = systemColor.getGreen();
        final int blue = systemColor.getBlue();

        Color color = colorRegistry.get(systemColorName);
        if (color == null) {
            colorRegistry.put(systemColorName, new RGB(red, green, blue));

        } else if (color.getRed() != red || color.getGreen() != green
                || color.getBlue() != blue) {
            color.dispose();
            color = null;
            colorRegistry.put(systemColorName, new RGB(red, green, blue));
        }
    }

    /**
     * Override computeSize to consider the size of the name of this
     * FormatComposite that is displayed within the client area of the parent
     * StyledGroupd.
     * 
     * @param wHint
     *            see {@link org.eclipse.swt.widgets.Composite}
     * @param hHint
     *            see {@link org.eclipse.swt.widgets.Composite}
     * @param changed
     *            see {@link org.eclipse.swt.widgets.Composite}
     * @return the size of this FormatComposite
     */
    public Point computeSize(int wHint, int hHint, boolean changed) {
        Point size = super.computeSize(wHint, hHint, changed);
        if (name != null && name.length() > 0) {
            // Need to consider the name of the FormatComposite.
            GC gc = new GC(this);
            Rectangle clientArea = getClientArea();
            Point nameExtent = gc.textExtent(name);
            if (clientArea.width < nameExtent.x + INNER_MARGIN * 2) {
                size.x += INNER_MARGIN * 2 + nameExtent.x - clientArea.width;
            }
            if (clientArea.height < nameExtent.y + INNER_MARGIN * 2) {
                size.y += INNER_MARGIN * 2 + nameExtent.y - clientArea.height;
            }
            gc.dispose();
        }

        return size;
    }

    /**
     * Delegate to the super class to draw the StyledGroup then draw the
     * appropriate selection border around the outside of the StyledGroup.
     *
     * @param gc the GC in which to do the repaint
     */
    protected void repaint(GC gc) {
        super.repaint(gc);

        repaintSelectionBorder(gc);

        repaintName(gc);
    }

    /**
     * Clear the client area and repaint the name.
     * @param gc the GC into which to paint the name.
     */
    void clearAndRepaintName(GC gc) {
        Rectangle clientArea = getClientArea();
        // Draw a filled rectangle to clear the background for the text.
        gc.fillRectangle(clientArea.x, clientArea.y,
                clientArea.width, clientArea.height);
        repaintName(gc);
    }

    /**
     * Repaint the name part of this FormatComposite.
     * @param gc the GC into which to paint the name.
     */
    private void repaintName(GC gc) {
        if (name != null) {
            Point nameExtent = gc.textExtent(name);
            int nameX = getSize().x / 2 - nameExtent.x / 2;
            int nameY = getSize().y / 2 - nameExtent.y / 2;
            Color textColor = Convertors.getContrastingColor(getBackground());
            gc.setForeground(textColor);
            gc.drawText(name, nameX, nameY);
        }
    }

    /**
     * Repaint the selection part of the border of this FormatComposite - i.e.
     * selected & focused = active selection coloured border, just selected
     * = inactive selection coloured border, not selected = no selection
     * border.
     *
     * @param gc the GC into which to paint the border.
     */
    private void repaintSelectionBorder(GC gc) {
        // Ensure we have the latest active/inactive selection colors
        if (isSelected()) {
            updateSystemColors();
        }

        // Determine the color of the rectangle tha signifies selection +
        // focus, just selection or no selection.
        Color selectLineColor;

        if (isSelected() && hasFocus) {
            selectLineColor = getColorRegistry().get(ACTIVE_SELECTION_COLOR);
        } else if (isSelected()) {
            selectLineColor = getColorRegistry().get(INACTIVE_SELECTION_COLOR);
        } else {
            selectLineColor = getBackground();
        }

        int lineWidth = 2;
        gc.setForeground(selectLineColor);
        gc.setLineWidth(lineWidth);
        gc.drawRectangle(0 + lineWidth / 2, 0 + lineWidth / 2,
                getSize().x - lineWidth,
                getSize().y - lineWidth);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Feb-05	6749/1	allan	VBM:2005012102 Drag n Drop support

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Sep-04	5663/1	tom	VBM:2004081003 Replaced ColorRegistry with Eclipse V3.0.0 Version

 25-Aug-04	5266/5	philws	VBM:2004081007 Fix layout context menu handling

 18-Aug-04	5264/1	allan	VBM:2004081008 Use GC.textExtent() instead of GC.stringExtent()

 04-Aug-04	4902/14	allan	VBM:2004071504 Rework issues

 03-Aug-04	4902/12	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 07-Apr-04	3787/1	byron	VBM:2004020212 FormatComposites: Use resources for titles and row/column text

 07-Apr-04	3769/1	byron	VBM:2004020212 FormatComposites: Use resources for titles and row/column text

 22-Mar-04	3498/1	byron	VBM:2004022501 Layout: Segment colour cannot be changed

 04-Mar-04	3298/3	byron	VBM:2004022611 Defect :Error adding a pane in spatial iterator - grid - updated javadoc

 04-Mar-04	3298/1	byron	VBM:2004022611 Defect :Error adding a pane in spatial iterator - grid

 25-Feb-04	3213/3	pcameron	VBM:2004022414 A few tweaks to background colours

 25-Feb-04	3213/1	pcameron	VBM:2004022414 Background colours change to default if invalid or missing values

 24-Feb-04	3021/27	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 24-Feb-04	3021/24	pcameron	VBM:2004020211 Added StyledGroup and background colours

 20-Feb-04	3021/21	pcameron	VBM:2004020211 Undid addition of layoutFormat key to some resources

 19-Feb-04	3021/18	pcameron	VBM:2004020211 Committed for integration

 19-Feb-04	3021/15	pcameron	VBM:2004020211 Committed for integration

 13-Feb-04	2915/6	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 12-Feb-04	2915/2	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 09-Feb-04	2881/1	pcameron	VBM:2004020515 Added graphical row and column deletion

 05-Feb-04	2875/1	pcameron	VBM:2004020202 Added RowDeleteAction

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 30-Jan-04	2549/41	pcameron	VBM:2004011201 FormatComposite structures and graphical layout editor

 23-Jan-04	2726/6	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2726/4	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2720/4	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 22-Jan-04	2549/21	pcameron	VBM:2004011201 Committed for integration into layout editor

 ===========================================================================
*/
