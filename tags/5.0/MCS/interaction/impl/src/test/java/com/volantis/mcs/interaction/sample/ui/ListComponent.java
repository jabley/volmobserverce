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

import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.ClassDescriptor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListComponent
        extends JComponent {

    private final GUI gui;

    public ListComponent(final GUI gui, final ListProxy listProxy) {
        this.gui = gui;

        ListClassDescriptor descriptor = listProxy.getListClassDescriptor();
        final ClassDescriptor itemDescriptor = descriptor.getItemClassDescriptor();

        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;

        final JList list = new JList();
//        list.setBackground(Color.BLUE);
        add(new JScrollPane(list), gridBagConstraints);

        ListModel model = new InteractionSwingListModel(listProxy);

        list.setModel(model);
        list.setCellRenderer(
                new DefaultListCellRenderer() {
                    public Component getListCellRendererComponent(
                            JList list, Object value, int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

//                        BeanProxy personProxy = (BeanProxy) value;
//                        String firstName = getPropertyAsString(
//                                personProxy, Person.FIRST_NAME);
//                        String lastName = getPropertyAsString(
//                                personProxy, Person.LAST_NAME);
//                        String age = getPropertyAsString(
//                                personProxy, Person.AGE);
//
//                        String details = firstName + " " + lastName + " - " + age;
                        Proxy proxy = (Proxy) value;
                        ProxyRenderer renderer = gui.getRenderers().getRenderer(
                                proxy);
                        String details = renderer.render(proxy);

                        return super.getListCellRendererComponent(
                                list, details, index, isSelected, cellHasFocus);
                    }
                });
        list.addMouseListener(
                new MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            int index = list.getSelectedIndex();
                            Proxy itemProxy = listProxy.getItemProxy(index);
                            openEditDialog(itemProxy);
                        }
                    }
                });

        JPanel listButtonPanel = new JPanel();
        listButtonPanel.setBackground(Color.YELLOW);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        add(listButtonPanel);

        listButtonPanel.setLayout(new GridLayout(2, 1));

        JButton button;

        AbstractAction addContactAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                final Proxy itemProxy = listProxy.addItemProxy();
                openEditDialog(itemProxy);
            }
        };
        addContactAction.putValue(Action.NAME, "Add");

        button = new JButton(addContactAction);
        listButtonPanel.add(button);
        button = new JButton("Remove");
        listButtonPanel.add(button);
    }

    private void openEditDialog(Proxy itemProxy) {
        JComponent component = new ComponentCreator(gui)
                .create(itemProxy);
        EditDialog dialog = new EditDialog(component);
        dialog.show();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/5	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
