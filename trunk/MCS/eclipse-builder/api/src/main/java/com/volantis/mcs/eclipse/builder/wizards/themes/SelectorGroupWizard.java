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
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.themes.SelectorGroup;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * A wizard for creating selector groups.
 */
public class SelectorGroupWizard extends Wizard {
    private static final String RESOURCE_PREFIX = "SelectorGroupWizard.";

    /**
     * Title for the wizard dialog
     */
    private static final String TITLE =
            ThemesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The width hint of the wizard page.
     */
    private static final int PAGE_WIDTH_HINT =
            EditorMessages.getInteger("Wizard." +
            "page.width.hint").intValue();

    /**
     * The single page that this wizard uses to allow clients to create the
     * rules selectors.
     */
    private SelectorSequenceCreationPage page;

    /**
     * This is the rule that this wizard is creating. Clients can access
     * it via the {@link #getSelectorGroup} method.
     */
    private SelectorGroup selectorGroup;

    /**
     * The associated project
     */
    private IProject project;

    /**
     * The parent shell of the wizard.
     */
    private Shell shell;

    /**
     * Initializes a <code>NewStyleRuleWizard</code> instance with the given
     * arguments
     *
     * @param shell the shell associate with the composite that invoked this
     *              wizard
     * @param project associated project
     */
    public SelectorGroupWizard(Shell shell, IProject project) {
        // assert that the arguments passed in are valid
        if (shell == null) {
            throw new IllegalArgumentException("Shell cannot be null.");
        }
        if (project == null) {
            throw new IllegalArgumentException("Project cannot be null.");
        }
        this.shell = shell;
        this.project = project;
        this.selectorGroup = null;

        // set the window title for this wizard
        setWindowTitle(TITLE);
    }

    // javadoc inherited
    public void addPages() {
        // create the single page that this wizard uses
        page = new SelectorSequenceCreationPage(project);
        addPage(page);
    }

    /**
     * Creates a WizardDialog and adds this NewStyleRuleWizard to it to it.
     * The WizardDialog is then opened.
     */
    public void open() {
        WizardDialog dialog = new WizardDialog(shell, this);
        dialog.create();

        // Force the dialog width to the suggested size and then allow it to
        // resize itself - this sets the width of the selector list column to
        // a sensible default.
        Shell dialogShell = dialog.getShell();
        dialogShell.setSize(PAGE_WIDTH_HINT, dialogShell.getSize().y);
        dialogShell.pack();

        dialog.open();
    }

    /**
     * Returns the SelectorGroup that this wizard is being used to
     * create. This method will return null if the wizard is cancelled.
     * @return A SelectorGroup element if this wizard was finished or null if
     *         it was cancelled.
     */
    public SelectorGroup getSelectorGroup() {
        return selectorGroup;
    }

    // Javadoc inherited
    public boolean performFinish() {
        boolean canFinish = page.canFinish();
        if (canFinish) {
            // Write the selector group to the rule
            selectorGroup = page.getSelectorGroup();
        }
        return canFinish;
    }

    public boolean canFinish() {
        return page.canFinish();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Nov-05	10290/1	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 11-Nov-05	10266/3	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 08-Nov-05	10195/1	adrianj	VBM:2005101803 Display deprecated elements in selector wizard

 07-Nov-05	10179/1	adrianj	VBM:2005101803 Show deprecated elements in selector group wizard

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/1	adrianj	VBM:2005101811 New themes GUI

 ===========================================================================
*/
