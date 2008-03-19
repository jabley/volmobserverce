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
package com.volantis.mcs.eclipse.ab.editors.layout.states;

import com.volantis.mcs.eclipse.ab.editors.layout.FormatComposite;
import com.volantis.mcs.eclipse.ab.editors.layout.FormatCompositeModifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.UndeclaredThrowableException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

import java.util.List;

/**
 * The FormatModifyState is used to insert and delete single formats. Its next
 * state is always itself.
 */
public class FormatModifyState implements GridModifierState {

    /**
     * An element filter used to filter out all non element nodes.
     */
    private static final ElementFilter ELEMENT_FILTER = new ElementFilter();

    /**
     * The FormatCompositeModifier used to access the GridFormatComposite's data.
     */
    private FormatCompositeModifier formatCompositeModifier;

    /**
     * Creates a FormatModifyState for inserting or deleting formats.
     * in the GridFormatComposite of the supplied FormatCompositeModifier.
     * @param gridModifier the FormatCompositeModifier
     */
    public FormatModifyState(FormatCompositeModifier gridModifier) {
        this.formatCompositeModifier = gridModifier;
    }

    /**
     * Processes the ODOMChangeEvent for a format insertion or deletion.
     * @param event the event to process
     * @return the next state for FormatModifyState. The next state is always
     *         itself
     */
    public GridModifierState processEvent(ODOMChangeEvent event) {
        if (event.getSource() instanceof Element) {
            ODOMElement element = formatCompositeModifier.getElement();
            ODOMElement source = (ODOMElement) event.getSource();

            // Events that are processed here will either be within a
            // grid row that is inside a grid or segment or they will be
            // within a format that can only contain a single child.
            // Events that originate for the root format are handled
            // elsewhere.

            FormatType.Structure structure =
                    formatCompositeModifier.getFormatComposite().
                    getFormatType().getStructure();

            // Only interested in events from the descendents of element
            // and not from element itself.
            if (source != element) {
                Element sourceParent = source.getParent() != null ?
                        source.getParent() : event.getNewValue() != null ?
                        (Element) event.getNewValue() :
                        (Element) event.getOldValue();

                Element parentFCElement = sourceParent;
                if (structure == FormatType.Structure.GRID) {
                    // sourceParent is a row so the grid is its parent.
                    parentFCElement = sourceParent.getParent();
                }

                if (event.getNewValue() == null &&
                        parentFCElement == element) {
                    FormatComposite childFC =
                            formatCompositeModifier.
                            getChildFCForElement(source);

                    if (childFC != null) {
                        formatCompositeModifier.removeChildFC(childFC);
                        formatCompositeModifier.getFormatComposite().
                                updateSelection(true);
                    }
                } else if (event.getOldValue() == null &&
                        parentFCElement == element) {

                    // Find which cell is affected by the new insertion.
                    int row = 0;
                    int column = 0;
                    if (structure == FormatType.Structure.GRID) {
                        row = getGridRows(parentFCElement, sourceParent).
                                indexOf(sourceParent);
                        column = sourceParent.getContent(ELEMENT_FILTER).
                                indexOf(source);
                    }
                    formatCompositeModifier.insertFC(source, row, column);
                }
            }
        }
        // Always return itself as the next state.
        return this;
    }

    /**
     * Given a grid element provide the grid row elements for that grid.
     * @param grid the grid Element
     * @param gridRowElement the Element that represents the grid row - e.g.
     * a gridFormatRow, segmentGridFormatRow.
     * @return the List of rows with the grid element
     */
    private List getGridRows(Element grid, Element gridRowElement) {
        StringBuffer buffer =
                new StringBuffer(grid.getNamespacePrefix().length() + 1 +
                gridRowElement.getName().length());
        buffer.append(grid.getNamespacePrefix()).append(":").
                append(gridRowElement.getName());
        XPath gridRows = new XPath(buffer.toString(),
                new Namespace[] { grid.getNamespace() });

        List gridRowFormats;
        try {
            gridRowFormats = gridRows.selectNodes(grid);
        } catch (XPathException e) {
            throw new UndeclaredThrowableException(e);
        }

        return gridRowFormats;
    }

    /**
     * Overridden so that the next state cannot be changed. The next state is
     * always the FormatModifyState itself.
     * @param nextState
     */
    public void setNextState(GridModifierState nextState) {
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

 03-Aug-04	4902/6	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 24-Feb-04	3021/6	pcameron	VBM:2004020211 Some tweaks to StyledGroup and FormatComposites

 24-Feb-04	3021/4	pcameron	VBM:2004020211 Added StyledGroup and background colours

 19-Feb-04	3021/2	pcameron	VBM:2004020211 Committed for integration

 13-Feb-04	2915/3	pcameron	VBM:2004020905 Used LayoutSchemaType for attribute names

 12-Feb-04	2915/1	pcameron	VBM:2004020905 Refactored GridFormatComposite to use the State pattern for grid mods

 ===========================================================================
*/
