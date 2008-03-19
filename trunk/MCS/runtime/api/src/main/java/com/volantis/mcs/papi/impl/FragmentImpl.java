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
 * $Header: /src/voyager/com/volantis/mcs/papi/Fragment.java,v 1.3 2003/03/12 16:10:43 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Doug            VBM:2001121701 - Created
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.papi.Fragment;
import com.volantis.mcs.protocols.layouts.FragmentInstance;

/**
 * The Fragment class.
 */
public class FragmentImpl
        implements Fragment {

    private FragmentInstance fragmentInstance;

    /**
     * Do not alter the access level of this constructor
     * It is package scope so that it is not availaible via the
     * public API.
     *
     * Creates a new Fragment instance.
     *
     * @param fragmentInstance A FragmentInstance object that is used
     *                         to access/modify the Fragments attributes.
     */
    FragmentImpl(FragmentInstance fragmentInstance) {
        this.fragmentInstance = fragmentInstance;
    }

    //Javadoc Inherited
    public String getName() {
        return fragmentInstance.getFragmentName();
    }

    //Javadoc Inherited
    public String getLinkToText() {
        return fragmentInstance.getLinkToText();
    }

    //Javadoc Inherited
    public String getLinkFromText() {
        return fragmentInstance.getLinkFromText();
    }

    //Javadoc Inherited
    public void overrideLinkToText(String linkToText) {
        fragmentInstance.setLinkToText(linkToText);
    }

    //Javadoc Inherited
    public void overrideLinkFromText(String linkFromText) {
        fragmentInstance.setLinkFromText(linkFromText);
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

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 ===========================================================================
*/
