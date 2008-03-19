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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Format.java,v 1.96 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history and
 *                              added visit method.
 * 09-Jul-01    Paul            VBM:2001062810 - Changed the visit and
 *                              visitChildren methods to return a boolean.
 * 23-Jul-01    Paul            VBM:2001070507 - Added support for destination
 *                              area.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 27-Jul-01    Paul            VBM:2001072603 - Cleaned up.
 * 03-Aug-01    Kula            VBM:2001080102 - Height property added to pane
 * 03-Aug-01    Allan           VBM:2001080102 - HEIGHT_UNIT_ATTRIBUTE fixed
 *                              to begin with an uppercase H.
 * 14-Sep-01    Allan           VBM:2001091103 - Added hasGridChildren
 *                              property.
 * 17-Sep-01    Allan           VBM:2001091103 - Added getColumns(). Removed
 *                              hasGridChildren property
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 01-Oct-01    Doug            VBM:2001092501 - Added method
 *                              getBestBackgroundImage to retrive the most
 *                              suitable background URL. Moved getters and
 *                              setters for BACKROUND_IMAGE from subclasses
 *                              to this class and renamed property to
 *                              BACKGROUND_COMPONENT. Added new property
 *                              BACKGROUND_COMPONENT_TYPE.
 * 17-Oct-01    Paul            VBM:2001101701 - Override toString, hashCode
 *                              and equals methods to implement the default
 *                              Object behaviour as SimpleAttributeContainer
 *                              changes their behaviour.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Nov-01    Paul            VBM:2001102403 - Moved the constants out into
 *                              the FormatConstants interface to make them
 *                              more accessible to other classes.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 22-Feb-02    Steve           VBM:2001101803 - Added
 *                              getEnclosingFormFragment() method to see if a
 *                              form fragment encloses a format.
 * 04-Mar-02    Allan           VBM:2002030102 - Add removeDefaultAttributes().
 * 14-Mar-02    Adrian          VBM:2002020101 - modified method visitChildren
 *                              to return false if field children is null.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 31-Oct-02    Allan           VBM:2002103107 - Added getName() and setName().
 *                              This makes the child implementations of these
 *                              methods obselete.
 * 01-Nov-02    Allan           VBM:2002101805 - Added getAttributeGroupings()
 *                              to support attribute groupings. Added
 *                              setAttribute() and setDeviceLayout(). The
 *                              the children that use namespaces need to stop
 *                              doing this before they can make use of these
 *                              two methods. Added getFormatIterator().
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 10-Dec-02    Allan           VBM:2002110102 - Modified setAttribute() and
 *                              setDeviceLayout() in accordance with this
 *                              change and the new FormatScope class.
 * 18-Feb-03    Allan           VBM:2003021803 - Implement equals() and 
 *                              hashcode(). Added getFormatKey() both 
 *                              static and non-static versions.
 * 11-Mar-03    Chris W         VBM:2003031106 - Modified getChildAt() to stop
 *                              ArrayIndexOutOfBoundsException when parameter
 *                              passed in equals number of child elements.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 24-Mar-03    Allan           VBM:2003021803 - Consider class in
 *                              equals() and hashCode().
 * 28-Mar-03    Allan           VBM:2003030603 - Implemented clone. Made
 *                              defaultProperties protected so they can be
 *                              accessed from a testcase.
 * 01-Apr-03    Chris W         VBM:2003031106 - getChildAt() modified to stop
 *                              ArrayIndexOutOfBoundsException when parameter
 *                              passed in is negative.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() and visitChildren()
 *                              methods.
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.jibx.JiBXSourceLocation;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.I18NMessage;
import com.volantis.mcs.model.validation.SourceLocation;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.integrity.DefinitionScope;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

import org.jibx.runtime.impl.ITrackSourceImpl;

/**
 * The <B>Format</B> class is the parent for all specific formats.
 * Layouts define how components will be arranged on the display of
 * output devices. Layouts consist of hierarchies of individual
 * formatting specifications, known collectively as formats. Each
 * format defines the formatting for a small number of child formats.
 * At the lowest level the formats are individual panes that usually
 * usually contain a single component. It is possible to have multiple
 * components in a single pane though it is not possible to specify any
 * additional formats woover and above that within the components
 * themselves. A typical use for this would be for a simple sequence of
 * paragraphs, for example.
 * @author Rhys Lewis
 *
 * @mock.generate
 */
