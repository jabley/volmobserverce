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
package com.volantis.mcs.eclipse.ab.search.devices;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.search.SearchMessages;
import com.volantis.mcs.eclipse.ab.search.SearchScope;
import com.volantis.mcs.eclipse.ab.search.SearchScopeFactory;
import com.volantis.mcs.eclipse.controls.DefaultHistoryManager;
import com.volantis.mcs.eclipse.controls.HistoryManager;
import com.volantis.mcs.objects.FileExtension;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;

/**
 * The ISearchPage that is the dialog for initiating device searches.
 */
public class DeviceSearchPage extends DialogPage implements ISearchPage {
    /**
     * The prefix for property resources associated with this class.
     */
    private static final String RESOURCE_PREFIX = "DeviceSearchPage.";

    /**
     * The QualifiedName used as a key to obtain the history for the
     * searchStringCombo in this dialog.
     */
    private static final QualifiedName COMBO_HISTORY_ID =
            new QualifiedName(DeviceSearchPage.class.getName(),
                    "ComboHistory");

    /**
     * The HistoryManager that provides the available selection for the
     * Combo.
     */
    private HistoryManager historyManager =
            new DefaultHistoryManager(COMBO_HISTORY_ID);

    /**
     * A DialogSettings to persist values across sessions.
     */
    private IDialogSettings dialogSettings =
            ABPlugin.getDefault().getDialogSettings(getClass().getName());

    /**
     * The ISearchPageContainer associated with this DeviceSearchPage.
     */
    private ISearchPageContainer searchPageContainer;

    /**
     * The Combo for specifying the search string.
     */
    private Combo searchStringCombo;

    /**
     * The check box button designating case sensitivity.
     */
    private Button caseButton;

    /**
     * The check box button designating the use of a regular expression.
     */
    private Button regExpButton;

    /**
     * The check box button designating a device name search.
     */
    private Button deviceNameSearch;

    /**
     * The check box button designating a device patterns search.
     */
    private Button devicePatternsSearch;

