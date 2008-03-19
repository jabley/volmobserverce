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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MenuAttributes.java,v 1.19 2003/04/24 16:52:57 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 23-Jul-01    Paul            VBM:2001070507 - Almost completely rewrote in
 *                              order to support the redesigned and
 *                              reimplemented menu support.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 31-Mar-03    Sumit           VBM:2003032714 - This class implements the 
 *                              MenuItemCollector interface
 * 24-Apr-03    Adrian          VBM:2003040903 - Added new attributes prompt,
 *                              error message and help. added appropriate 
 *                              getter and setter methods. 
 * 24-Apr-03    Byron           VBM:2003042402 - Added manualDTMF property with
 *                              getter and setter.
 * 27-May-03    Allan           VBM:2003052207 - Added 
 *                              requiresVerticalSeparator and 
 *                              requiresHorizontalSeparator properties. 
 * 28-May-03    Allan           VBM:2003051904 - Removed previous 2003052207
 *                              change. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.assets.TextAssetReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @deprecated Old style menu needs to be removed.
 */
public class MenuAttributes
        extends MCSAttributes
        implements MenuItemCollector {

    private List itemsAndGroups;
    private Pane pane;
    private String type;

    /**
     * The prompt property.
     */
    private TextAssetReference prompt;

    /**
     * The help property.
     */
    private TextAssetReference help;

    /**
     * The error message property.
     */
    private TextAssetReference errmsg;

    /**
     * Store the flag for manualDTMF
     */
    private boolean manualDTMF;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public MenuAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     *
     * @todo better the initialization of all the values is not done in METIS.
     */
    private void initialise() {

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("menu");

        if (itemsAndGroups == null) {
            itemsAndGroups = new ArrayList();
        } else {
            itemsAndGroups.clear();
        }

        pane = null;
        type = null;
        prompt = null;
        help = null;
        errmsg = null;
        manualDTMF = false;
    }

    /**
     * Add a menu item to the collection of menu itemsAndGroups.
     */
    public void addItem(MenuItem item) {
        itemsAndGroups.add(item);
    }

    /**
     * Get the collection of menu itemsAndGroups.
     */
    public Collection getItems() {
        return itemsAndGroups;
    }

    public void addGroup(MenuItemGroupAttributes group) {
        itemsAndGroups.add(group);
    }

    /**
     * Set the pane property.
     *
     * @param pane The new value of the pane property.
     */
    public void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     * Get the value of the pane property.
     *
     * @return The value of the pane property.
     */
    public Pane getPane() {
        return pane;
    }

    /**
     * Set the type property.
     *
     * @param type The new value of the type property.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the value of the type property.
     *
     * @return The value of the type property.
     */
    public String getType() {
        return type;
    }

    /**
     * Get the value of the error message property.
     *
     * @return The value of the error message property.
     */
    public TextAssetReference getErrmsg() {
        return errmsg;
    }

    /**
     * Set the value of the error message property.
     *
     * @param errmsg The new value of the error message property.
     */
    public void setErrmsg(TextAssetReference errmsg) {
        this.errmsg = errmsg;
    }

    /**
     * Get the value of the help property.
     *
     * @return The value of the help property.
     */
    public TextAssetReference getHelp() {
        return help;
    }

    /**
     * Set the value of the help property.
     *
     * @param help The new value of the help property.
     */
    public void setHelp(TextAssetReference help) {
        this.help = help;
    }

    /**
     * Get the value of the prompt property.
     *
     * @return The value of the prompt property.
     */
    public TextAssetReference getPrompt() {
        return prompt;
    }

    /**
     * Set the value of the prompt property.
     *
     * @param prompt The new value of the prompt property.
     */
    public void setPrompt(TextAssetReference prompt) {
        this.prompt = prompt;
    }


    /**
     * Get the value of the manual DTMF property
     *
     * @return the value of the manual DTMF property
     */

    public boolean isManualDTMF() {
        return manualDTMF;
    }

    /**
     * Set the manual DTMF value to be as that passed in as a parameter.
     *
     * @param manualDTMF the boolean value used to set this property
     */
    public void setManualDTMF(boolean manualDTMF) {
        this.manualDTMF = manualDTMF;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
