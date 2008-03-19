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
package com.volantis.mcs.eclipse.controls.forms;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Helper class for creating Sections.
 */
public class SectionFactory {

    /**
     * Create a Section with the specified title and description. This
     * section have a TWISTIE | EXPANDED | TITLE_BAR | DESCRIPTION style and
     * no underline.
     * @param parent the parent Composite
     * @param title the title for the Section
     * @param description the description for the Section
     * @return the Section
     */
    public static Section createSection(Composite parent, String title,
                                        String description) {
        return createSection(parent, Section.TWISTIE | Section.EXPANDED,
                title, description);
    }

    /**
     * Create a Section with the specified title and description. This
     * section have a TITLE_BAR | DESCRIPTION style plus whatever styles are
     * specified in the style parameter.
     * @param parent the parent Composite
     * @param style the styles in addition to Section.TITLE_BAR and
     * Section.DESCRIPTION.
     * @param title the title for the Section
     * @param description the description for the Section
     * @return the Section
     */
    public static Section createSection(Composite parent,
                                        int style, 
                                        String title,
                                        String description) {
        final FormToolkit formToolkit = new FormToolkit(parent.getShell()
                .getDisplay());
        parent.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent arg0) {
                formToolkit.dispose();
            }
        });
        style |= Section.TITLE_BAR | Section.DESCRIPTION;
        Section section = formToolkit.createSection(parent, style);
        section.setText(title);
        if(description!=null) {
            section.setDescription(description);
        }
        // Uncomment the following line to provide a separator
        // formToolkit.createCompositeSeparator(section);

        GridLayout layout = new GridLayout();
        section.setLayout(layout);

        return section;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 ===========================================================================
*/
