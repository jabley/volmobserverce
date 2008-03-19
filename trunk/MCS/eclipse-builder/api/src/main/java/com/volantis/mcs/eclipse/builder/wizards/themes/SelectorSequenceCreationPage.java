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
import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.controls.ValidatedObjectControlFactory;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import com.volantis.mcs.themes.SelectorGroup;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.parsing.ObjectParserFactory;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.validation.Validator;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.Diagnostic;
import com.volantis.synergetics.ObjectHelper;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import java.util.List;
import java.util.Iterator;

/**
 * Wizard page for creating selector sequences.
 */
public class SelectorSequenceCreationPage extends WizardPage {
    /**
     * Resource prefix for the SelectorCreationPage.
     */
    private final static String RESOURCE_PREFIX = "SelectorCreationPage.";

    /**
     * The path to the icon that this page should use
     */
    private static final String RULE_ICON = ThemesMessages.getString(
            RESOURCE_PREFIX + "icon");

    /**
     * The title for this page
     */
    private static final String PAGE_TITLE = ThemesMessages.getString(
            RESOURCE_PREFIX + "title");

    /**
     * The description for this page
     */
    private static final String PAGE_DESCRIPTION = ThemesMessages.getString(
            RESOURCE_PREFIX + "description");

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING = ThemesMessages.getInteger(
            RESOURCE_PREFIX + "horizontalSpacing").intValue();

    /**
     * The margin height of the page.
     */
    private static final int MARGIN_HEIGHT = ThemesMessages.getInteger(
            RESOURCE_PREFIX + "marginHeight").intValue();

    /**
     * The margin width of the page.
     */
    private static final int MARGIN_WIDTH = ThemesMessages.getInteger(
            RESOURCE_PREFIX + "marginWidth").intValue();

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING = ThemesMessages.getInteger(
            RESOURCE_PREFIX + "verticalSpacing").intValue();

    /**
     * The text for the list's label.
     */
    private static final String LIST_LABEL_TEXT =
            ThemesMessages.getString(RESOURCE_PREFIX + "listLabel");

    /**
     * The factory to use for creating theme model instances.
     */
    private static final StyleSheetFactory MODEL_FACTORY =
            StyleSheetFactory.getDefaultInstance();

    /**
     * The DialogListBuilder widget used by this page
     */
    private SelectorSequenceDialogListBuilder listBuilder;

    /**
     * The factory to use for creating the selector control
     */
    private final ValidatedObjectControlFactory controlFactory;

    /**
     * Initializes a {@link SelectorSequenceCreationPage} instance with the
     * given arguments.
     *
     * @param project the associated project
     */
    public SelectorSequenceCreationPage(IProject project) {
        super("SelectorCreationPage");

        // assert that the arguments passed in are valid
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }

        this.controlFactory = new SelectorSequenceProviderFactory(project);

        // set the pages title
        setTitle(PAGE_TITLE);

        // set the description
        setMessage(PAGE_DESCRIPTION);

        // Set the banner
        setImageDescriptor(BuilderPlugin.getImageDescriptor(RULE_ICON));
    }

    /**
     * Build the GUI for this wizard page.
     *
     * @param composite The parent composite in which the GUI will be built
     */
    public void createControl(Composite composite) {
        // create the wizard page
        Composite topLevel = new Composite(composite, SWT.NONE);

        GridLayout layoutGrid = new GridLayout();
        layoutGrid.horizontalSpacing = HORIZONTAL_SPACING;
        layoutGrid.verticalSpacing = VERTICAL_SPACING;
        layoutGrid.marginHeight = MARGIN_HEIGHT;
        layoutGrid.marginWidth = MARGIN_WIDTH;

        topLevel.setLayout(layoutGrid);
        // create the list and text control
        addListBuilder(topLevel);
        topLevel.layout();
        setControl(topLevel);

        // set the page complete status
        setPageComplete(true);
    }

    /**
     * Creates and adds the DialogListBuilder with a ModifyListener to listen for
     * item removals, and populates the list with an initial selection.
     * @param parent the parent composite
     */
    private void addListBuilder(Composite parent) {
        listBuilder = new SelectorSequenceDialogListBuilder(
                parent,
                SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL,
                LIST_LABEL_TEXT,
                null, // no current selection for this wizard
                true, // Duplicate selectors values are allowed
                controlFactory,
                ObjectParserFactory.getDefaultInstance().createRuleSelectorParser(),
                false);

        listBuilder.setLayoutData(new GridData(GridData.FILL_BOTH));

        listBuilder.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                getWizard().getContainer().updateButtons();
                validateSelectors();
            }
        });

        listBuilder.getObjectControl().addStateChangeListener(new StateChangeListener() {
            public void stateChanged() {
                validateSelectors();
            }
        });
    }

    /**
     * Validate the selectors and set an appropriate error message.
     */
    private void validateSelectors() {
        String errorMessage = null;

        Object object = listBuilder.getObjectControl().getValue();
        if (object != null && object instanceof Validatable) {
            ModelFactory modelFactory = ModelFactory.getDefaultInstance();
            Validator validator = modelFactory.createValidator();

            // Validate the object being created
            validator.validate((Validatable) object);

            // Validate the already created selectors
            List selectors = listBuilder.getItems();
            Iterator it = selectors.iterator();
            while (it.hasNext() && validator.getDiagnostics().isEmpty()) {
                validator.validate((Validatable) it.next());
            }

            // Retrieve the first error message
            List diagnostics = validator.getDiagnostics();
            if (!diagnostics.isEmpty()) {
                Diagnostic firstDiagnostic = (Diagnostic) diagnostics.get(0);
                errorMessage = firstDiagnostic.getMessage().getMessage();
            }
        }

        // Only change the error message if it has a new value.
        if (!ObjectHelper.equals(getErrorMessage(), errorMessage)) {
            setErrorMessage(errorMessage);
        }
    }

    /**
     * Returns true if the page can allow the Finish button to be pressed -
     * in this case the check is whether the list builder contains any
     * selectors.
     *
     * @return True if the page can finish
     */
    public boolean canFinish() {
        return !listBuilder.getItems().isEmpty();
    }

    /**
     * Returns the selector group represented by the control.
     *
     * @return The selector group represented by the control
     */
    public SelectorGroup getSelectorGroup() {
        List selectors = listBuilder.getItems();
        SelectorGroup group = MODEL_FACTORY.createSelectorGroup();
        group.setSelectors(selectors);
        return group;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10290/1	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 11-Nov-05	10266/4	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 08-Nov-05	10195/1	adrianj	VBM:2005101803 Display deprecated elements in selector wizard

 07-Nov-05	10179/1	adrianj	VBM:2005101803 Show deprecated elements in selector group wizard

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
