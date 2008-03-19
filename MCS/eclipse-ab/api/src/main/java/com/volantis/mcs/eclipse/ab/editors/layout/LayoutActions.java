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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.ab.actions.DefaultActionCommand;
import com.volantis.mcs.eclipse.ab.actions.GlobalActions;
import com.volantis.mcs.eclipse.ab.actions.ODOMAction;
import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.ab.actions.SubMenuAction;
import com.volantis.mcs.eclipse.ab.actions.layout.ActionID;
import com.volantis.mcs.eclipse.ab.actions.layout.AttributesAction;
import com.volantis.mcs.eclipse.ab.actions.layout.ColumnDeleteActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.ColumnInsertActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.CopyActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.CutActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.DeleteActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.NewGridFormatActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.NewMenuActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.NewNonGridFormatActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.PasteActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.ReplaceActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.RowDeleteActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.RowInsertActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.SwapActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.WrapGridFormatActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.WrapMenuActionCommand;
import com.volantis.mcs.eclipse.ab.actions.layout.WrapNonGridFormatActionCommand;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Display;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This class creates the actions required for editing layouts.
 */
public class LayoutActions {

    /**
     * The bundle associated with LayoutActions.
     */
    private static final ResourceBundle BUNDLE =
            LayoutMessages.getResourceBundle();

