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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * A Combo box which allows selection of file filters for a type of mariner
 * element. 
 * <p>The element must have two sets of entries in ControlsMessages.properties 
 * one set defining the encoding and another defining the file extensions for
 * that encoding. An example entry set is:</p>
 * <ul>
 * <li>FilterCombo.imageComponent.encoding.jpeg=JPEG Files ({0})</li>
 * <li>FilterCombo.imageComponent.extentions.jpeg=*.jpeg;*.jpg</li>
 * <li>FilterCombo.imageComponent.encoding.gif=GIF Files ({0})</li>
 * <li>FilterCombo.imageComponent.extentions.gif=*.gif</li>
 * </ul>
 * <p>In this instance, the FilterCombo should be created with "imageComponent"
 * being passed to the constructor. This will create <code>FileFilter</code>
 * objects for jpeg and gif files and populate the combo with the entries:</p>
 * <ul>
 * <li>JPEG Files (*.jpeg;*.jpg)</li>
 * <li>GIF Files (*.gif)</li>
 * </ul>
 * <p>The widget wraps an SWT <code>Combo</code> widget to create a drop-down
 * and read-only style combo control allowing the selection of FileFilter 
 * objects.</p>
 * <p>This control 'can' be created standalone but should be created using a 
 * class which implements <code>FilterComboFactory</code>. These factories
 * will both create and populate the combo for a given mariner component.</p>
 */
public class FilterCombo extends Composite {
    
    /**
     * The wrapped Combo control
     */
    private Combo theCombo;

    /**
     * The current filters
     */
    private List filters;
    
    
    /**
     * Creates a new FilterCombo and populates it with selections for a named
     * mariner element.
     *
     * @param parent a composite widget to add this widget to.
     * @param element the mariner element we are populating the combo for.
     */
    public FilterCombo(Composite parent, String element) {
        
        super(parent, SWT.NONE);
        
        setLayout( new FormLayout() );
        
        theCombo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        filters = new ArrayList();

        super.setBackground(theCombo.getBackground());
        super.setForeground(theCombo.getForeground());
        super.setFont(theCombo.getFont());

        // Listen for resize events
        theCombo.addListener(SWT.Resize,
            new Listener() {
                public void handleEvent(Event e) {
                    onResize();
                }
            });

        // Listen for focus events
        theCombo.addListener(SWT.FocusIn,
            new Listener() {
                public void handleEvent(Event e) {
                    onFocusIn();
                }
            });
            
        // Populate the combo from the properties file
        populateFromProperties( element );

        initAccessible();
    }
    
    /**
     * Add a listener
     * @param listener the listener to Combo selections 
     */
    public void addSelectionListener( SelectionListener listener ) {
        theCombo.addSelectionListener( listener );
    }

    /**
     * Remove a listener
     * @param listener the listener to Combo selections
     */
    public void removeSelectionListener( SelectionListener listener ) {
        theCombo.removeSelectionListener( listener );
    }

    /**
     * Set the background colour of the control
     *
     * @param color the colour to use
     * @see org.eclipse.swt.widgets.Control#setBackground
     */
    public void setBackground(Color color) {
        super.setBackground(color);
        theCombo.setBackground(color);
    }

    /**
     * Specify the cursor to use when the mouse is over the control
     *
     * @param cursor the cursor to use
     * @see org.eclipse.swt.widgets.Control#setCursor
     */
    public void setCursor(Cursor cursor) {
        super.setCursor(cursor);
        theCombo.setCursor(cursor);
    }

