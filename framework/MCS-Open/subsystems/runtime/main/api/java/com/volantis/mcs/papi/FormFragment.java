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

public interface FormFragment {
    String getPreviousLinkText();

    void setPreviousLinkText( String txt );

    String getNextLinkText();

    void setNextLinkText( String s );

    void setPreviousLinkStyleClass( String s );

    void setNextLinkStyleClass( String s );

    String getPreviousLinkStyleClass();

    String getNextLinkStyleClass();

    String isPreviousLinkBefore();

    void setPreviousLinkBefore( String before );

    String isNextLinkBefore();

    void setNextLinkBefore( String before );

    String hasReset();

    void setReset( String flag );
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/4	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
