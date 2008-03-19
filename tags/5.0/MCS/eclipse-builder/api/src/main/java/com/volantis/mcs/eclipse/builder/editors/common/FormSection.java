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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * A common parent class for all form sections.
 */
public abstract class FormSection extends Composite {
    public FormSection(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * Create a form section in a default style.
     *
     * @param parent The parent component for the section
     * @param title The title of the section
     * @param description The description of the section
     * @param style The style of the section
     * @return A section created to the specification provided
     */
    protected Section createSection(Composite parent, String title,
                                    String description, int style) {
        FormToolkit formToolkit =
                new FormToolkit(parent.getShell().getDisplay());
        style |= Section.TITLE_BAR | Section.DESCRIPTION;
        Section section = formToolkit.createSection(parent, style);
        section.setText(title);
        if(description!=null) {
            section.setDescription(description);
        }
        return section;
    }

    /**
     * Sets the colour of a control to the default background colour for the
     * GUI (the list background colour).
     *
     * @param control The control to set the colour for
     */
    protected void setDefaultColour(Control control) {
        control.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
