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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatIterator.java,v 1.5 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Oct-02    Allan           VBM:2002101805 - An abstract Format for
 *                              FormatIteator formats.
 * 04-Nov-02    Allan           VBM:2002110403 - Added a the
 *                              ITERATOR_SEPARATOR_ATTRIBUTE constant and as
 *                              as attribute to the list of attribtes.
 * 07-Nov-02    Allan           VBM:2002110506 - Added choice constants for
 *                              combos and labels. 
 * 25-Nov-02    Payal           VBM:2002111804 - Removed the constants for 
 *                              ITERATOR_COMBO_CHOICES,ITERATOR_LABEL_CHOICES 
 *                              and moved it into FormatPropertiesDialog.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() and visitChildren()
 *                              methods.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.mcs.model.validation.ValidationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract Format for FormatIteator formats. FormatIterators only have
 * a single child of any Format type.
 *
 * @mock.generate base="Format"
 */
public abstract class FormatIterator extends Format {

    /**
     * Attributes associated with this Format (a.k.a userAttributes).
     */
    protected static List attributesList;

    /**
     * Attributes that have default values (a.k.a defaultAttributes).
     */
    protected static List attributesWithDefaultsList;

    /**
     * Attributes that are saved to the repository (a.k.a persistentAttributes)
     */
    protected static List persistentAttributesList;

    /**
     * Constant representing the separator attribute for this Format.
     */
    public static final String ITERATOR_SEPARATOR_ATTRIBUTE = 
        "IteratorSeparator";

    /**
     * Initialize the property lists.
     */
    static {
        attributesList = new ArrayList();
        attributesList.add(FormatConstants.NAME_ATTRIBUTE);
        attributesList.add(FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE);
        attributesList.add(FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE);
        attributesList.add(FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE);
        attributesList.add(FormatConstants.BORDER_WIDTH_ATTRIBUTE);
        attributesList.add(FormatConstants.CELL_PADDING_ATTRIBUTE);
        attributesList.add(FormatConstants.CELL_SPACING_ATTRIBUTE);
        attributesList.add(FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE);
        attributesList.add(FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE);
        attributesList.add(FormatConstants.WIDTH_ATTRIBUTE);
        attributesList.add(FormatConstants.WIDTH_UNITS_ATTRIBUTE);
        attributesList.add(FormatConstants.HEIGHT_ATTRIBUTE);
        attributesList.add(ITERATOR_SEPARATOR_ATTRIBUTE);

        attributesWithDefaultsList = new ArrayList();
        attributesWithDefaultsList.add(FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE);
        attributesWithDefaultsList.add(FormatConstants.BORDER_WIDTH_ATTRIBUTE);
        attributesWithDefaultsList.add(FormatConstants.CELL_PADDING_ATTRIBUTE);
        attributesWithDefaultsList.add(FormatConstants.CELL_SPACING_ATTRIBUTE);
        attributesWithDefaultsList.add(FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE);
        attributesWithDefaultsList.add(FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE);
        attributesWithDefaultsList.add(FormatConstants.WIDTH_UNITS_ATTRIBUTE);
        
        persistentAttributesList = attributesList;
    }

    public FormatIterator(CanvasLayout canvasLayout,
                          FormatProperties defaults) {
        super(1, canvasLayout, defaults);
    }

    /**
     * Gets the iterator separator.
     * @return the iterator separator.
     */
    public String getIteratorSeparator() {
        return (String)getAttribute(ITERATOR_SEPARATOR_ATTRIBUTE);
    }

    /**
     * Sets the iterator separator.
     * @param iteratorSeparator the iterator separator
     */
    public void setIteratorSeparator(String iteratorSeparator) {
        setAttribute(ITERATOR_SEPARATOR_ATTRIBUTE,
                     iteratorSeparator);
    }

    /**
     * Call the relevant method in the visitor for this class of object.
     * @param visitor The object which is visiting this format.
     * @param object Some extra information which the visitor needs.
     * @return True to stop visiting and false to continue.
     */
    public boolean visit(FormatVisitor visitor, Object object) 
            throws FormatVisitorException {
        return false;
    }

    /**
     * Visit all the children of this format in order.
     * @param visitor The object which is visiting this format.
     * @param object Some extra information which the visitor needs.
     * @return True if the result of visting any child is true and false
     * otherwise.
     */
    public boolean visitChildren(FormatVisitor visitor, Object object) 
            throws FormatVisitorException {
        if(children == null) {
            return false;
        }

        Format child = children [0];

        if(child != null) {
            boolean done = child.visit(visitor, object);
            if(done) {
                return done;
            }
        }

        return false;
    }

    void validateFormatIteratorAttributes(ValidationContext context,
            String element) {

        validateOptionalName(context);
        validateAllPaneAndGridAndIteratorAttributes(context, element);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/3	schaloner	VBM:2005092204 Added accessors for FormatIterator.ITERATOR_SEPARATOR_ATTRIBTUE

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
