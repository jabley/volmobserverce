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
 * $Header: /src/voyager/com/volantis/mcs/papi/FormFragment.java,v 1.5 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Feb-02    Steve           VBM:2002021404 - PAPI fragment to override the 
 *                              protocol version.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 28-Mar-02    Steve           VBM:2001021404 - Fixed for method name changes
 *                              in FormFragment.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.papi.FormFragment;
import com.volantis.mcs.protocols.layouts.FormFragmentInstance;

/**
 */
public class FormFragmentImpl
        implements FormFragment {

    private FormFragmentInstance instance;

    /**
     * Creates a new instance of FormFragment
     */
    public FormFragmentImpl(FormFragmentInstance instance) {
        this.instance = instance;
    }


    public String getPreviousLinkText() {
        return instance.getPreviousLinkText();
    }

    public void setPreviousLinkText(String txt) {
        instance.setPreviousLinkText(txt);
    }

    public String getNextLinkText() {
        return instance.getNextLinkText();
    }

    public void setNextLinkText(String s) {
        instance.setNextLinkText(s);
    }

    public void setPreviousLinkStyleClass(String s) {
        instance.setPreviousLinkStyleClass(s);
    }

    public void setNextLinkStyleClass(String s) {
        instance.setNextLinkStyleClass(s);
    }

    public String getPreviousLinkStyleClass() {
        return instance.getPreviousLinkStyleClass();
    }

    public String getNextLinkStyleClass() {
        return instance.getNextLinkStyleClass();
    }

    public String isPreviousLinkBefore() {
        return instance.isPreviousLinkBefore();
    }

    public void setPreviousLinkBefore(String before) {
        instance.setPreviousLinkBefore(before);
    }

    public String isNextLinkBefore() {
        return instance.isNextLinkBefore();
    }

    public void setNextLinkBefore(String before) {
        instance.setNextLinkBefore(before);
    }

    public String hasReset() {
        return instance.hasReset();
    }

    public void setReset(String flag) {
        instance.setReset(flag);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 ===========================================================================
*/
