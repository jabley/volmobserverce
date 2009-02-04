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
package com.volantis.mcs.eclipse.builder.wizards.themes;

import com.volantis.mcs.eclipse.builder.editors.themes.ThemesMessages;
import com.volantis.mcs.eclipse.controls.ObjectDialogListBuilder;
import com.volantis.mcs.eclipse.controls.ValidatedObjectControlFactory;
import com.volantis.mcs.themes.CombinatorEnum;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.Subject;
import com.volantis.mcs.themes.parsing.ObjectParser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * Control for building up a list of selector sequences.
 */
public class SelectorSequenceDialogListBuilder extends ObjectDialogListBuilder {
    /**
     * Resource prefix for the DialogListBuilder.
     */
    private final static String RESOURCE_PREFIX = "SelectorSequenceDialogListBuilder.";

    /**
     * The text for the Append button.
     */
    private static final String APPEND_TEXT =
            ThemesMessages.getString(RESOURCE_PREFIX + "button.append");

    /**
     * The text for the Child menu option.
     */
    private static final String CHILD_TEXT =
            ThemesMessages.getString(RESOURCE_PREFIX + "menu.child");

    /**
     * The text for the Descendant menu option.
     */
    private static final String DESCENDANT_TEXT =
            ThemesMessages.getString(RESOURCE_PREFIX + "menu.descendant");

    /**
     * The text for the Direct Adjacent menu option.
     */
    private static final String DIRECT_ADJACENT_TEXT =
            ThemesMessages.getString(RESOURCE_PREFIX + "menu.direct-adjacent");

    /**
     * The text for the Indirect Adjacent menu option.
     */
    private static final String INDIRECT_ADJACENT_TEXT =
            ThemesMessages.getString(RESOURCE_PREFIX + "menu.indirect-adjacent");

    /**
     * The append button
     */
    private Button appendButton;

    /**
     * Create a selector sequence dialog list builder.
     *
     * @param parent The parent composite in which the builder will be created
     * @param style The style of the builder
     * @param title The title to use for the list
     * @param items The initial contents of the list
     * @param duplicatesAllowed True if duplicate entries are to be allowed
     * @param factory The factory to use for creating list entries
     * @param parser The object parser to use for rendering the entries, or for
     *               converting from text to object if they are editable
     * @param editable True if the entries can be edited as text (requires an
     *                 appropriate parser)
     */
    public SelectorSequenceDialogListBuilder(Composite parent, int style,
                                             String title, Object[] items,
                                             boolean duplicatesAllowed,
                                             ValidatedObjectControlFactory factory,
                                             ObjectParser parser, boolean editable) {
        super(parent, style, title, items, duplicatesAllowed, factory, parser, editable);
    }

    /**
     * Adds the buttons for adding/appending new entries to the list.
     *
     * @param container The container in which the buttons are created
     */
    protected void addButtons(Composite container) {
        // Create a container which will hold both the button provided by the
        // parent class and the additional append button
        Composite buttonContainer = new Composite(container, SWT.NONE);
        GridLayout topLevelGrid = new GridLayout(1, false);
        topLevelGrid.horizontalSpacing = HORIZONTAL_SPACING;
        topLevelGrid.verticalSpacing = VERTICAL_SPACING;
        topLevelGrid.marginWidth = 0;
        topLevelGrid.marginHeight = 0;
        GridData buttonCompositeData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL |
                GridData.VERTICAL_ALIGN_END);
        buttonContainer.setLayoutData(buttonCompositeData);
        buttonContainer.setLayout(topLevelGrid);

        // Add any buttons required by the parent (effectively the add button)
        super.addButtons(buttonContainer);

        // Add the append button
        addAppendButton(buttonContainer);
    }

    /**
     * Add the append button and set up its event handling.
     *
     * @param container The container into which the append button should be
     *                  created
     */
    private void addAppendButton(Composite container) {
        appendButton = new Button(container, SWT.PUSH);
        appendButton.setText(APPEND_TEXT);
        GridData appendButtonData = new
                GridData(GridData.HORIZONTAL_ALIGN_FILL |
                GridData.VERTICAL_ALIGN_END);
        appendButton.setLayoutData(appendButtonData);

        // Build up the popup menu for the append button
        final Menu menu = new Menu(getShell(), SWT.POP_UP);
        MenuItem item = new MenuItem(menu, SWT.PUSH);
        item.setText(CHILD_TEXT);
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                appendSelected(CombinatorEnum.CHILD);
            }
        });

        item = new MenuItem(menu, SWT.PUSH);
        item.setText(DESCENDANT_TEXT);
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                appendSelected(CombinatorEnum.DESCENDANT);
            }
        });

        item = new MenuItem(menu, SWT.PUSH);
        item.setText(DIRECT_ADJACENT_TEXT);
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                appendSelected(CombinatorEnum.DIRECT_ADJACENT);
            }
        });

        item = new MenuItem(menu, SWT.PUSH);
        item.setText(INDIRECT_ADJACENT_TEXT);
        item.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                appendSelected(CombinatorEnum.INDIRECT_ADJACENT);
            }
        });

        // Associate the menu with the button
        appendButton.setMenu(menu);
        appendButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Rectangle rect = appendButton.getBounds();
                rect = getDisplay(). map(appendButton.getParent(), null, rect);
                int x = rect.x;
                int y = rect.y + rect.height;
                menu.setLocation(x, y);
                menu.setVisible(true);
            }
        });
    }

    /**
     * Appends the entered data to the selected entry from the list.
     *
     * @param combinator The combinator to use when appending
     */
    private void appendSelected(CombinatorEnum combinator) {

        int index = getSelectedIndex();

        Subject subject = (Subject) getSelectedObject();
        SelectorSequence sequence =
                (SelectorSequence) getObjectControl().getValue();
        CombinedSelector combined = subject.append(combinator, sequence);

        removeFromList(index);
        addToList(combined, index);
        setSelectedIndex(index);

        getObjectControl().setValue(null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
