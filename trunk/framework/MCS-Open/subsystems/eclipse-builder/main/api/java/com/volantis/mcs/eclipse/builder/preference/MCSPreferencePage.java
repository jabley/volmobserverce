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
package com.volantis.mcs.eclipse.builder.preference;

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.common.ClassVersionProperties;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import java.io.File;

/**
 * MCS Preference page.
 */
public class MCSPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {

    /**
     * Preference key for Oracle database driver location.
     */
    public static final String PREF_ORACLE_DRIVER_LOCATION =
            "oracle.driver.location";

    /**
     * Preference key for fast device repository validation option.
     */
    public static final String PREF_FASTER_VALIDATION =
            "faster.validation";

    /**
     * To determine if the oracle driver location changed since the
     * last time it was observed. Note: wanted to use property change
     * listener and boolean flag for this but the change listeners were
     * not notified on change for some reason - using Eclipse v3.2. Also
     * decided not to keep account of the original values to differentiate
     * between apply and ok because this is just over complicates things.
     */
    private String previousOracleDriverLocation;

    /**
     * To determine if faster validation changed since the
     * last time it was observed. Note: wanted to use property change
     * listener and boolean flag for this but the change listeners were
     * not notified on change for some reason - using Eclipse v3.2.  Also
     * decided not to keep account of the original values to differentiate
     * between apply and ok because this is just over complicates things.
     */
    private boolean previousFasterValidation;

    /**
     * True if this installation of MCS is the community edition - we will
     * hide some fields that are only relevant to the professional edition.
     */
    private boolean communityEdition;

    /**
     * Creates an MCSPreferencePage object.
     */
    public MCSPreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        communityEdition = ClassVersionProperties.isCommunityEdition();
        setPreferenceStore(BuilderPlugin.getDefault().getPreferenceStore());
    }

    // javadoc inherited
    protected void createFieldEditors() {
        if (!communityEdition) {
            final FileFieldEditor driverLocation =
                    new LocationFieldEditor(PREF_ORACLE_DRIVER_LOCATION,
                            EclipseCommonMessages.getString(
                                    "MCSPreferencePage.oracleDriverLocation"),
                            false,
                            getFieldEditorParent());
            addField(driverLocation);
        }

        final BooleanFieldEditor fasterValidation =
                new BooleanFieldEditor(PREF_FASTER_VALIDATION,
                        EclipseCommonMessages.getString(
                                "MCSPreferencePage.fasterValidation"),
                        BooleanFieldEditor.DEFAULT, getFieldEditorParent());
        addField(fasterValidation);
    }


    // javadoc inherited
    public void init(final IWorkbench iWorkbench) {
        IPreferenceStore store =
                BuilderPlugin.getDefault().getPreferenceStore();
        if (!communityEdition) {
            previousOracleDriverLocation =
                    store.getString(MCSPreferencePage.
                            PREF_ORACLE_DRIVER_LOCATION);
        }
        previousFasterValidation = store.getBoolean(
                MCSPreferencePage.PREF_FASTER_VALIDATION);
    }

    // Javadoc inherited.
    public boolean performOk() {

        // call super.performOK first so the fields get updated
        boolean ok = super.performOk();
        if (ok) {
            IPreferenceStore store =
                    BuilderPlugin.getDefault().getPreferenceStore();

            boolean fasterValidation = store.getBoolean(
                    MCSPreferencePage.PREF_FASTER_VALIDATION);

            String oracleDriverLocation = store.getString(
                    MCSPreferencePage.PREF_ORACLE_DRIVER_LOCATION);

            StringBuffer message = null;

            if (!communityEdition &&
                    !previousOracleDriverLocation.equals(oracleDriverLocation)) {
                message = new StringBuffer();
                message.append(EclipseCommonMessages.getString(
                        "MCSPreferencePage.newOracleDriverLocation"));
            }

            if (previousFasterValidation != fasterValidation) {
                if (message == null) {
                    message = new StringBuffer();
                } else {
                    message.append("\n\n");
                }
                message.append(EclipseCommonMessages.getString(
                        "MCSPreferencePage.newFasterValidation")).append("\n\n");
                if (fasterValidation) {
                    message.append(EclipseCommonMessages.getString(
                            "MCSPreferencePage.fasterValidationEnabled"));
                } else {
                    message.append(EclipseCommonMessages.getString(
                            "MCSPreferencePage.fasterValidationDisabled"));
                }
            }

            if (message != null) {
                final String title = EclipseCommonMessages.getString(
                        "MCSPreferencePage.informationDialog.title");

                MessageDialog
                        .openInformation(getShell(), title,
                                message.toString());
            }

            previousOracleDriverLocation = oracleDriverLocation;
            previousFasterValidation = fasterValidation;
        }
        return ok;
    }

    private class LocationFieldEditor extends FileFieldEditor {
        public LocationFieldEditor(final String name, final String labelText,
                                   final boolean enforceAbsolute,
                                   final Composite parent) {
            super(name, labelText, enforceAbsolute, parent);
        }

        protected void doFillIntoGrid(final Composite parent,
                                      final int numColumns) {
            setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
            super.doFillIntoGrid(parent, numColumns);
        }

        protected boolean checkState() {
            boolean ok = true;
            if (communityEdition) {
                clearMessage();
            } else {
                final String value = getStringValue();
                final File file = new File(value);
                if (value != null && value.length() > 0) {
                    if (!file.exists()) {
                        setMessage(EclipseCommonMessages.getString(
                                "MCSPreferencePage.error.oracleDriverFileNotExist"),
                                DialogPage.ERROR);
                        ok = false;
                    } else if (!file.isFile()) {
                        setMessage(EclipseCommonMessages.getString(
                                "MCSPreferencePage.error.oracleDriverFileNotFile"),
                                DialogPage.ERROR);
                        ok = false;
                    } else if (!file.canRead()) {
                        setMessage(EclipseCommonMessages.getString(
                                "MCSPreferencePage.error.oracleDriverFileCannotRead"),
                                DialogPage.ERROR);
                        ok = false;
                    } else {
                        clearMessage();
                    }
                } else {
                    clearMessage();
                }
            }

            return ok;
        }
    }
}