public abstract class
    Format extends SimpleAttributeContainer
    implements StyleableFormat,
    WidthAttributes,
    HeightAttributes, Validatable, ITrackSourceImpl {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(Format.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(Format.class);

    /**
     * The location of this object within the source document.
     */
    protected final SourceLocation sourceLocation =
        new JiBXSourceLocation(this);

    private static FormatProperties defaultProperties
        = new FormatProperties();

    /**
     * The parent format of this one, or null if this is the root.
     */
    protected Format parent;

    /**
     * The array of formats that are children of this format
     */
    protected Format children[];

    /**
     * The Layout of which this format is a part
     * @todo later investigate removing this property when layouts are re-written
     * - a layout is a container of Formats the Formats inside should not
     * have to be aware of this.
     */
    protected Layout layout;

    /**
     * The unique identifier for this format within the Layout.
     */
    private int instance;

    /**
     * The number of dimensions that the Format referred to by this context has.
     * This is zero except when the format is contained within a format iterator.
     */
    private int dimensions = -1;

    /**
     * An array storing the maximum number of cells in each dimension of a
     * NDimensionalIndex.
     */
    private int[] maxCells;


    /**
     * Constructor for formats using default initialisation but where the
     * number of children is known in advance
     * @param numChildren The number of panes that the new format contains
     * @param layout The Layout of which this format is part.
     */
    public Format(int numChildren, Layout layout) {
        // Initialise the new format with default values for its attributes
        this(numChildren, layout, defaultProperties);
    }

    /**
     * Constructor for formats that specify initialisation parameters
     * @param numChildren The number of panes that the new format contains
     * @param properties Properties of the format.
     * @param layout The Layout of which this format is part.
     */
    public Format(int numChildren, Layout layout,
        FormatProperties properties) {

        setNumChildren(numChildren);
        setDeviceLayout(layout);

        // Populate the set of attributes associated with this format

        if (properties != null) {
            String [] attributes = getDefaultAttributes();
            if (attributes == null) {
                Iterator iterator = properties.attributeNames();
                while (iterator.hasNext()) {
                    String name = (String) iterator.next();
                    String value = (String) properties.getAttribute(name);
                    setAttribute(name, value);
                }
            } else {
                for (int a = 0; a < attributes.length; a += 1) {
                    String name = attributes[a];
                    String value = (String) properties.getAttribute(name);
                    setAttribute(name, value);
                }
            }
        }

        return;
    }

    /**
     * Sets the number of columns.  This method actually does nothing due to the
     * hard-coded column getter.  Subsclasses must override this to provide a
     * meaningful implementation.
     * @param columns the number of columns.
     */
    public void setColumns(int columns) {
        // do nothing
    }

    /**
     * Get the number of columns in this Layout.
     * @return 1 - the default. Subclasses should overide this method where
     * necessary.
     */
    public int getColumns() {
        return 1;
    }

    /**
     * Retrieve the format type
     * This method MUST be overridden in each subclass
     * @return The type of this format.
     */
    public abstract FormatType getFormatType();

    /**
     * Retrieve the set of attributes which can be changed by the user.
     * This method MUST be overridden in each subclass
     * @return An array of attributes names.
     */
    public abstract String [] getUserAttributes();

    /**
     * Retrieve the set of attributes which are persistent.
     * This method MUST be overridden in each subclass
     * @return An array of attributes names.
     */
    public abstract String [] getPersistentAttributes();

    /**
     * Retrieve the set of attributes which have sensible defaults.
     * This method MUST be overridden in each subclass
     * @return An array of attributes names.
     */
    public abstract String [] getDefaultAttributes();

    /**
     * Set the value of the instance property.
     * @param instance The new value of the instance property.
     */
    public void setInstance(int instance) {
        this.instance = instance;
    }

    /**
     * Get the value of the instance property.
     * @return The value of the instance property.
     */
    public int getInstance() {
        return instance;
    }

    /**
     * Set the number of children that are direct descendents of this format
     * @param numChildren The number of children that are direct
     * descendents of this format.
     */
    protected void setNumChildren(int numChildren) {

        if (numChildren > 0) {
            // Create the array to hold the children of this format
            children = new Format [numChildren];
        }
    }

    /**
     * Retrieve the number of children of this format
     * @return The number of children that are direct
     * descendents of this format.
     */
    public int getNumChildren() {
        return (children == null) ? 0 : children.length;
    }

    /**
     * Set the value of the layout property.
     * @param layout The new value of the layout property.
     */
    public void setDeviceLayout(Layout layout) {
        Layout originalLayout = getLayout();
        if (originalLayout == layout) {
            return;
        }

        String name = getName();

        if (originalLayout != null) {
            if (name != null) {
                originalLayout.removeFormat(this);
            }
        }

        this.layout = layout;

        if (layout != null) {
            if (name != null) {
                layout.addFormat(this);
            }
        }
    }

    /**
     * Override the SimpleAttributeContainer.setAttribute method to enable
     * special processing to be done. I.e. if the attribute is
     * Format.NAME_ATTRIBUTE and the value has changed then update the
     * FormatRegister.
     * @param name The name of the attribute.
     * @param value The value of the attribute.
     */
    public void setAttribute(String name, Object value) {
        String oldValue;
        if (name.equals(FormatConstants.NAME_ATTRIBUTE)) {
            // Do nothing if the value hasn't changed.
            oldValue = (String) getAttribute(name);
            if (oldValue == null ? value == null : oldValue.equals(value)) {
                return;
            }

            if (oldValue != null && layout != null) {
                layout.removeFormat(this);
            }

            super.setAttribute(name, value);

            if (value != null && layout != null) {
                layout.addFormat(this);
            }
        } else {
            super.setAttribute(name, value);
        }
    }

    /**
     * Get the value of the layout property.
     * @return The value of the layout property.
     */
    public Layout getLayout() {
        return layout;
    }

    /**
     * Set the parent format of this object.
     */
    public void setParent(Format parent) {
        this.parent = parent;

        if (parent != null) {
            // Inherit the layout from the parent if necessary.
            if (getLayout() == null) {
                setDeviceLayout(parent.getLayout());
            }
        }
    }

    /**
     * Get the parent format of this object.
     */
    public Format getParent() {
        return parent;
    }

    /**
     * Get the fragment format which encloses this format, this format is
     * ignored.
     * @return The enclosing fragment.
     */
    public Fragment getEnclosingFragment() {
        Format format = getParent();
        if (format != null) {
            format = format.getFragment();
        }

        return (Fragment) format;
    }

    /**
     * Get the closest fragment format to this format. If this format is a
     * fragment then return it, otherwise return the enclosing fragment.
     * @return The closest fragment.
     */
    public Fragment getFragment() {
        Format format = this;
        while (format != null && !(format instanceof Fragment)) {
            format = format.getParent();
        }

        return (Fragment) format;
    }

    /**
     * Get the form format which encloses this format, this format is
     * ignored.
     * @return The enclosing form.
     */
    public Form getEnclosingForm() {
        Format format = getParent();
        while (format != null && !(format instanceof Form)) {
            format = format.getParent();
        }

        return (Form) format;
    }

    /**
     * Get the form fragment format which encloses this format, this format is
     * ignored.
     * @return The enclosing form fragment.
     */
    public FormFragment getEnclosingFormFragment() {
        Format format = getParent();
        while (format != null && !(format instanceof FormFragment)) {
            format = format.getParent();
        }

        return (FormFragment) format;
    }

    /**
     * Set the name of the Form as an attribute
     * @param name The name of the Form
     */
    public void setName(String name) {
        setAttribute(FormatConstants.NAME_ATTRIBUTE, name);
    }

    /**
     * Retrieve the name of the Form
     * @return The name of the Form
     */
    public String getName() {
        return (String) getAttribute(FormatConstants.NAME_ATTRIBUTE);
    }

    /**
     * Set the border width attribute for this format
     * @param borderWidth The border width attribute for this format
     */
    public void setBorderWidth(String borderWidth) {
        setAttribute(FormatConstants.BORDER_WIDTH_ATTRIBUTE,
            borderWidth);
    }

    /**
     * Retrieve the border width attribute for this format
     * @return The border width attribute for this format
     */
    public String getBorderWidth() {
        return (String) getAttribute(FormatConstants.BORDER_WIDTH_ATTRIBUTE);
    }

    /**
     * Set the cell padding attribute for this format
     * @param cellPadding The cell padding attribute for this format
     */
    public void setCellPadding(String cellPadding) {
        setAttribute(FormatConstants.CELL_PADDING_ATTRIBUTE, cellPadding);
    }

    /**
     * Retrieve the cell padding attribute for this format
     * @return The cell padding attribute for this format
     */
    public String getCellPadding() {
        return (String) getAttribute(FormatConstants.CELL_PADDING_ATTRIBUTE);
    }

    /**
     * Set the cell spacing attribute for this format
     * @param cellSpacing The cell spacing attribute for this format
     */
    public void setCellSpacing(String cellSpacing) {
        setAttribute(FormatConstants.CELL_SPACING_ATTRIBUTE, cellSpacing);
    }

    /**
     * Retrieve the cell spacing attribute for this format
     * @return The cell spacing attribute for this format
     */
    public String getCellSpacing() {
        return (String) getAttribute(FormatConstants.CELL_SPACING_ATTRIBUTE);
    }

    /**
     * Set the horizontal alignment attribute for this format. This
     * attribute determines how this format will be horizontally aligned
     * within its parent format.
     * @param horizontalAlignment The horizontal alignment attribute for this
     * format
     */
    public void setHorizontalAlignment(String horizontalAlignment) {
        setAttribute(FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
            horizontalAlignment);
    }

    /**
     * Retrieve the horizontal alignment attribute for this format. This
     * attribute determines how this format will be horizontally aligned
     * within its parent format.
     * @return the horizontal alignment attribute for this format
     */
    public String getHorizontalAlignment() {
        return (String) getAttribute(
            FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE);
    }

    /**
     * Set the vertical alignment attribute for this format. This
     * attribute determines how this format will be vertically aligned
     * within its parent format.
     * @param verticalAlignment The vertical alignment attribute for this
     * format
     */
    public void setVerticalAlignment(String verticalAlignment) {
        setAttribute(FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
            verticalAlignment);
    }

    /**
     * Retrieve the vertical alignment attribute for this format. This
     * attribute determines how this format will be vertically aligned
     * within its parent format.
     * @return The vertical alignment attribute for this format
     */
    public String getVerticalAlignment() {
        return (String) getAttribute(
            FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE);
    }

    /**
     * Set the height attribute for this format
     * @param height The width attribute for this format
     */
    public void setHeight(String height) {
        setAttribute(FormatConstants.HEIGHT_ATTRIBUTE,
            height);
    }

    /**
     * Retrieve the height attribute for this format
     * @return The height attribute for this format
     */
    public String getHeight() {
        return (String) getAttribute(FormatConstants.HEIGHT_ATTRIBUTE);
    }

    /**
     * Set the heightUnits attribute for this format
     *
     * @param heightUnits The heightUnits attribute for this format
     */
    public void setHeightUnits(String heightUnits) {
        setAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE,
            heightUnits);
    }

    /**
     * Retrieve the heightUnits attribute for this format.
     *
     * @return The heightUnits attribute for this format, or percent if the
     *         attribute does not exist
     */
    public String getHeightUnits() {
        String units =
            (String) getAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE);
        return (units == null ?
            FormatConstants.HEIGHT_UNITS_VALUE_PERCENT : units);
    }

    /**
     * Set the width attribute for this format
     * @param width The width attribute for this format
     */
    public void setWidth(String width) {
        setAttribute(FormatConstants.WIDTH_ATTRIBUTE,
            width);
    }

    /**
     * Retrieve the width attribute for this format
     * @return The width attribute for this format
     */
    public String getWidth() {
        return (String) getAttribute(FormatConstants.WIDTH_ATTRIBUTE);
    }

    /**
     * Set the widthUnits attribute for this format
     * @param widthUnits The widthUnits attribute for this format
     */
    public void setWidthUnits(String widthUnits) {
        setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE,
            widthUnits);
    }

    /**
     * Retrieve the widthUnits attribute for this format.
     * @return The widthUnits attribute for this format, or percent if the
     * attribute does not exist
     */
    public String getWidthUnits() {
        String units =
            (String) getAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE);
        return (units == null ?
            FormatConstants.WIDTH_UNITS_VALUE_PERCENT : units);
    }

    /**
     * get the value of the background component attribute
     *
     * @return The value of the background component attribute
     */
    public String getBackgroundComponent() {
        return (String) getAttribute(
            FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE);
    }

    /**
     * set the value of the background component attribute
     *
     * @param backgroundComponent The new value of the background component
     * attribute
     */
    public void setBackgroundComponent(String backgroundComponent) {
        setAttribute(FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE,
            backgroundComponent);
    }

    /**
     * get the value of the background component type attribute
     *
     * @return The value of the background component type attribute
     */
    public String getBackgroundComponentType() {
        return (String)
            getAttribute(FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE);
    }

    /**
     * set the value of the background component type attribute
     *
     * @param backgroundComponentType The new value of the background component
     * type attribute
     */
    public void setBackgroundComponentType(String backgroundComponentType) {
        setAttribute(FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
            backgroundComponentType);
    }

    /**
     * Returns true if the attribute of this has a specified value
     * @param attribute The attribute to retrieve
     * @param value     The value the attribute is compared with
     * @return boolean
     */
    public boolean attributeHasValue(String attribute, String value) {
        String attributeValue = (String) getAttribute(attribute);
        if (value.equals(attributeValue)) {
            return true;
        } else {
            return false;
        }
    }

    public void attributesHaveBeenSet()
        throws LayoutException {
    }

    public void childrenHaveBeenCreated()
        throws LayoutException {
    }

    public SimpleAttributeContainer createSubComponent(String type,
        int childIndex)
        throws LayoutException {
        return null;
    }

    public void subComponentInitialised(SimpleAttributeContainer container)
        throws LayoutException {
    }

    /**
     * @todo later investigate the necessity for SubComponentInfo if/when
     * layouts are re-written. Note: removal of this will impact equals() and
     * hashCode() on those components that do have SubComponentInfo like Grid.
     * @return an Enumeration to the sub components
     */
    public Enumeration getSubComponentInfo() {
        return null;
    }

    /**
     * Convenience method to set the background colour attribute for this
     * Format.
     * @param backgroundColour The String representation of the background
     * colour.
     */
    public void setBackgroundColour(String backgroundColour) {
        setAttribute(FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE,
            backgroundColour);
    }

    /**
     * Convenience method to get the background colour attribute for this
     * Format.
     * @return The String represetation of the background colour.
     */
    public String getBackgroundColour() {
        return (String)
            getAttribute(FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE);
    }

    // Javadoc inherited.
    public String getTypeName() {
        return getFormatType().getTypeName();
    }

    /**
     * @todo later investigate the necessity for SubComponentInfo if/when
     * layouts are re-written. Note: removal of this will impact equals() and
     * hashCode() on those components that do have SubComponentInfo like Grid.
     */
    public class SubComponentInfo {

        private SimpleAttributeContainer container;
        private String type;
        private int childIndex;

        public SubComponentInfo(SimpleAttributeContainer container,
            String type, int childIndex) {
            this.container = container;
            this.type = type;
            this.childIndex = childIndex;
        }

        public SimpleAttributeContainer getContainer() {
            return container;
        }

        public String getType() {
            return type;
        }

        public int getChildIndex() {
            return childIndex;
        }

        // javadoc inherited
        public boolean equals(Object o) {
            boolean equals = o instanceof SubComponentInfo;

            if (equals) {
                SubComponentInfo info = (SubComponentInfo) o;
                equals = getChildIndex() == info.getChildIndex() &&
                    ObjectHelper.equals(getType(), info.getType()) &&
                    ObjectHelper.equals(getContainer(), getContainer());
            }

            return equals;
        }

        // javadoc inherited
        public int hashCode() {

            return ObjectHelper.hashCode(getContainer()) +
                ObjectHelper.hashCode(getType()) +
                getChildIndex();
        }
    }

    /**
     * Add a child format to this format
     * @param child The child format to be added
     * @param index The index at which the child is to be added
     * to the parent
     */
    public void setChildAt(Format child, int index)
        throws LayoutException {
        if (children == null) {
            throw new LayoutException(EXCEPTION_LOCALIZER.format(
                "format-child-not-allowed", getFormatType()));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Format.setChildAt invoked with an index of "
                + index);
        }
        children[index] = child;
        return;
    }

    /**
     * Get the child format at the specified index.
     * @param index Index into the children array
     * @return Child format, or null if index is out of bounds
     */
    public Format getChildAt(int index) {
        if (index < 0 || children.length <= index) {
            return (null);
        } else {
            return (children[index]);
        }
    }

    public void insertChildAt(Format child, int index) {
        int length = getNumChildren();
        Format [] newChildren = new Format [length + 1];

        for (int i = 0; i < index; i += 1) {
            newChildren[i] = children[i];
        }
        newChildren[index] = child;
        for (int i = index; i < length; i += 1) {
            newChildren[i + 1] = children[i];
        }
        children = newChildren;
    }

    /**
     * Call the relevant method in the visitor for this class of object.
     * @param visitor The object which is visiting this format.
     * @param object Some extra information which the visitor needs.
     * @return True to stop visiting and false to continue.
     */
    public abstract boolean visit(FormatVisitor visitor, Object object)
        throws FormatVisitorException;

    /**
     * Visit all the children of this format in order.
     * @param visitor The object which is visiting this format.
     * @param object Some extra information which the visitor needs.
     * @return True if the result of visting any child is true and false
     * otherwise.
     */
    public boolean visitChildren(FormatVisitor visitor, Object object)
        throws FormatVisitorException {
        if (children == null) {
            return false;
        }

        for (int i = 0; i < children.length; i += 1) {
            Format child = children[i];
            if (child != null) {
                boolean done = child.visit(visitor, object);
                if (done) {
                    return done;
                }
            }
        }

        return false;
    }

    /**
     * Generate a String representation of the format.
     *
     * @return The generated string representation.
     */
    public String toString() {
        StringBuffer details = new StringBuffer();
        details.append(getClass().getName()).append(": ").
            append(getName()).append(", ").append(getFormatType()).
            append(", ").append(getFormatType());

        return details.toString();
    }

    // =====================================================================
    //
    // NOTE: the dimension and skippable calculation methods below were moved 
    // from FormatInstance to here when we made format contexts per instance,
    // as this was the simplest and most obvious place to put it.
    // 
    // These methods should probably not have been put into FormatInstance in
    // the first place as they used only Format state. They should also have 
    // been implemented in a way which is cleaner and more performant. However,
    // fixing these methods is out of scope for the current VBM and as such 
    // they are left for the fixing in the future.
    // 
    // TODO: decide on the real home for dimension and skippable calculations.
    // This code may need to be moved to a more appropriate place when it 
    // is more clear what that place should be. It maybe a runtime version of
    // a Format, or maybe left here if that is appropriate.
    //
    // TODO: replace syncronized for (ultimate) performance?
    // The current implementation requires syncronised on these methods. 
    // The performance of getDimensions is not a huge issue because it is only 
    // called when resolving pane instance expressions.
    // The performance of isSkippable is not an issue because it is currently 
    // always cached.
    // When fixing this code it may be good to replace this with a 
    // RepositoryObjectActivator which initialised these values at load time,
    // thus avoiding the need for syncronisation.
    // We could also use the lazy-initialisation-on-demand idiom which would 
    // be quite a lot simpler ... see "Effective Java" Item 48. 
    // 
    // TODO: reimplement dimension and skippable calculations properly
    // The current implementations could be improved to be much faster and 
    // cleaner, if they are to be within the Format heirarchy/tree.
    // 
    // =====================================================================

    /**
     * Returns the number of dimensions for the Format that this FormatInstance
     * refers to. This is zero unless the format is contained within a format
     * iterator.
     * @return int The number of dimensions.
     * @todo later requires quite a lot of work. See above.
     */
    public synchronized int getDimensions() {

        if (dimensions == -1) {
            int dimensions = 0;
            Format format = getParent();
            while (format != null) {
                if (format instanceof FormatIterator) {
                    dimensions++;
                }
                format = format.getParent();
            }
            this.dimensions = dimensions;
        }
        return dimensions;
    }

    /**
     * This method returns true if this format does not need to be
     * processed. For example, if the maximum number of cells is set to 5 in a
     * temporal format iterator and the jsp refers to pane.7 then this pane
     * does not need to be processed.
     * @param index The NDimensionalIndex of the format.
     * @return boolean
     * @todo later requires quite a lot of work. See above.
     */
    public synchronized boolean isSkippable(NDimensionalIndex index) {

        if (maxCells == null) {
            setMaxCells();
        }

        // Get the array of indices associated with this FormatReference        
        int[] indicies = index.getIndicies();
        int pos;
        boolean skippable = false;

        for (int i = 0; i < maxCells.length; i++) {
            // If there are insufficient indices we assume those present apply
            // to the outermost format iterators. We assume a value of zero
            // for all missing indices in order to select the target pane.          
            if (indicies.length > i) {
                pos = indicies[i];
            } else {
                pos = 0;
            }

            // If the max no. of cells = 0 then no. cells is unlimited.
            // Valid indices for the index are 0...maxCells[i]-1            
            if (maxCells[i] != 0 && pos >= maxCells[i]) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                        "Skipping instance: " + getName() + " " + index);
                }
                skippable = true;
                break;
            }
        }
        return skippable;
    }

    /**
     * Populates the array storing the maximum number of cells in each
     * dimension of a NDimensionalIndex.
     */
    private void setMaxCells() {

        final int[] maxCells;
        // Loop up this format's parents looking for spatial or temporal format
        // iterators. We can't use a visitor that implements FormatVisitor as
        // it is designed to look down the hierarchy of formats.
        List formats = new ArrayList();
        Format myFormat = this;
        if (myFormat != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Calculating max. cells for format: " +
                    getName());
            }

            while ((myFormat = myFormat.getParent()) != null) {
                if (FormatType.SPATIAL_FORMAT_ITERATOR.equals(
                    myFormat.getFormatType())
                    || FormatType.TEMPORAL_FORMAT_ITERATOR.equals(
                    myFormat.getFormatType())) {
                    // Add the format iterator to the beginning of the list.
                    // i.e. in reverse order - outermost first. This is the
                    // same order as the dimensions in the
                    // NDimensionalIndex. 
                    formats.add(0, myFormat);
                }
            }
        }

        int dimensions = formats.size();
        maxCells = new int[dimensions];
        for (int i = 0; i < dimensions; i++) {
            // maxCells is the maximum number of cells in this dimension
            // or 0 if the number of cells is unlimited.
            maxCells[i] = getMaxCellsOfFormat((Format) formats.get(i));
        }
        this.maxCells = maxCells;
    }

    /**
     * Returns the maximum number of cells that should be rendered. This is
     * specified by the user, in the gui, in the properties of the format
     * iterator.
     * @param format
     * @return int
     */
    private int getMaxCellsOfFormat(Format format) {

        if (FormatType.SPATIAL_FORMAT_ITERATOR.equals(format.getFormatType())) {
            // Calculate max cells for a spatial format iterator
            SpatialFormatIterator spatial = (SpatialFormatIterator) format;
            int rows = spatial.getAttributeAsInt(
                SpatialFormatIterator.SPATIAL_ITERATOR_ROW_COUNT,
                "Spatial has no rows set");
            int cols = spatial.getAttributeAsInt(
                SpatialFormatIterator.SPATIAL_ITERATOR_COLUMN_COUNT,
                "Spatial has no cols set");
            boolean fixedRows = spatial.attributeHasValue(
                SpatialFormatIterator.SPATIAL_ITERATOR_ROWS,
                "fixed");
            boolean fixedCols = spatial.attributeHasValue(
                SpatialFormatIterator.SPATIAL_ITERATOR_COLUMNS,
                "fixed");

            // If either the rows are fixed and equal zero 
            // or the columns are fixed and equal zero then we don't render any panes. 
            if ((fixedRows && rows == 0) || (fixedCols && cols == 0)) {
                return -1;
            } else {
                // If these are non-zero, then irrespective of
                // whether we have a fixed or variable number of rows and columns,
                // the max. value = rows * cols;
                return rows * cols;
            }
        } else {
            // Calculate max cells for a temporal format iterator
            TemporalFormatIterator temporal = (TemporalFormatIterator) format;

            int elements = temporal.getAttributeAsInt(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CELL_COUNT,
                "Temporal has no cell count");

            boolean fixed = temporal.attributeHasValue(
                TemporalFormatIterator.TEMPORAL_ITERATOR_CELLS,
                "fixed");

            // If the number of elements is fixed and equal to 0 then we don't
            // render any panes.
            if (fixed && elements == 0) {
                return -1;
            } else {
                // If these are non-zero, then irrespective of
                // whether we have a fixed or variable number of elements,
                // the max. value is the number of elements
                return elements;
            }
        }
    }

    /**
     * Retrieves an attribute of a format iterator
     * @param attribute      The attribute to retrieve
     * @param message        The message in the PAPIException if the attribute
     *                       cannot be found
     * @return int           The value of the attribute
     * @throws IllegalStateException   thrown if attribute cannot be found.
     */
    public int getAttributeAsInt(String attribute,
        String message) throws IllegalStateException {

        int value;
        final String attributeValue = (String) getAttribute(attribute);
        if (attributeValue != null) {
            value = Integer.parseInt(attributeValue);
        } else {
            throw new IllegalStateException(
                EXCEPTION_LOCALIZER.format(message));
        }
        return value;
    }

    /**
     * Returns true if this format is contained by a format of the class
     * specified.
     *
     * @param clazz the class to search for.
     * @return true if the format is contained by the format specified, or
     *      false otherwise.
     */
    boolean isContainedBy(Class clazz) {
        return getContainer(clazz) != null;
    }

    /**
     * Return the first containing Format of the class provided, or null if
     * there is none.
     *
     * @param clazz the class of Format to match on.
     * @return the matching Format, or null.
     */
    Format getContainer(Class clazz) {

        Format format = this;
        while ((format = format.getParent()) != null) {
            if (clazz == format.getClass()) {
                return format;
            }
        }
        return null;
    }

    public static final PropertyIdentifier FORMATS =
        new PropertyIdentifier(Format.class, "formats");


    protected void validateRequiredName(ValidationContext context) {
        validateName(context, true);
    }

    protected void validateOptionalName(ValidationContext context) {
        validateName(context, false);
    }


    /**
     * Validate the name of this format.
     *
     * @param context the validation context.
     */
    protected void validateName(ValidationContext context, boolean required) {

        // Hack: if the layout is "anonymous", do no name checking.
        // This is used for the default XDIME2 layout.
        if (layout.isAnonymous()) {
            return;
        }

        Step step = context.pushPropertyStep("name");
        final String name = getName();
        // If there was a name specified
        if (name != null) {
            // Then ensure that is is valid.
            if (!LayoutTypeValidator.isFormatNameType(name)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.FORMAT_NAME_ILLEGAL, name));
            }

            // And then ensure it is unique with it's scope.
            final DefinitionType type = FormatNamespaceDefinitionTypes.getType(
                getFormatType().getNamespace());
            DefinitionScope scope = context.getDefinitionScope(type);
            if (scope != null) {
                scope.define(context, sourceLocation, name);
            }
        } else {
            // Else, it was not specified.
            // if required, report a validation error.
            if (required) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.FORMAT_NAME_UNSPECIFIED));
            }
        }
        context.popStep(step);
    }

    void validateAllPaneAndGridAndIteratorAttributes(
        final ValidationContext context, String element) {

        validateBackgroundColorAttribute(context);
        validateBackgroundComponentAttributes(context, element);
        validateBorderWidthAttribute(context);
        validateCellPaddingAttribute(context);
        validateCellSpacingAttribute(context);
        validateHeightPixelsOnlyAttribute(context);
        validateHorizontalAlignmentAttribute(context, element);
        validateVerticalAlignmentAttribute(context, element);
        validateWidthPixelsOrPercentAttributes(context, element);
    }

    private void validateBackgroundColorAttribute(ValidationContext context) {

        Step step = context.pushPropertyStep("backgroundColor");
        String bgcolor = getBackgroundColour();
        if (bgcolor != null) {
            if (!LayoutTypeValidator.isRGBOrColorNameType(bgcolor)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.BACKGROUND_COLOR_ILLEGAL, bgcolor));
            }
        }
        context.popStep(step);
    }

    private void validateBackgroundComponentAttributes(
        final ValidationContext context, String element) {

        // backgroundComponent requires no validation.

        Step step = context.pushPropertyStep("backgroundComponentType");
        final String backgroundComponentType = getBackgroundComponentType();
        if (backgroundComponentType != null) {
            Set keywords = LayoutTypeValidator
                .getOldKeywords(element, "backgroundComponentType",
                    new String[]{"image", "dynamic-visual"});
            if (!keywords.contains(backgroundComponentType)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.BACKGROUND_COMPONENT_TYPE_ILLEGAL,
                    backgroundComponentType));
            }
        }
        context.popStep(step);
    }

    void addErrorDiagnostic(ValidationContext context, I18NMessage message) {
        context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR, message);
    }

    private void validateBorderWidthAttribute(ValidationContext context) {

        Step step = context.pushPropertyStep("borderWidth");
        final String borderWidth = getBorderWidth();
        if (borderWidth != null) {
            if (!LayoutTypeValidator.isUnsigned(borderWidth)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.BORDER_WIDTH_ILLEGAL, borderWidth));
            }
        }
        context.popStep(step);
    }

    private void validateCellPaddingAttribute(ValidationContext context) {

        Step step = context.pushPropertyStep("cellPadding");
        final String cellPadding = getCellPadding();
        if (cellPadding != null) {
            if (!LayoutTypeValidator.isUnsigned(cellPadding)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.CELL_PADDING_ILLEGAL, cellPadding));
            }
        }
        context.popStep(step);
    }

    private void validateCellSpacingAttribute(ValidationContext context) {

        Step step = context.pushPropertyStep("cellSpacing");
        final String cellSpacing = getCellSpacing();
        if (cellSpacing != null) {
            if (!LayoutTypeValidator.isUnsigned(cellSpacing)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.CELL_SPACING_ILLEGAL, cellSpacing));
            }
        }
        context.popStep(step);
    }

    private void validateHeightPixelsOnlyAttribute(ValidationContext context) {
        validateHeightPixelsOnlyAttribute(context, this);
    }

    protected void validateHeightPixelsOnlyAttribute(ValidationContext context,
        HeightAttributes attributes) {

        final String height = attributes.getHeight();
        Step step = context.pushPropertyStep("height");
        if (height != null) {
            if (!LayoutTypeValidator.isUnsigned(height)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.HEIGHT_ILLEGAL, height));
            }
        }
        context.popStep(step);
    }

    private void validateHorizontalAlignmentAttribute(
        ValidationContext context, String element) {

        Step step = context.pushPropertyStep("horizontalAlignment");
        final String horizontalAlignment = getHorizontalAlignment();
        if (horizontalAlignment != null) {
            Set keywords = LayoutTypeValidator
                .getOldKeywords(element, "horizontalAlignment",
                    new String[]{"left", "center", "right", "start", "end"});
            if (!keywords.contains(horizontalAlignment)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.HORIZONTAL_ALIGNMENT_ILLEGAL,
                    horizontalAlignment));
            }
        }
        context.popStep(step);
    }

    private void validateVerticalAlignmentAttribute(ValidationContext context,
        String element) {

        Step step = context.pushPropertyStep("verticalAlignment");
        final String verticalAlignment = getVerticalAlignment();
        if (verticalAlignment != null) {
            Set keywords = LayoutTypeValidator
                .getOldKeywords(element, "verticalAlignment",
                    new String[]{"top", "center", "bottom"});
            if (!keywords.contains(verticalAlignment)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.VERTICAL_ALIGNMENT_ILLEGAL,
                    verticalAlignment));
            }
        }
        context.popStep(step);
    }

    private void validateWidthPixelsOrPercentAttributes(
        final ValidationContext context, String element) {
        validateWidthPixelsOrPercentAttributes(context, element, this);
    }

    protected void validateWidthPixelsOrPercentAttributes(
        final ValidationContext context, String element,
        WidthAttributes attributes) {

        final String width = attributes.getWidth();
        Step step = context.pushPropertyStep("width");
        if (width != null) {
            if (!LayoutTypeValidator.isUnsigned(width)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.WIDTH_ILLEGAL, width));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("widthUnits");
        final String widthUnits = attributes.getWidthUnits();
        if (widthUnits != null) {
            if (!LayoutTypeValidator
                .isPixelsOrPercentType(element, "widthUnits", widthUnits)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.WIDTH_UNITS_ILLEGAL, widthUnits));
            }
        }
        context.popStep(step);
    }

    protected void validateAdditionalNonDissectablePaneAndGridAttributes(
        final ValidationContext context, String element) {

        validateOptimizationLevelAttribute(context, element);
    }

    /**
     * Validate all the child Formats of this Format.
     *
     * @param context the validation context.
     */
    protected void validateChildren(ValidationContext context) {

        // todo: later: add validation for those formats that can have only one
        // child and must have only one - eg spatial.

        // Iterate over the children, validating them.
        if (children != null && children.length > 0) {
            Step step = context.pushPropertyStep(FORMATS);
            for (int i = 0; i < children.length; i++) {
                Format format = children[i];
                if (format != null) {
                    Step indexedStep = context.pushIndexedStep(i);
                    format.validate(context);
                    context.popStep(indexedStep);
                }
            }
            context.popStep(step);
        }
    }

    protected void validateOptimizationLevelAttribute(
        ValidationContext context, String element) {
        Step step = context.pushPropertyStep("optimizationLevel");
        final OptimizationLevelAttribute attributes =
            (OptimizationLevelAttribute) this;
        final String optimizationLevel = attributes.getOptimizationLevel();
        if (optimizationLevel != null) {
            Set keywords = LayoutTypeValidator
                .getOldKeywords(element, "optimizationLevel",
                    new String[]{"never", "little-impact", "always"});
            if (!keywords.contains(optimizationLevel)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.OPTIMIZATION_LEVEL_ILLEGAL,
                    optimizationLevel));
            }
        }
        context.popStep(step);
    }

    protected void validateStyleClassAttribute(
        final ValidationContext context) {

        final StyleAttributes attributes = (StyleAttributes) this;
        validateStyleClassAttribute(context, attributes);
    }

    protected void validateStyleClassAttribute(final ValidationContext context,
        final StyleAttributes attributes) {

        Step step = context.pushPropertyStep("styleClass");
        final String styleClass = attributes.getStyleClass();
        if (styleClass != null) {
            if (!LayoutTypeValidator.isThemeClassNameType(styleClass)) {
                addErrorDiagnostic(context, context.createMessage(
                    LayoutMessages.STYLE_CLASS_ILLEGAL, styleClass));
            }
        }
        context.popStep(step);
    }

    // Implement ITrackSource so we can collect the source location.
    // We only need to do this because we use custom unmarshalling for the
    // entire layout.

    private String jibx_documentName;

    private int jibx_lineNumber;

    private int jibx_columnNumber;

    /**
     * These comments are required otherwise the mock has the method defined
     * twice, once from the interface and once from this class. This is a bug
     * in XDoclet according to Paul.
     * 
     * @mock.ignore
     */
    public void jibx_setSource(final String documentName, final int lineNumber,
        final int columnNumber) {

        this.jibx_documentName = documentName;
        this.jibx_lineNumber = lineNumber;
        this.jibx_columnNumber = columnNumber;
    }

    /**
     * @mock.ignore
     */
    public int jibx_getColumnNumber() {

        return jibx_columnNumber;
    }

    /**
     * @mock.ignore
     */
    public String jibx_getDocumentName() {

        return jibx_documentName;
    }

    /**
     * @mock.ignore
     */
    public int jibx_getLineNumber() {

        return jibx_lineNumber;
    }

    // javadoc inherited
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;

        final Format format = (Format) other;

        return getFormatType().equals(format.getFormatType()) &&
            dimensions == format.dimensions &&
            Arrays.equals(children, format.children) &&
            Arrays.equals(maxCells, format.maxCells);
    }

    // javadoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getFormatType().hashCode();
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + dimensions;
        result = 31 * result + (maxCells != null ? maxCells.hashCode() : 0);
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9590/3	schaloner	VBM:2005092204 Added width, height, and style accessor interfaces derived from CoreAttributes interface

 18-Aug-05	9007/4	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 18-Feb-05	7037/1	pcameron	VBM:2005021704 Width units default to percent if not present

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/1	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Jul-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 09-Jan-04	2461/3	steve	VBM:2003121701 Check for non-existant or empty names

 08-Jan-04	2461/1	steve	VBM:2003121701 Patch pane name changes from Proteus2

 07-Jan-04	2389/4	steve	VBM:2003121701 Enhanced pane referencing

 06-Jan-04	2389/2	steve	VBM:2003121701 Pre-test save

 ===========================================================================
*/
