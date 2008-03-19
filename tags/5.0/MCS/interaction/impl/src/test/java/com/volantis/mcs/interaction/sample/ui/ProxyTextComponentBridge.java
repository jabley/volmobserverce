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

import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class ProxyTextComponentBridge
        extends InteractionEventListenerAdapter
        implements DocumentListener {

    private final JTextComponent textComponent;
    private final OpaqueProxy opaqueProxy;
    private Object trigger;

    public ProxyTextComponentBridge(
            OpaqueProxy opaqueProxy, JTextComponent textComponent) {
        this.opaqueProxy = opaqueProxy;
        this.textComponent = textComponent;

        Document document = textComponent.getDocument();
        document.addDocumentListener(this);

        opaqueProxy.addListener(this, false);
    }

    public void changedUpdate(DocumentEvent e) {
        documentChanged();
    }

    public void insertUpdate(DocumentEvent e) {
        documentChanged();
    }

    public void removeUpdate(DocumentEvent e) {
        documentChanged();
    }

    private void documentChanged() {

        // If this event was triggered by a user change to the document then
        // update the simple proxy.
        if (trigger == null) {
            try {
                trigger = textComponent;
                String newText = textComponent.getText();
                opaqueProxy.setModelObject(newText);
            } finally {
                trigger = null;
            }
        }
    }

    protected void interactionEvent(InteractionEvent event) {

        // If this event was triggered by a change to the proxy through some
        // other view then update the proxy.
        if (trigger == null) {
            try {
                trigger = opaqueProxy;
                String newText = opaqueProxy.getModelObjectAsString();
                textComponent.setText(newText);
            } finally {
                trigger = null;
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 ===========================================================================
*/