    /**
     * Enable or Disable the control.
     *
     * @param enable true if control is enabled, otherwise false
     * @see org.eclipse.swt.widgets.Control#setEnabled
     */
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        theCombo.setEnabled(enable);
    }

    /**
     * Set the font to use in the control
     *
     * @param font the font to use
     * @see org.eclipse.swt.widgets.Control#setFont
     */
    public void setFont(Font font) {
        super.setFont(font);
        theCombo.setFont(font);
    }

    /**
     * Sets the text foreground colour
     *
     * @param colour the new colour or null for the default system colour
     * @see org.eclipse.swt.widgets.Control#setForeground
     */
    public void setForeground(Color colour) {
        super.setForeground(colour);
        theCombo.setForeground(colour);
    }

    /**
     * Sets the receiver's pop up menu.
     * All controls may optionally have a pop up menu that is displayed when
     * the user requests one for the control.
     *
     * @param menu the new pop up menu
     * @see org.eclipse.swt.widgets.Control#setMenu
     */
    public void setMenu(Menu menu) {
        super.setMenu(menu);
        theCombo.setMenu(menu);
    }

    /**
     * Sets the tooltip text for the control
     * 
     * @param tip the new tool tip text or null if no tip is to be shown
     * @see org.eclipse.swt.widgets.Control#setToolTipText
     */
    public void setToolTipText(String tip) {
        super.setToolTipText(tip);
        theCombo.setToolTipText(tip);
    }

    /**
     * Called when the control is resized. 
     * This is called by our event listener when the resize event arrives, all 
     * we need to do is pass on the news to the combo. The event occurs AFTER 
     * the actual resize so getClientArea() will return the size that the combo
     * should be resized to.
     * 
     * @see org.eclipse.swt.widgets.Scrollable#getClientArea
     * @see org.eclipse.swt.widgets.Control#setBounds
     */
    private void onResize() {
        Rectangle area = getClientArea();
        theCombo.setBounds(0, 0, area.width, area.height);
    }

    /**
     * Called when we are given focus.
     * This is called by our event listener when a focus event arrives. All we
     * do is give the focus to the Combo control.
     * 
     * @see Composite#setFocus
     */
    private void onFocusIn() {
        theCombo.setFocus();
    }

    
    /**
     * Populate this combo with entries from ControlsMessages.properties
     * <p>
     * The element name is the type of properties we are extracting names for, 
     * imageComponent for example. Each component will have a list of encoding
     * and extentions entries for each of the file types supported by that
     * component. For example imageComponent has entries :</p>
     * <ul>
     * <li>FilterCombo.imageComponent.encoding.jpeg=JPEG Images ({0})</li>
     * <li>FilterCombo.imageComponent.extentions.jpeg=*.jpg;*.jpeg</li>
     * <li>FilterCombo.imageComponent.encoding.gif=GIF Images ({0})</li>
     * <li>FilterCombo.imageComponent.extentions.gif=*.gif</li>
     * </ul>
     * <p>This method will extract all property keys beginning with
     * FilterCombo.<em>element</em>.encoding. and merge this with the similarly
     * named FilterCombo.<em>element</em>.extentions. key to create the entries
     * in the combo box "JPEG Images (*.jpg;*.jpeg)" for example. A FileFilter
     * will also be created to filter resources for the extentions.
     *  
     * @param element the name of the element to extract the properties for
     * @see FileFilter
     */
    private void populateFromProperties( String element ) {
        
        String encodingKey, extentionKey;
        String encoding, extention, label;
        
        StringBuffer prefix = new StringBuffer("FilterCombo.");
        prefix.append( element ).append( ".encoding.");
        List encodings = EclipseCommonMessages.getSortedKeys( 
                            ControlsMessages.getResourceBundle(),
                            prefix.toString(), String.CASE_INSENSITIVE_ORDER);
        
        for( int i = 0; i < encodings.size(); i++ ) {
            encodingKey = (String)encodings.get(i);
            extentionKey = getExtentionKey( element, encodingKey );

            encoding = ControlsMessages.getString( encodingKey );
            extention = ControlsMessages.getString( extentionKey );            
            label = MessageFormat.format( encoding, new String[]{ extention });                
            
            filters.add( new FileFilter( extention ) );
            theCombo.add( label );
        }
    }

    /**
     * Create the name of the extensions key from an encoding key. 
     * This is basically taking the last part of the encoding key and
     * creating the extentions key from it. 
     * FilterCombo.imageComponent.encoding.jpeg as an encoding key for example
     * will yield FilterCombo.imageComponent.extentions.jpeg as the extentions
     * key.
     * 
     * @param element the name of the element to extract the properties for
     * @param encodingKey the key for the encoding element.
     * @return the key for the extentions element relating to the encoding 
     * element  
     */
    private String getExtentionKey( String element, String encodingKey ) {
        StringBuffer key = new StringBuffer("FilterCombo.");
        key.append( element ).append( ".extentions");
        key.append( encodingKey.substring( encodingKey.lastIndexOf('.')) );
        return key.toString();      
    }
    
    /**
     * Return the ViewerFilter object that maps to the current selection
     * index of the wrapped Combo
     * @return the currently selected FileFilter.
     */
    public ViewerFilter getSelectedFilter() {
        ViewerFilter vf = null;
        int idx = theCombo.getSelectionIndex();
        if( idx > -1 ) {
            vf = (ViewerFilter)filters.get( idx ); 
        }
        return vf;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        SingleComponentACL acl = new SingleComponentACL() {
            public void getValue(AccessibleControlEvent ae) {
                ae.result = theCombo.getText();
            }
        };
        acl.setControl(this);
        acl.setRole(ACC.ROLE_COMBOBOX);
        getAccessible().addAccessibleControlListener(acl);

        StandardAccessibleListener al = new StandardAccessibleListener(this);
        getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 14-Nov-03	1835/2	pcameron	VBM:2003102801 Added GenericAssetCreation wizard page and supporting resources

 03-Nov-03	1661/4	steve	VBM:2003102410 javadoc and merge fixes

 31-Oct-03	1661/2	steve	VBM:2003102410 Moved messages to ControlsMessages and Factory should not be a singleton

 ===========================================================================
*/
