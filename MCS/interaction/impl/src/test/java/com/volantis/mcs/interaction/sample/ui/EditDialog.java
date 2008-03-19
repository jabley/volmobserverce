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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.interaction.sample.ui;

import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.property.PropertyIdentifier;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.List;

public class EditDialog
        extends JDialog {

    public EditDialog(JComponent component) {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(component, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        buttonPanel.add(new JButton("Cancel"));
        buttonPanel.add(new JButton("Ok"));

        setSize(200, 100);
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
