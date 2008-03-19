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

package com.volantis.mcs.themes.ui;

import com.volantis.mcs.accessors.xml.jibx.JiBXReader;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.InteractionFactory;
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.sample.ui.BeanComponent;
import com.volantis.mcs.interaction.sample.ui.DiagnosticModel;
import com.volantis.mcs.interaction.sample.ui.GUI;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.Policy;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.shared.content.BinaryContentInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ThemeGUI
        extends GUI {
    private InteractionModel interactionModel;

    public static void main(String[] args) {
        ThemeGUI gui = new ThemeGUI(new ThemeProxyRenderers());
        gui.run();
    }

    private BeanProxy themeProxy;

    public ThemeGUI(ThemeProxyRenderers themeProxyRenderers) {
        super(themeProxyRenderers);

        InteractionFactory factory = InteractionFactory.getDefaultInstance();
        interactionModel = factory.createInteractionModel(
                PolicyModel.MODEL_DESCRIPTOR);
    }

    private void run() {

        final JFrame frame = new JFrame();

        final Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        Action openAction = new AbstractAction() {
            public void actionPerformed(ActionEvent event) {
                loadPolicy(frame);
            }
        };
        openAction.putValue(Action.NAME, "Open");
        openAction.putValue(Action.MNEMONIC_KEY, new Integer('O'));

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.add(openAction);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        frame.setSize(200, 200);
        frame.show();
    }

    private void loadPolicy(final JFrame frame) {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showDialog(frame, "Open");
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            InternalPolicyFactory factory =
                    InternalPolicyFactory.getInternalInstance();

            JiBXReader reader = factory.createPolicyReader();
            try {
                final FileInputStream inputStream = new FileInputStream(file);
                BinaryContentInput content = new BinaryContentInput(inputStream);

//                Theme theme = (Theme) reader.read(content, file.getPath());

                Policy policy = (Policy) reader.read(content, file.getPath());

                themeProxy = (BeanProxy) interactionModel
                        .createProxyForModelObject(policy);

                themeProxy.addListener (
                        new InteractionEventListenerAdapter() {
                            protected void interactionEvent (
                                    InteractionEvent event) {
                                if ( event.isOriginator () ) {
                                    themeProxy.validate ();
                                }
                            }
                        }, true);

                final Container contentPane = frame.getContentPane();
                contentPane.removeAll();
                contentPane.setLayout(new GridBagLayout());
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.fill = GridBagConstraints.BOTH;
                contentPane.add(
                        new BeanComponent(this, themeProxy), constraints);

                constraints.gridy = 1;
                constraints.weightx = 1;
                constraints.weighty = 0.5;
                constraints.fill = GridBagConstraints.BOTH;
                JTable diagnostics = new JTable();
                diagnostics.setModel(new DiagnosticModel(themeProxy));
                JComponent diagnosticWindow = new JScrollPane(diagnostics);
                contentPane.add(diagnosticWindow, constraints);

                // Validate the proxy now it has been loaded.
                themeProxy.validate();

                contentPane.validate();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-05	10756/2	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 09-Dec-05	10738/1	geoff	VBM:2005120813 JiBX is reading XML using system default encoding

 17-Nov-05	10341/4	pduffin	VBM:2005111410 Fixed issue with mapping classes to type descriptors

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/5	pduffin	VBM:2005111410 Added support for copying model objects

 16-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/10	pduffin	VBM:2005101811 Committing restructuring

 26-Oct-05	9961/7	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/5	pduffin	VBM:2005101811 Fixed issue with diagnostics and improved user interface to allow opening of files

 25-Oct-05	9961/3	pduffin	VBM:2005101811 Added diagnostic support and some commands

 21-Oct-05	9961/1	pduffin	VBM:2005101811 Committing first stab at interaction layer

 ===========================================================================
*/
