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
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.descriptor.BaseClassDescriptor;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.ListClassDescriptor;
import com.volantis.mcs.model.descriptor.OpaqueClassDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptorVisitor;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.text.Document;
import java.awt.Component;
import java.awt.event.ActionEvent;

class ControlCreator
        implements TypeDescriptorVisitor {

    private final GUI gui;

    private Component component;
    private Proxy proxy;

    public ControlCreator(GUI gui) {
        this.gui = gui;
    }

    public Component createControl(TypeDescriptor descriptor, Proxy proxy) {
        this.proxy = proxy;
        component = null;
        descriptor.accept(this);
        return component;
    }

    public void visit(BeanClassDescriptor descriptor) {
        final BeanProxy beanProxy = (BeanProxy) proxy;
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                EditDialog dialog = new EditDialog(
                        new BeanComponent(gui, beanProxy));
                dialog.show();
            }
        };
        action.putValue(Action.NAME, "Edit...");
        component = new JButton(action);
    }

    public void visit(ListClassDescriptor descriptor) {
        final ListProxy listProxy = (ListProxy) proxy;
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                EditDialog dialog = new EditDialog(
                        new ListComponent(gui, listProxy));
                dialog.show();
            }
        };
        action.putValue(Action.NAME, "Edit...");
        component = new JButton(action);
    }

    public void visit(BaseClassDescriptor descriptor) {
        JTextField field = new JTextField();
        attach(field, proxy);
        component = field;
    }

    public void visit(OpaqueClassDescriptor descriptor) {
        JTextField field = new JTextField();
        attach(field, (OpaqueProxy) proxy);
        component = field;
    }

    private void attach(final JTextField field, Proxy proxy) {
        final Document document = field.getDocument();
//        DocumentListener listener = new SimpleProxy2DocumentListenerAdapter(
//                simpleProxy);
//        document.addDocumentListener(listener);

        // Commented out for now as it causes an
        // "Attempt to mutate in notification" message to be thrown by the
        // Document when this was triggered as a result of typing in a field.
//        simpleProxy.addListener(
//                new InteractionEventListenerAdapter() {
//                    protected void interactionEvent(InteractionEvent event) {
//                        try {
//                            document.remove(0, document.getLength());
//                        } catch (BadLocationException e) {
//                            throw new IllegalStateException(e.getMessage());
//                        }
//                    }
//                }, false);

        ProxyRenderer renderer = gui.getRenderers().getRenderer(proxy);
        field.setText(renderer.render(proxy));
    }

    private void attach(final JTextField field, OpaqueProxy opaqueProxy) {
        new ProxyTextComponentBridge(opaqueProxy, field);

        Object modelObject = opaqueProxy.getModelObject();
        if (modelObject != null) {
            field.setText(modelObject.toString());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/13	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/10	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/8	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/6	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/3	pduffin	VBM:2005101811 Committing first stab at interaction layer

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
