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
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

public class BeanComponent
        extends JComponent {

    private final GUI gui;

    public BeanComponent(GUI gui, BeanProxy proxy) {
        this.gui = gui;

        BeanClassDescriptor descriptor = proxy.getBeanClassDescriptor();
        List properties = descriptor.getPropertyDescriptors();

        int count = properties.size();
        setLayout(new BorderLayout());

        if (count > 1) {
            JPanel panel = new JPanel();
            add(new JScrollPane(panel), BorderLayout.CENTER);

            panel.setLayout(new GridLayout(count, 2));

            ControlCreator controlCreator = new ControlCreator(this.gui);

            for (int i = 0; i < count; i++) {
                PropertyDescriptor property = (PropertyDescriptor)
                        properties.get(i);

                TypeDescriptor propertyType = property.getPropertyType();

                PropertyIdentifier identifier = property.getIdentifier();
                Proxy propertyProxy = proxy.getPropertyProxy(identifier);

                panel.add(new JLabel(identifier.getDescription()));
                panel.add(
                        controlCreator.createControl(
                                propertyType, propertyProxy));
            }
        } else if (count == 1) {
            PropertyDescriptor property =
                    (PropertyDescriptor) properties.get(0);
            PropertyIdentifier identifier = property.getIdentifier();
            Proxy propertyProxy = proxy.getPropertyProxy(identifier);
            add(new ComponentCreator(gui).create(propertyProxy),
                BorderLayout.CENTER);

        } else {
            throw new IllegalStateException("No properties defined for bean");
        }
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
