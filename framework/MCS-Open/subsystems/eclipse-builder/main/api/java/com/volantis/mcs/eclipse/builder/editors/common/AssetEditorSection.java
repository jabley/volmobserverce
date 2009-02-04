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

import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * A common superclass for asset editor sections to provide
 */
public abstract class AssetEditorSection extends FormSection implements InteractionFocussable {
    private String editorResourcePrefix;

    private Section section;

    public AssetEditorSection(Composite parent, int style, String resourcePrefix) {
        super(parent, style);
        editorResourcePrefix = resourcePrefix;
    }

    public Composite createDefaultDisplayArea() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        String description = EditorMessages.getString(editorResourcePrefix +
                "subheading");

        section =
                createSection(this, EditorMessages.getString(editorResourcePrefix +
                "title"), description, Section.EXPANDED);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        section.setLayout(layout);

        Composite displayArea = new Composite(section, SWT.NONE);
        data = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        displayArea.setLayoutData(data);

        data = new GridData(GridData.FILL_BOTH);
        displayArea.setLayoutData(data);

        section.setClient(displayArea);

        setDefaultColour(displayArea);

        return displayArea;
    }

    public Point getSectionSize() {
        return section.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    }

    public void packSection() {
        section.layout();
        section.pack();
    }
}