    /**
     * Create the control that contains the dialog components for this
     * ISeachPage.
     * @param parent the parent Composite for the control.
     */
    public void createControl(Composite parent) {
        Composite main = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        main.setLayout(layout);
        if (parent.getLayout() instanceof GridLayout) {
            // This is necessary otherwise Eclipse does not properly layout
            // the main control. Really the parent Composite should be doing
            // this but this seems to be how Eclipse expects search dialogs
            // to be layed out see
            // org.eclipse.search.internal.ui.text.TextSearchPage.
            main.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        }

        Label label = new Label(main, SWT.LEFT);
        label.setText(SearchMessages.getString(RESOURCE_PREFIX +
                "searchStringCombo.text"));

        Composite innerComposite = new Composite(main, SWT.NONE);
        innerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        innerComposite.setLayout(layout);

        Composite comboComposite = new Composite(innerComposite, SWT.NONE);
        comboComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        comboComposite.setLayout(layout);

        searchStringCombo = new Combo(comboComposite, SWT.DROP_DOWN);
        searchStringCombo.setItems(historyManager.getHistory());
        GridData data = new GridData(GridData.FILL_BOTH);
        searchStringCombo.setLayoutData(data);

        searchStringCombo.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                getContainer().setPerformActionEnabled(!searchStringCombo.
                        getText().equals(""));
            }
        });
        Composite checkBoxComposite = new Composite(innerComposite, SWT.NONE);
        layout = new GridLayout();
        layout.numColumns = 1;
        layout.marginHeight = 0;
        layout.marginWidth = 0;

        checkBoxComposite.setLayout(layout);

        caseButton = new Button(checkBoxComposite, SWT.CHECK);
        caseButton.setText(SearchMessages.getString(RESOURCE_PREFIX +
                "caseSensitive.text"));
        caseButton.setSelection(dialogSettings.
                getBoolean(caseButton.getText()));

        regExpButton = new Button(checkBoxComposite, SWT.CHECK);
        regExpButton.setText(SearchMessages.getString(RESOURCE_PREFIX +
                "regularExpression.text"));
        regExpButton.setSelection(dialogSettings.
                getBoolean(regExpButton.getText()));

        final Label hint = new Label(comboComposite, SWT.LEFT);
        hint.setText(SearchMessages.getString(RESOURCE_PREFIX +
                "nonRegExp.hint.label"));
        hint.setVisible(!regExpButton.getSelection());

		regExpButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				hint.setVisible(!regExpButton.getSelection());
			}
		});

        Group searchGroup = new Group(main, SWT.SHADOW_ETCHED_IN);
        data = new GridData(GridData.FILL_HORIZONTAL);
        searchGroup.setLayoutData(data);
        searchGroup.setText(SearchMessages.getString(RESOURCE_PREFIX +
                "searchGroup.text"));
        searchGroup.setLayout(new FillLayout());

        deviceNameSearch = new Button(searchGroup, SWT.RADIO);
        deviceNameSearch.setText(SearchMessages.getString(RESOURCE_PREFIX +
                "deviceNameSearch.text"));
        deviceNameSearch.setSelection(dialogSettings.
                getBoolean(deviceNameSearch.getText()));

        devicePatternsSearch = new Button(searchGroup, SWT.RADIO);
        devicePatternsSearch.setText(SearchMessages.getString(RESOURCE_PREFIX +
                "devicePatternsSearch.text"));
        devicePatternsSearch.setSelection(dialogSettings.
                getBoolean(devicePatternsSearch.getText()));

        if(!devicePatternsSearch.getSelection() &&
                !deviceNameSearch.getSelection()) {
            deviceNameSearch.setSelection(true); // default
        }
        setControl(main);
        getContainer().setPerformActionEnabled(false);
    }

    /**
     * Called when the user selects the search button to perform the search.
     * @return true
     */
    public boolean performAction() {
        // Save the combo history so previous search strings can be retrieved
        historyManager.updateHistory(searchStringCombo.getText());
        historyManager.saveHistory();

        // Get the scope for the search based on the users choice.
        SearchScope scope = new SearchScopeFactory().
                createSearchScope(getContainer(),
                        new FileExtension[]{FileExtension.DEVICE_REPOSITORY});

        // Set up the options for the query based on the users choice.
        DeviceSearchQueryOptions options = new DeviceSearchQueryOptions();
        options.setCaseSensitive(caseButton.getSelection());
        options.setDeviceNameSearch(deviceNameSearch.getSelection());
        options.setDevicePatternSearch(devicePatternsSearch.getSelection());
        options.setRegularExpression(regExpButton.getSelection());

        // Create the query.
        DeviceSearchQuery query = new DeviceSearchQuery(scope,
                searchStringCombo.getText(), options);

        // Activate the view page and run the query.
        NewSearchUI.activateSearchResultView();
        NewSearchUI.runQuery(query);

        // Save the dialogSetting as a permanent source of the selected
        // dialog options so they can be retrieved next time the user opens
        // the dialog.
        dialogSettings.put(caseButton.getText(), caseButton.getSelection());
        dialogSettings.put(regExpButton.getText(), regExpButton.getSelection());
        dialogSettings.put(deviceNameSearch.getText(),
                deviceNameSearch.getSelection());
        dialogSettings.put(devicePatternsSearch.getText(),
                devicePatternsSearch.getSelection());

        return true;
    }


    // javadoc inherited.
    public void setContainer(ISearchPageContainer searchPageContainer) {
        this.searchPageContainer = searchPageContainer;
    }

    /**
     * Returns the search page's container.
     * @return the ISearchPageContainer containing this ISearchDialog.
     */
    private ISearchPageContainer getContainer() {
        return searchPageContainer;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Nov-04	6293/1	allan	VBM:2004110804 Prevent empty string device search

 08-Oct-04	5557/6	allan	VBM:2004070608 Device search

 ===========================================================================
*/
