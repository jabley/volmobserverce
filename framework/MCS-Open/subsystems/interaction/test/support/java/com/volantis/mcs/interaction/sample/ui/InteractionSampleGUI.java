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
import com.volantis.mcs.interaction.InteractionFactory;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.sample.descriptors.Descriptors;
import com.volantis.mcs.interaction.sample.model.Contacts;
import com.volantis.mcs.interaction.sample.model.Person;
import com.volantis.mcs.model.property.PropertyIdentifier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InteractionSampleGUI {
    private final GUI gui;

    public static void main(String[] args) {
        GUI gui = new GUI(null);
        InteractionSampleGUI sampleGUI = new InteractionSampleGUI(gui);
        sampleGUI.run();
    }

    private BeanProxy contactsProxy;

    private ListProxy contactsListProxy;

    public InteractionSampleGUI(GUI gui) {
        this.gui = gui;
        InteractionFactory factory = InteractionFactory.getDefaultInstance();
        InteractionModel descriptor =
                factory.createInteractionModel(Descriptors.MODEL_DESCRIPTOR);

        contactsProxy = (BeanProxy) descriptor.createProxyForModelObject(
                new Contacts());

        contactsListProxy =
                (ListProxy) contactsProxy.getPropertyProxy(Contacts.CONTACTS);
    }

    private void run() {

        JFrame frame = new JFrame();
        frame.setSize(200, 200);

        Container container = frame.getContentPane();
        container.setBackground(Color.RED);

        JPanel panel = new JPanel();
        panel.setBackground(Color.GREEN);
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        JList list = new JList();
//        list.setBackground(Color.BLUE);
        panel.add(list, gridBagConstraints);

        ListModel model = new InteractionSwingListModel(contactsListProxy);

        list.setModel(model);
        list.setCellRenderer(
                new DefaultListCellRenderer() {
                    public Component getListCellRendererComponent(
                            JList list, Object value, int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        BeanProxy personProxy = (BeanProxy) value;
                        String firstName = getPropertyAsString(
                                personProxy, Person.FIRST_NAME);
                        String lastName = getPropertyAsString(
                                personProxy, Person.LAST_NAME);
                        String age = getPropertyAsString(
                                personProxy, Person.AGE);

                        String details = firstName + " " + lastName + " - " + age;

                        return super.getListCellRendererComponent(
                                list, details, index, isSelected, cellHasFocus);
                    }
                });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.YELLOW);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        panel.add(buttonPanel);

        buttonPanel.setLayout(new GridLayout(2, 1));

        JButton button;

        AbstractAction addContactAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                BeanProxy proxy = (BeanProxy) contactsListProxy.addItemProxy();
                EditDialog dialog = new EditDialog(new BeanComponent(gui, proxy));
                dialog.show();
            }
        };
        addContactAction.putValue(Action.NAME, "Add");

        button = new JButton(addContactAction);
        buttonPanel.add(button);
        button = new JButton("Remove");
        buttonPanel.add(button);

        container.add(panel);

        frame.show();
    }

    private String getPropertyAsString(
            BeanProxy beanProxy, PropertyIdentifier identifier) {

        OpaqueProxy opaqueProxy = (OpaqueProxy) beanProxy.getPropertyProxy(
                identifier);
        return opaqueProxy.getModelObjectAsString();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/4	pduffin	VBM:2005111410 Added support for copying model objects

 31-Oct-05	9961/9	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/7	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
