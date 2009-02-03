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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.common.Convertors;
import com.volantis.mcs.eclipse.common.NamedColor;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Manual test for the StyledGroup control. StyledGroup sizing and content
 * size are affected by the size of the font for the StyledGroup text and
 * also the width of the border. This test will test these aspects as well
 * as the general working behaviour.
 */
public class StyledGroupTest extends ControlsTestAbstract {

    /**
     * A ColorRegistry for this test case.
     */
    private static ColorRegistry colorRegistry;

    /**
     * Create a new StylegGroupTest
     */
    public StyledGroupTest(String title) {
        super(title);
        colorRegistry = new ColorRegistry(Display.getCurrent());
    }

    /**
     * Create a Control containing a StyleGroup and input fields allowing
     * the text, font size and border width to be changed.
     */
    public void createControl() {
        Composite composite = new Composite(getShell(), SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 6;
        composite.setLayout(layout);

        Label label = new Label(composite, SWT.NONE);
        label.setText("Text: ");
        final Text text = new Text(composite, SWT.SINGLE);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(data);

        label = new Label(composite, SWT.NONE);
        label.setText("Border Width: ");
        final Text borderWidth = new Text(composite, SWT.SINGLE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        borderWidth.setLayoutData(data);

        label = new Label(composite, SWT.NONE);
        label.setText("Font Size: ");
        final Text fontSize = new Text(composite, SWT.SINGLE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        fontSize.setLayoutData(data);

        label = new Label(composite, SWT.NONE);
        label.setText("Background Colour: ");
        final Text bgColor = new Text(composite, SWT.SINGLE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        bgColor.setLayoutData(data);

        label = new Label(composite, SWT.NONE);
        label.setText("Border Colour: ");
        final Text bdColor = new Text(composite, SWT.SINGLE);
        data = new GridData(GridData.FILL_HORIZONTAL);
        bdColor.setLayoutData(data);

        Composite groupContainer = new Composite(composite, SWT.NONE);
        groupContainer.setLayout(new GridLayout());
        data = new GridData(GridData.FILL_BOTH);
        data.horizontalSpan = 6;
        groupContainer.setLayoutData(data);

        final StyledGroup styledGroup = new StyledGroup(groupContainer,
                SWT.NONE);
        layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        styledGroup.setLayout(layout);
        data = new GridData(GridData.FILL_BOTH);
        styledGroup.setLayoutData(data);
        StyledGroup groupChild = new StyledGroup(styledGroup, SWT.NONE);
        groupChild.setText("A StyledGroup");
        data = new GridData(GridData.FILL_BOTH);
        groupChild.setLayoutData(data);
        groupChild = new StyledGroup(styledGroup, SWT.NONE);
        groupChild.setText("Another StyledGroup");
        data = new GridData(GridData.FILL_BOTH);
        groupChild.setLayoutData(data);
        text.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                styledGroup.setText(text.getText());
            }
        });

        borderWidth.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                try {
                    int width = Integer.parseInt(borderWidth.getText());
                    styledGroup.setBorderLineWidth(width);
                } catch (NumberFormatException e) {
                }
            }
        });

        fontSize.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                try {
                    int fontHeight = Integer.parseInt(fontSize.getText());
                    FontData fontData = styledGroup.getFont().getFontData()[0];
                    fontData.setHeight(fontHeight);
                    Font font = new Font(styledGroup.getDisplay(), fontData);
                    styledGroup.setFont(font);
                } catch (NumberFormatException e) {
                }
            }
        });

        bgColor.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                String colorValue = bgColor.getText();
                String ncHex = NamedColor.getHex(colorValue);
                if(ncHex!=null) {
                    Color color = colorRegistry.get(ncHex);
                    if(color==null) {
                        colorRegistry.put(ncHex, Convertors.hexToRGB(ncHex));
                        color = colorRegistry.get(ncHex);
                    }
                    styledGroup.setBackground(color);
                }
            }
        });

        bdColor.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                String colorValue = bdColor.getText();
                String ncHex = NamedColor.getHex(colorValue);
                if(ncHex!=null) {
                    Color color = colorRegistry.get(ncHex);
                    if(color==null) {
                        colorRegistry.put(ncHex, Convertors.hexToRGB(ncHex));
                        color = colorRegistry.get(ncHex);
                    }
                    styledGroup.setBorderColor(color);
                }
            }
        });



    }

    public String getSuccessCriteria() {
        String message = "This test tests various characteristics of " +
                "StyledGroup.\n\n The display should show a StyledGroup " +
                "containing two child StyledGroups in a single column\n\n" +
                "A selection of controls are provided that should affect" +
                " the outer most StyledGroup only in a manner dictated by " +
                "the control labels.";

        return message;
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the ColorSelector does not require input arguments.
     */
    public static void main(String[] args) {
        new StyledGroupTest("StyledGroup Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-04	5663/1	tom	VBM:2004081003 Replaced ColorRegistry with Eclipse V3.0.0 Version

 03-Aug-04	4902/2	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 ===========================================================================
*/
