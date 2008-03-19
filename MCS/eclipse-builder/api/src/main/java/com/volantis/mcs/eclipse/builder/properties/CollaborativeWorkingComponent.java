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
package com.volantis.mcs.eclipse.builder.properties;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.eclipse.validation.*;
import com.volantis.mcs.eclipse.builder.common.ClassVersionProperties;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

/**
 * Component for collaborative working settings.
 */
public class CollaborativeWorkingComponent extends Composite implements Listener {

    private static final SimpleValidator NON_EMPTINESS_VALIDATOR =
        new SimpleValidator(1, -1);

    private static final BigIntegerValidator PORT_VALIDATOR =
        new BigIntegerValidator("1", "65535");

    /** Controls if collaborative working is enabled or disabled. */
    private Button collaborativeWorking;

    /** Control that holds the database name. */
    private Text databaseName;

    /** Control that holds the database project name. */
    private Text databaseProjectName;

    /** Control that holds the host name. */
    private Text hostName;

    /** Control that holds the port number. */
    private Text port;

    /** Control that holds the user name. */
    private Text userName;

    /** Control that holds the password. */
    private Text password;

    /** Control that holds the displayed name. */
    private Text displayName;

    /** Parent composite for the input fields. */
    private Composite collaborativeChildComposite;

    /**
     * The current project.
     * Can be null (for example when creating a new project), but no saving and
     * loading is available (throw NPE) until it is set.
     */
    private IProject project;

    /** Field to store the project name in case the project is null. */
    private String projectName;

    /** Change listeners. */
    private List listeners;

    /**
     * Creates a collaborative working component.
     * @param project the project object. Can be null.
     * @param parent the parent composite
     * @param style the style bits to use
     */
    public CollaborativeWorkingComponent(final IProject project,
                                         final Composite parent,
                                         final int style
    ) {
        super(parent, style);
        setLayout(new GridLayout(2, false));
        this.project = project;
        listeners = new LinkedList();
        NON_EMPTINESS_VALIDATOR.setStatusOnEmpty(Status.ERROR);
        PORT_VALIDATOR.setStatusOnEmpty(Status.OK);
        createControls(ClassVersionProperties.isCommunityEdition());
    }

