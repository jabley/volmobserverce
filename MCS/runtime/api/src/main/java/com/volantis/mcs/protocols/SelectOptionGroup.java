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
 * $Header: /src/voyager/com/volantis/mcs/protocols/SelectOptionGroup.java,v 1.5 2003/04/17 10:21:07 geoff Exp $ 
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jul-2002  Sumit           VBM:2002073109 - Created for xfoptgroup support
 * 01-Oct-02    Allan           VBM:2002093002 - Added the prompt property.
 * 20-Jan-03    Doug            VBM:2002120213 - Class now implements the
 *                              Option interface. Implemented the visit()
 *                              method.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Attributes for a Option Group
 */
public class SelectOptionGroup
        extends MCSAttributes
        implements Option {

    private TextAssetReference caption;
    private TextAssetReference prompt;
    private List optionList = null;

    /**
     * Creates a new instance of SelectOptionGroup
     */
    public SelectOptionGroup() {
        initialise();
    }


    private void initialise() {
        caption = null;
        prompt = null;
        if (optionList != null) {
            optionList.clear();
        } else {
            optionList = new ArrayList();
        }
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();
        initialise();
    }

    /**
     * Set the value of the caption property.
     *
     * @param caption The new value of the caption property.
     */
    public void setCaption(TextAssetReference caption) {
        this.caption = caption;
    }

    /**
     * Get the value of the caption property.
     *
     * @return The value of the caption property.
     */
    public TextAssetReference getCaption() {
        return caption;
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
     * Get the value of the prompt property.
     *
     * @return The value of the prompt property.
     */
    public TextAssetReference getPrompt() {
        return prompt;
    }

    /**
     * Add an Option to the list
     */

    public void addSelectOption(SelectOption option) {
        optionList.add(option);
    }

    /**
     * Add a SelectOptionGroup to the list
     */
    public void addSelectOptionGroup(SelectOptionGroup option) {
        optionList.add(option);
    }

    public List getSelectOptionList() {
        return optionList;
    }

    // javadoc inherited from Option interface
    public void visit(OptionVisitor optionVisitor, Object object)
            throws ProtocolException {
        optionVisitor.visit(this, object);
    }

    /**
     * Pass the selected value onto all options in the optionList.
     *
     * @param value
     */
    public void selectedValues(List value) {

        Iterator optIterator = optionList.iterator();

        while (optIterator.hasNext()) {
            Option unknownOption = (Option) optIterator.next();
            unknownOption.selectedValues(value);
        }
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
