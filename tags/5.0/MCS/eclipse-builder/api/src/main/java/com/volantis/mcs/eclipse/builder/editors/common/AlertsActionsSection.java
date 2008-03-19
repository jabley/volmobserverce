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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.policies.PolicyType;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

import java.text.MessageFormat;
import java.util.List;

/**
 */
public class AlertsActionsSection extends FormSection {
    private static String RESOURCE_PREFIX = "AlertsActionsSection.";

    private EditorContext context;

    public AlertsActionsSection(Composite composite, int i,
                                EditorContext context) {
        super(composite, i);
        this.context = context;

        createDisplayArea();
    }

    private Hyperlink hyperlink;

    private Label noErrors;

    private StackLayout displayAreaLayout;

    private Composite displayArea;

    private void createDisplayArea() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        Section section =
                createSection(this, EditorMessages.getString(RESOURCE_PREFIX +
                "title"), null, Section.TWISTIE | Section.EXPANDED);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        displayAreaLayout = new StackLayout();
        displayArea.setLayout(displayAreaLayout);

        noErrors = new Label(displayArea, SWT.LEFT);
        setDefaultColour(noErrors);
        noErrors.setText(EditorMessages.getString(RESOURCE_PREFIX + "errors.none"));

        hyperlink = new Hyperlink(displayArea, SWT.NONE);
        setDefaultColour(hyperlink);

        final Color activeLinkForeground =
                JFaceColors.getActiveHyperlinkText(getDisplay());
        final Color inactiveLinkForeground =
                JFaceColors.getHyperlinkText(getDisplay());

        hyperlink.setForeground(inactiveLinkForeground);
        hyperlink.setText("");
        hyperlink.setUnderlined(true);

        IHyperlinkListener hyperlinkListener = new IHyperlinkListener() {
            public void linkEntered(HyperlinkEvent event) {
                hyperlink.setForeground(activeLinkForeground);
            }

            public void linkExited(HyperlinkEvent event) {
                hyperlink.setForeground(inactiveLinkForeground);
            }

            public void linkActivated(HyperlinkEvent event) {
                // TODO later We should probably do something here...
            }
        };
        hyperlink.addHyperlinkListener(hyperlinkListener);

        data = new GridData(GridData.FILL_HORIZONTAL);
        displayArea.setLayoutData(data);
        section.setClient(displayArea);

        updateErrorStatus();
    }

    public void updateErrorStatus() {
        Proxy proxy = context.getInteractionModel();
        List diagnostics = (proxy == null) ? null : proxy.getDiagnostics();
        boolean hasErrors = diagnostics != null && !diagnostics.isEmpty();
        if (hasErrors) {
            final PolicyType policyType = ((PolicyEditorContext) context).getPolicyType();
            if (policyType == null) {
                throw new IllegalArgumentException("Policy type is NOT defined for this context");
            }
            // policyType is used as a key to get a message from bundle
            String policyTypeMessage = EditorMessages.getString(RESOURCE_PREFIX + policyType.toString());
            String errorFormat = EditorMessages.getString(RESOURCE_PREFIX + "errors");
            String errorMessage = MessageFormat.format(errorFormat, new Object[] {
                new Integer(diagnostics.size()),                
                new Integer(0),
                new Integer(0),
                policyTypeMessage
            });
            hyperlink.setText(errorMessage);
            displayAreaLayout.topControl = hyperlink;
        } else {
            displayAreaLayout.topControl = noErrors;
        }
        displayArea.layout();
        displayArea.pack();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 15-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 01-Nov-05	9886/4	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
