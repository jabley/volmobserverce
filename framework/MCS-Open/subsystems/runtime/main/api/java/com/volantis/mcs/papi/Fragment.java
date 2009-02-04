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

package com.volantis.mcs.papi;

/**
 * Fragment Class
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface Fragment {
    /**
     * A method to return the name of this Fragment.
     * @return the name
     */
    String getName();

    /**
     * A method to return the text used when generating a link to this fragment
     * This text is used by this fragment's parent when creating the link.
     * @return the link text
     */
    String getLinkToText();

    /**
     * A method to return the text used when generating a link to this fragment
     * This text is used by this fragment's children when creating the link.
     * @return the link text
     */
    String getLinkFromText();

    /**
     * A method to specify the text to use when generating a link to this
     * fragment. This text is used by the fragment's parent when creating
     * the link.
     * @param linkToText the text to use for the link
     */
    void overrideLinkToText(String linkToText);

    /**
     * A method to specify the text to use when generating a link to this
     * fragment. This text is used by the fragment's children when creating
     * the link.
     * @param linkFromText the text to use for the link
     */
    void overrideLinkFromText(String linkFromText);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/4	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