    /**
     * Creates the necessary controls with empty values.
     */
    private void createControls(boolean communityEdition) {
        // collaborative working checkbox
        collaborativeWorking = new Button(this, SWT.CHECK);
        final GridData collabGridData = new GridData(GridData.FILL_HORIZONTAL);
        collabGridData.horizontalSpan = 2;
        collaborativeWorking.setLayoutData(collabGridData);
        collaborativeWorking.addSelectionListener(new ButtonSelectionListener());
        final String collaborativeLabelText;
        if (communityEdition) {
            collaborativeLabelText = EclipseCommonMessages.getString(
                    "MCSProjectProperties.collaborativeWorking.community");
            collaborativeWorking.setEnabled(false);
        } else {
            collaborativeLabelText = EclipseCommonMessages.getString(
                    "MCSProjectProperties.collaborativeWorking.professional");
        }
        collaborativeWorking.setText(collaborativeLabelText);

        collaborativeChildComposite = new Composite(this, SWT.NONE);
        collaborativeChildComposite.setLayout(new GridLayout(2, false));
        final GridData collaborativeCompositeGridData =
            new GridData(GridData.FILL_HORIZONTAL);
        collaborativeCompositeGridData.horizontalSpan = 2;
        collaborativeCompositeGridData.horizontalIndent = 10;
        collaborativeChildComposite.setLayoutData(
            collaborativeCompositeGridData);

        // database row
        final String databaseLabelText = EclipseCommonMessages.getString(
                "MCSProjectProperties.databaseName");
        final Label databaseLabel =
            new Label(collaborativeChildComposite, SWT.NONE);
        databaseLabel.setText(databaseLabelText);

        databaseName =
            new Text(collaborativeChildComposite, SWT.BORDER | SWT.SINGLE);
        final GridData databaseGridData = new GridData(GridData.FILL_HORIZONTAL);
        databaseGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue();
        databaseName.setLayoutData(databaseGridData);
        databaseName.addListener(SWT.Modify, this);

        // database project name row
        final String projectNameLabelText = EclipseCommonMessages.getString(
                "MCSProjectProperties.databaseProjectName");
        final Label projectNameLabel =
            new Label(collaborativeChildComposite, SWT.NONE);
        projectNameLabel.setText(projectNameLabelText);

        databaseProjectName =
            new Text(collaborativeChildComposite, SWT.BORDER | SWT.SINGLE);
        final GridData projectNameGridData =
            new GridData(GridData.FILL_HORIZONTAL);
        projectNameGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue();
        databaseProjectName.setLayoutData(projectNameGridData);
        databaseProjectName.addListener(SWT.Modify, this);

        // host name row
        final String hostNameLabelText = EclipseCommonMessages.getString(
                "MCSProjectProperties.hostName");
        final Label hostNameLabel =
            new Label(collaborativeChildComposite, SWT.NONE);
        hostNameLabel.setText(hostNameLabelText);

        hostName =
            new Text(collaborativeChildComposite, SWT.BORDER | SWT.SINGLE);
        final GridData hostNameGridData = new GridData(GridData.FILL_HORIZONTAL);
        hostNameGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue();
        hostName.setLayoutData(hostNameGridData);
        hostName.addListener(SWT.Modify, this);

        // port row
        final String portLabelText = EclipseCommonMessages.getString(
                "MCSProjectProperties.port");
        final Label portLabel = new Label(collaborativeChildComposite, SWT.NONE);
        portLabel.setText(portLabelText);

        port = new Text(collaborativeChildComposite, SWT.BORDER | SWT.SINGLE);
        port.setTextLimit(5);
        final GridData portGridData = new GridData();
        portGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.portWidth").intValue();
        port.setLayoutData(portGridData);
        port.addListener(SWT.Modify, this);

        // user name row
        final String userNameLabelText = EclipseCommonMessages.getString(
                "MCSProjectProperties.userName");
        final Label userNameLabel =
            new Label(collaborativeChildComposite, SWT.NONE);
        userNameLabel.setText(userNameLabelText);

        userName =
            new Text(collaborativeChildComposite, SWT.BORDER | SWT.SINGLE);
        final GridData userNameGridData = new GridData(GridData.FILL_HORIZONTAL);
        userNameGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue();
        userName.setLayoutData(userNameGridData);
        userName.addListener(SWT.Modify, this);

        // user name row
        final String passwordLabelText = EclipseCommonMessages.getString(
                "MCSProjectProperties.password");
        final Label passwordLabel =
            new Label(collaborativeChildComposite, SWT.NONE);
        passwordLabel.setText(passwordLabelText);

        password =
            new Text(collaborativeChildComposite, SWT.BORDER | SWT.SINGLE);
        password.setEchoChar('*');
        final GridData passwordGridData = new GridData(GridData.FILL_HORIZONTAL);
        passwordGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue();
        password.setLayoutData(passwordGridData);
        password.addListener(SWT.Modify, this);

        // separator rule
        final Label separator = new Label(collaborativeChildComposite,
            SWT.HORIZONTAL | SWT.SEPARATOR);
        final GridData separatorGridData =
            new GridData(GridData.FILL_HORIZONTAL);
        separatorGridData.horizontalSpan = 2;
        separator.setLayoutData(separatorGridData);

        // display name row
        final String displayNameLabelText = EclipseCommonMessages.getString(
                "MCSProjectProperties.displayName");
        final Label displayNameLabel =
            new Label(collaborativeChildComposite, SWT.NONE);
        displayNameLabel.setText(displayNameLabelText);

        displayName =
            new Text(collaborativeChildComposite, SWT.BORDER | SWT.SINGLE);
        final GridData displayNameGridData =
            new GridData(GridData.FILL_HORIZONTAL);
        displayNameGridData.widthHint = EclipseCommonMessages.getInteger(
                "MCSProjectProperties.Text.textWidth").intValue();
        displayName.setLayoutData(displayNameGridData);
        displayName.addListener(SWT.Modify, this);

        collaborativeChildComposite.setEnabled(false);
        disableCollabortativeWorkingComposite();
    }

    /**
     * Called whenever collaborative working is enabled or disabled.
     */
    private void collaborativeWorkingChanged() {
        if (collaborativeWorking.getSelection()) {
            enableCollaborativeWorkingComposite();
        } else {
            disableCollabortativeWorkingComposite();
        }
        notifyListeners();
    }

    /**
     * Loads collaborative working values from the previously set project.
     */
    public void loadCollaborativeWorking() {
        collaborativeWorking.setSelection(
            MCSProjectNature.getCollaborativeWorking(project));
        if (collaborativeWorking.getSelection()) {
            collaborativeChildComposite.setEnabled(true);
            databaseName.setText(MCSProjectNature.getDatabaseName(project));
            databaseProjectName.setText(
                MCSProjectNature.getDatabaseProjectName(project));
            hostName.setText(MCSProjectNature.getHostName(project));
            port.setText(MCSProjectNature.getPort(project));
            userName.setText(MCSProjectNature.getUserName(project));
            password.setText(MCSProjectNature.getPassword(project));
            displayName.setText(MCSProjectNature.getDisplayName(project));
        } else {
            disableCollabortativeWorkingComposite();
        }
    }