    /**
     * Constructs the actions and places them in a map, indexed by their
     * action IDs.
     */
    public static Map createActions(final ODOMEditorContext context) {
        final Map actions = new HashMap();
        final ODOMSelectionManager manager = context.
                getODOMSelectionManager();

        actions.put(ActionID.CANVAS_NEW_MENU,
                new SubMenuAction(
                        new NewMenuActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.CANVAS_NEW_MENU.getPrefix()));
        actions.put(ActionID.NEW_PANE_MENU,
                new SubMenuAction(
                        new DefaultActionCommand(),
                        LayoutActions.BUNDLE,
                        ActionID.NEW_PANE_MENU.getPrefix()));
        actions.put(ActionID.NEW_PANE,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.PANE,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_PANE.getPrefix()));
        actions.put(ActionID.NEW_ROW_ITERATOR_PANE,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.ROW_ITERATOR_PANE,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_ROW_ITERATOR_PANE.getPrefix()));
        actions.put(ActionID.NEW_COLUMN_ITERATOR_PANE,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.COLUMN_ITERATOR_PANE,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_COLUMN_ITERATOR_PANE.getPrefix()));
        actions.put(ActionID.NEW_DISSECTING_PANE,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.DISSECTING_PANE,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_DISSECTING_PANE.getPrefix()));
        actions.put(ActionID.NEW_ITERATOR_MENU,
                new SubMenuAction(
                        new DefaultActionCommand(),
                        LayoutActions.BUNDLE,
                        ActionID.NEW_ITERATOR_MENU.getPrefix()));
        actions.put(ActionID.NEW_SPATIAL_ITERATOR,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.SPATIAL_FORMAT_ITERATOR,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_SPATIAL_ITERATOR.getPrefix()));
        actions.put(ActionID.NEW_TEMPORAL_ITERATOR,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.TEMPORAL_FORMAT_ITERATOR,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_TEMPORAL_ITERATOR.getPrefix()));
        actions.put(ActionID.NEW_GRID_MENU,
                new SubMenuAction(
                        new DefaultActionCommand(),
                        LayoutActions.BUNDLE,
                        ActionID.NEW_GRID_MENU.getPrefix()));
        actions.put(ActionID.NEW_2_COLUMN_GRID,
                new ODOMAction(
                        new NewGridFormatActionCommand(FormatType.GRID,
                                manager) {
                            protected Dimension getGridDimensions() {
                                return new Dimension(2, 1);
                            }
                        },
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_2_COLUMN_GRID.getPrefix()));
        actions.put(ActionID.NEW_3_ROW_GRID,
                new ODOMAction(
                        new NewGridFormatActionCommand(FormatType.GRID,
                                manager) {
                            protected Dimension getGridDimensions() {
                                return new Dimension(1, 3);
                            }
                        },
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_3_ROW_GRID.getPrefix()));
        actions.put(ActionID.NEW_N_BY_M_GRID,
                new ODOMAction(
                        new NewGridFormatActionCommand(FormatType.GRID,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_N_BY_M_GRID.getPrefix()));
        actions.put(ActionID.NEW_FRAGMENT,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.FRAGMENT, manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_FRAGMENT.getPrefix()));
        actions.put(ActionID.NEW_FORM,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.FORM,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_FORM.getPrefix()));
        actions.put(ActionID.NEW_REGION,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.REGION,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_REGION.getPrefix()));
        actions.put(ActionID.NEW_REPLICA,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.REPLICA,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_REPLICA.getPrefix()));
        actions.put(ActionID.NEW_FORM_FRAGMENT,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.FORM_FRAGMENT,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_FORM_FRAGMENT.getPrefix()));

        actions.put(ActionID.MONTAGE_NEW_MENU,
                new SubMenuAction(
                        new NewMenuActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.MONTAGE_NEW_MENU.getPrefix()));
        actions.put(ActionID.NEW_SEGMENT_GRID_MENU,
                new SubMenuAction(
                        new DefaultActionCommand(),
                        LayoutActions.BUNDLE,
                        ActionID.NEW_SEGMENT_GRID_MENU.getPrefix()));
        actions.put(ActionID.NEW_2_COLUMN_SEGMENT_GRID,
                new ODOMAction(
                        new NewGridFormatActionCommand(
                                FormatType.SEGMENT_GRID,
                                manager) {
                            protected Dimension getGridDimensions() {
                                return new Dimension(2, 1);
                            }
                        },
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_2_COLUMN_SEGMENT_GRID.getPrefix()));
        actions.put(ActionID.NEW_3_ROW_SEGMENT_GRID,
                new ODOMAction(
                        new NewGridFormatActionCommand(
                                FormatType.SEGMENT_GRID,
                                manager) {
                            protected Dimension getGridDimensions() {
                                return new Dimension(1, 3);
                            }
                        },
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_3_ROW_SEGMENT_GRID.getPrefix()));
        actions.put(ActionID.NEW_N_BY_M_SEGMENT_GRID,
                new ODOMAction(
                        new NewGridFormatActionCommand(
                                FormatType.SEGMENT_GRID,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_N_BY_M_SEGMENT_GRID.getPrefix()));
        actions.put(ActionID.NEW_SEGMENT,
                new ODOMAction(
                        new NewNonGridFormatActionCommand(
                                FormatType.SEGMENT,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.NEW_SEGMENT.getPrefix()));

        actions.put(ActionID.UNDEFINED_NEW_MENU,
                new SubMenuAction(
                        new DefaultActionCommand() {
                            // javadoc inherited
                            public boolean enable(ODOMActionDetails context) {
                                return false;
                            }
                        },
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.UNDEFINED_NEW_MENU.getPrefix()));

        actions.put(ActionID.CANVAS_WRAP_MENU,
                new SubMenuAction(
                        new WrapMenuActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.CANVAS_WRAP_MENU.getPrefix()));
        actions.put(ActionID.WRAP_ITERATOR_MENU,
                new SubMenuAction(
                        new DefaultActionCommand(),
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_ITERATOR_MENU.getPrefix()));
        actions.put(ActionID.WRAP_SPATIAL_ITERATOR,
                new ODOMAction(
                        new WrapNonGridFormatActionCommand(
                                FormatType.SPATIAL_FORMAT_ITERATOR,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_SPATIAL_ITERATOR.getPrefix()));
        actions.put(ActionID.WRAP_TEMPORAL_ITERATOR,
                new ODOMAction(
                        new WrapNonGridFormatActionCommand(
                                FormatType.TEMPORAL_FORMAT_ITERATOR,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_TEMPORAL_ITERATOR.getPrefix()));
        actions.put(ActionID.WRAP_N_BY_M_GRID,
                new ODOMAction(
                        new WrapGridFormatActionCommand(FormatType.GRID,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_N_BY_M_GRID.getPrefix()));
        actions.put(ActionID.WRAP_FRAGMENT,
                new ODOMAction(
                        new WrapNonGridFormatActionCommand(
                                FormatType.FRAGMENT, manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_FRAGMENT.getPrefix()));
        actions.put(ActionID.WRAP_FORM,
                new ODOMAction(
                        new WrapNonGridFormatActionCommand(
                                FormatType.FORM,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_FORM.getPrefix()));
        actions.put(ActionID.WRAP_FORM_FRAGMENT,
                new ODOMAction(
                        new WrapNonGridFormatActionCommand(
                                FormatType.FORM_FRAGMENT,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_FORM_FRAGMENT.getPrefix()));

        actions.put(ActionID.MONTAGE_WRAP_MENU,
                new SubMenuAction(
                        new WrapMenuActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.MONTAGE_WRAP_MENU.getPrefix()));
        actions.put(ActionID.WRAP_N_BY_M_SEGMENT_GRID,
                new ODOMAction(
                        new WrapGridFormatActionCommand(
                                FormatType.SEGMENT_GRID,
                                manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.WRAP_N_BY_M_SEGMENT_GRID.getPrefix()));

        actions.put(ActionID.UNDEFINED_WRAP_MENU,
                new SubMenuAction(
                        new DefaultActionCommand() {
                            // javadoc inherited
                            public boolean enable(ODOMActionDetails context) {
                                return false;
                            }
                        },
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.UNDEFINED_WRAP_MENU.getPrefix()));

        actions.put(ActionID.CUT,
                new ODOMAction(
                        new CutActionCommand(
                                EclipseCommonPlugin.getClipboard(
                                        Display.getDefault()), manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.CUT.getPrefix()));
        actions.put(ActionID.COPY,
                new ODOMAction(
                        new CopyActionCommand(
                                EclipseCommonPlugin.getClipboard(
                                        Display.getDefault()), manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.COPY.getPrefix()));
        actions.put(ActionID.PASTE,
                new ODOMAction(
                        new PasteActionCommand(
                                EclipseCommonPlugin.getClipboard(
                                        Display.getDefault()),
                                context),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.PASTE.getPrefix()));
        actions.put(ActionID.DELETE,
                new ODOMAction(
                        new DeleteActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.DELETE.getPrefix()));
        actions.put(ActionID.REPLACE,
                new ODOMAction(
                        new ReplaceActionCommand(
                                EclipseCommonPlugin.getClipboard(
                                        Display.getDefault()),
                                context),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.REPLACE.getPrefix()));
        actions.put(ActionID.SWAP,
                new ODOMAction(new SwapActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.SWAP.getPrefix()));
        actions.put(ActionID.GRID_MODIFY_MENU,
                new SubMenuAction(
                        // @todo later implement: placeholder only
                        new DefaultActionCommand(),
                        LayoutActions.BUNDLE,
                        ActionID.GRID_MODIFY_MENU.getPrefix()));
        actions.put(ActionID.INSERT_ROWS,
                new ODOMAction(
                        new RowInsertActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.INSERT_ROWS.getPrefix()));
        actions.put(ActionID.INSERT_COLUMNS,
                new ODOMAction(
                        new ColumnInsertActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.INSERT_COLUMNS.getPrefix()));
        actions.put(ActionID.DELETE_COLUMN,
                new ODOMAction(
                        new ColumnDeleteActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.DELETE_COLUMN.getPrefix()));
        actions.put(ActionID.DELETE_ROW,
                new ODOMAction(
                        new RowDeleteActionCommand(manager),
                        context,
                        null,
                        LayoutActions.BUNDLE,
                        ActionID.DELETE_ROW.getPrefix()));
        actions.put(ActionID.SHOW_ATTRIBUTE_VIEW,
                new AttributesAction(
                        LayoutActions.BUNDLE,
                        ActionID.SHOW_ATTRIBUTE_VIEW.getPrefix()));

        return actions;
    }

    /**
     * Gets the global actions from the supplied actions.
     * @param actions the actions from which to extract the global ones
     * @return the global actions
     */
    public static GlobalActions getGlobalActions(Map actions) {
        GlobalActions globalActions = new GlobalActions(
                (IAction) actions.get(ActionID.COPY),
                (IAction) actions.get(ActionID.CUT),
                (IAction) actions.get(ActionID.DELETE),
                (IAction) actions.get(ActionID.PASTE),
                null);
        return globalActions;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8213/1	pcameron	VBM:2005031015 Added global actions to the Layout Outline Page

 ===========================================================================
*/
