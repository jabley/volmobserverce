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
package com.volantis.mcs.eclipse.builder.editors.policies;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * An editor for policies that only provide simple attributes, without
 * variants.
 */
public abstract class BasicPolicyEditor extends PolicyEditor {
    public void createPartControl(Composite composite) {
        ScrolledComposite scroller =
                new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);
        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);

        GridData data = new GridData(GridData.FILL_BOTH);
        scroller.setLayoutData(data);

        Composite scrollable = new Composite(scroller, SWT.NONE);
        scroller.setContent(scrollable);

        data = new GridData(GridData.FILL_BOTH);
        scrollable.setLayoutData(data);

        setDefaultColour(scrollable);
        setDefaultColour(scroller);

        // TODO better get these from the appropriate place
        int marginHeight = 15;
        int marginWidth = 13;
        int verticalSpacing = 13;

        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginHeight = marginHeight;
        gridLayout.marginWidth = marginWidth;
        gridLayout.verticalSpacing = verticalSpacing;
        scrollable.setLayout(gridLayout);

        // The Alerts and Actions Section
        createAlertsActionsSection(scrollable);

        // The Assets and Attributes section
        createAssetAttributesSection(scrollable);

        configureScrolling(scroller);
    }
}