    /**
     * Saves collaborative working values into the previously set project.
     */
    public void saveCollaborativeWorking() {
        MCSProjectNature.setCollaborativeWorking(
            project, collaborativeWorking.getSelection());
        MCSProjectNature.setDatabaseName(
            project, databaseName.getText().trim());
        MCSProjectNature.setDatabaseProjectName(
            project, databaseProjectName.getText().trim());
        MCSProjectNature.setHostName(
            project, hostName.getText().trim());
        MCSProjectNature.setPort(
            project, port.getText().trim());
        MCSProjectNature.setUserName(
            project, userName.getText().trim());
        MCSProjectNature.setPassword(
            project, password.getText().trim());
        MCSProjectNature.setDisplayName(
            project, displayName.getText().trim());
    }

    /**
     * Enables the collaborative working child composite. Also initializes the
     * database project name field from the project or project name field.
     */
    private void enableCollaborativeWorkingComposite() {
        collaborativeChildComposite.setEnabled(true);
        setEnabled(true, collaborativeChildComposite.getChildren());
        if (project != null) {
            databaseProjectName.setText(project.getName());
        } else {
            databaseProjectName.setText(projectName);
        }
    }

    /**
     * Enables the collaborative working child composite and clears all the
     * input fields.
     */
    private void disableCollabortativeWorkingComposite() {
        collaborativeChildComposite.setEnabled(false);
        setEnabled(false, collaborativeChildComposite.getChildren());
        databaseName.setText("");
        databaseProjectName.setText("");
        hostName.setText("");
        port.setText("");
        userName.setText("");
        password.setText("");
        displayName.setText("");
    }

    /**
     * Validates the input fields.
     * @param messageContainer container for the error and info messages
     * @param messageBuilder message builder for the validators
     * @return true iff all the enabled input fields contain valid values
     */
    public boolean validate(final DialogPage messageContainer,
                            final ValidationMessageBuilder messageBuilder) {
        boolean valid = true;
        if (collaborativeWorking.getSelection()) {

            // validate the settings
            ValidationStatus status = NON_EMPTINESS_VALIDATOR.validate(
                databaseName.getText().trim(), messageBuilder);
            int severity = status.getSeverity();
            if (severity != Status.ERROR && severity != Status.INFO) {
                status = NON_EMPTINESS_VALIDATOR.validate(
                    databaseProjectName.getText().trim(), messageBuilder);
                severity = status.getSeverity();
            }
            if (severity != Status.ERROR && severity != Status.INFO) {
                status = NON_EMPTINESS_VALIDATOR.validate(
                    hostName.getText().trim(), messageBuilder);
                severity = status.getSeverity();
            }
            if (severity != Status.ERROR && severity != Status.INFO) {
                status = PORT_VALIDATOR.validate(
                    port.getText().trim(), messageBuilder);
                severity = status.getSeverity();
            }
            if (severity != Status.ERROR && severity != Status.INFO) {
                status = NON_EMPTINESS_VALIDATOR.validate(
                    displayName.getText().trim(), messageBuilder);
                severity = status.getSeverity();
            }

            if (severity == Status.ERROR) {
                messageContainer.setErrorMessage(status.getMessage());
                valid = false;
            } else if (severity == Status.INFO) {
                messageContainer.setErrorMessage(null);
                messageContainer.setMessage(status.getMessage(),
                    DialogPage.INFORMATION);
            } else {
                messageContainer.setErrorMessage(null);
                messageContainer.setMessage(null, PreferencePage.NONE);
            }
        }
        return valid;
    }

    /**
     * Adds an input field modification listener.
     *
     * @param listener the listener to add
     */
    public void addModifyListener(final Listener listener) {
        listeners.add(listener);
    }

    // javadoc inherited
    public void handleEvent(final Event event) {
        notifyListeners();
    }

    /**
     * Fires data modification events to all the listeners.
     */
    private void notifyListeners() {
        final Event toSend = new Event();
        for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
            final Listener listener = (Listener) iter.next();
            listener.handleEvent(toSend);
        }
    }

    /**
     * Sets the project object to be used for load/save operations and for the
     * initial value of the database project name.
     *
     * @param project the project object
     */
    public void setProject(final IProject project) {
        this.project = project;
    }

    /**
     * Sets the project name for the initial value of the database project name.
     * This value will only be used if no project object was set by the
     * constructor or the setProject method.
     *
     * @param projectName the name of the project
     */
    public void setProjectName(final String projectName) {
        this.projectName = projectName;
    }

    // javadoc inherited
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        setEnabled(enabled, getChildren());
    }

    /**
     * Recursively enables/disables the specified controls.
     *
     * @param enabled the new status
     * @param controls the controls to start with
     */
    private void setEnabled(final boolean enabled, final Control[] controls) {
        for (int i = 0; i < controls.length; i++) {
            if (controls[i] instanceof Composite) {
                setEnabled(enabled, ((Composite) controls[i]).getChildren());
            }
            controls[i].setEnabled(enabled);
        }
    }

    private class ButtonSelectionListener extends SelectionAdapter {

        public void widgetSelected(final SelectionEvent event) {
            if (event.getSource() == collaborativeWorking) {
                collaborativeWorkingChanged();
            } else {
                notifyListeners();
            }
        }
    }
}
